package com.lab.saboresdecolombia.core.domain.usecase

import android.util.Log
import com.lab.saboresdecolombia.core.data.local.dao.TranslationCacheDao
import com.lab.saboresdecolombia.core.data.local.entity.TranslationCacheEntity
import com.lab.saboresdecolombia.core.data.remote.TranslationApiService
import com.lab.saboresdecolombia.core.domain.model.Ingredient
import com.lab.saboresdecolombia.core.domain.model.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeTranslator @Inject constructor(
    private val translationApi: TranslationApiService,
    private val translationCacheDao: TranslationCacheDao
) {

    private val rateLimiter = Semaphore(permits = 15)

    private data class TranslateResult(
        val translatedText: String?,
        val sourceLanguage: String?
    )

    private suspend fun translateInternal(text: String): TranslateResult {
        if (text.isBlank()) return TranslateResult(null, null)

        translationCacheDao.getCachedTranslation(text)?.let {
            return TranslateResult(it, null)
        }

        return rateLimiter.withPermit {
            try {
                delay(80)
                val response = translationApi.translate(q = text)
                val json = response.string()
                val result = parseTranslationResponse(json)
                val translated = result?.translatedText

                if (translated != null && translated.isNotBlank() && translated != text) {
                    translationCacheDao.insertTranslation(
                        TranslationCacheEntity(originalText = text, translatedText = translated)
                    )
                }

                TranslateResult(translated, result?.sourceLanguage)
            } catch (e: Exception) {
                Log.e("RecipeTranslator", "Translation error: ${e.message}")
                TranslateResult(null, null)
            }
        }
    }

    private fun parseTranslationResponse(json: String): TranslateResult? {
        return try {
            val listType = object : TypeToken<List<Any>>() {}.type
            val parsed: List<Any> = Gson().fromJson(json, listType)

            val sentences = parsed.getOrNull(0) as? List<*> ?: return null
            val translated = sentences.mapNotNull { sentence ->
                val parts = sentence as? List<*>
                parts?.getOrNull(0) as? String
            }.joinToString("")

            val sourceLang = parsed.getOrNull(2) as? String

            TranslateResult(
                translatedText = translated.ifBlank { null },
                sourceLanguage = sourceLang
            )
        } catch (e: Exception) {
            Log.e("RecipeTranslator", "Parse error", e)
            null
        }
    }

    private suspend fun translateSingle(text: String): String? {
        val result = translateInternal(text)
        val translated = result.translatedText
        val sourceLang = result.sourceLanguage

        if (sourceLang == "es") {
            if (translated != text) {
                translationCacheDao.insertTranslation(
                    TranslationCacheEntity(originalText = text, translatedText = text)
                )
            }
            return text
        }

        return translated
    }

    private suspend fun translate(text: String): String? {
        val maxChunk = 450
        if (text.length <= maxChunk) {
            return translateSingle(text)
        }
        val chunks = text.chunked(maxChunk)
        val results = mutableListOf<String>()
        for (chunk in chunks) {
            val t = translateSingle(chunk) ?: chunk
            results.add(t)
        }
        return results.joinToString("")
    }

    suspend fun translateRecipe(recipe: Recipe): Recipe {
        return coroutineScope {
            val translatedName = async { translate(recipe.name) }
            val translatedCategory = async { translate(recipe.category) }
            val translatedArea = async { translate(recipe.area) }
            val translatedInstructions = async { translate(recipe.instructions) }
            val translatedTags = async { translate(recipe.tags) }
            val translatedIngredients = recipe.ingredients.map { ing ->
                async {
                    val tName = translate(ing.name)
                    val tMeasure = translate(ing.measure)
                    Ingredient(
                        name = tName ?: ing.name,
                        measure = tMeasure ?: ing.measure
                    )
                }
            }

            recipe.copy(
                name = translatedName.await() ?: recipe.name,
                category = translatedCategory.await() ?: recipe.category,
                area = translatedArea.await() ?: recipe.area,
                instructions = translatedInstructions.await() ?: recipe.instructions,
                tags = translatedTags.await() ?: recipe.tags,
                ingredients = translatedIngredients.map { it.await() }
            )
        }
    }
}
