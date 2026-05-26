package com.lab.saboresdecolombia.core.domain.usecase

import com.lab.saboresdecolombia.core.data.local.dao.CachedRecipeDao
import com.lab.saboresdecolombia.core.data.mapper.RecipeMapper.toCacheEntity
import com.lab.saboresdecolombia.core.data.mapper.RecipeMapper.toDomain
import com.lab.saboresdecolombia.core.data.remote.MealApiService
import com.lab.saboresdecolombia.core.domain.model.Recipe
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.mutableListOf

class ProgressData(
    val progress: Float,
    val message: String
)

@Singleton
class InitializeAppUseCase @Inject constructor(
    private val api: MealApiService,
    private val translator: RecipeTranslator,
    private val cachedRecipeDao: CachedRecipeDao
) {
    suspend operator fun invoke(onProgress: (ProgressData) -> Unit): Result<Unit> {
        return try {
            val existingCount = cachedRecipeDao.getCount()
            if (existingCount > 0) {
                onProgress(ProgressData(1f, "Caché cargado ($existingCount recetas)"))
                delay(400)
                return Result.success(Unit)
            }

            onProgress(ProgressData(0.02f, "Obteniendo recetas de la API..."))

            val letters = 'a'..'z'
            val allMeals = coroutineScope {
                letters.map { letter ->
                    async {
                        api.filterByFirstLetter(letter.toString()).meals ?: emptyList()
                    }
                }.flatMap { it.await() }
            }

            val recipes = allMeals.map { it.toDomain() }.distinctBy { it.id }.sortedBy { it.name.lowercase() }
            val total = recipes.size

            onProgress(ProgressData(0.05f, "$total recetas encontradas. Traduciendo..."))

            val batchSize = 11
            val concurrentBatches = 5
            val semaphore = Semaphore(concurrentBatches)
            val translated = mutableListOf<Recipe>()

            coroutineScope {
                recipes.chunked(batchSize).forEach { batch ->
                    launch {
                        semaphore.withPermit {
                            val results = batch.map { translator.translateRecipe(it) }
                            synchronized(translated) {
                                translated.addAll(results)
                                val done = translated.size
                                val progress = 0.05f + (0.94f * done / total)
                                onProgress(
                                    ProgressData(
                                        progress,
                                        "Traduciendo: $done/$total recetas (${(progress * 100).toInt()}%)"
                                    )
                                )
                            }
                        }
                    }
                }
            }

            val sorted = translated.sortedBy { it.name.lowercase() }

            onProgress(ProgressData(0.99f, "Guardando $total recetas en caché..."))
            cachedRecipeDao.insertAll(sorted.map { it.toCacheEntity() })

            onProgress(ProgressData(1f, "Listo — $total recetas en caché"))
            delay(500)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
