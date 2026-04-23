package com.previo.p2.data.repository

import com.previo.p2.data.local.dao.MealDao
import com.previo.p2.data.local.dao.MealSummaryDao
import com.previo.p2.data.mapper.toCacheEntity
import com.previo.p2.data.mapper.toDomain
import com.previo.p2.data.remote.api.MealDbService
import com.previo.p2.domain.model.Meal
import com.previo.p2.domain.model.MealSummary
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.repository.TranslationRepository
import com.previo.p2.domain.util.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val mealDbService: MealDbService,
    private val mealDao: MealDao,
    private val mealSummaryDao: MealSummaryDao,
    private val translationRepository: TranslationRepository
) : MealRepository {

    companion object {
        private const val CACHE_TTL_MS = 30 * 60 * 1000L
        private const val POPULAR_CACHE_KEY = "popular"
        private const val ALL_MEALS_CACHE_KEY = "all_meals"
    }

    override suspend fun getPopularMeals(): Result<List<MealSummary>> {
        return try {
            val cachedAt = mealSummaryDao.getCachedAtByKey(POPULAR_CACHE_KEY)
            val isValid = cachedAt != null && System.currentTimeMillis() - cachedAt < CACHE_TTL_MS
            if (isValid) {
                return Result.Success(
                    mealSummaryDao.getByKey(POPULAR_CACHE_KEY).map { it.toDomain() }.take(5)
                )
            }
            val summaries = fetchAndTranslateRandomMeals(POPULAR_CACHE_KEY, fetchCount = 20).take(5)
            mealSummaryDao.deleteByKey(POPULAR_CACHE_KEY)
            mealSummaryDao.insertAll(summaries.map { it.toCacheEntity(POPULAR_CACHE_KEY) })
            Result.Success(summaries)
        } catch (e: Exception) {
            val cached = mealSummaryDao.getByKey(POPULAR_CACHE_KEY).map { it.toDomain() }.take(5)
            if (cached.isNotEmpty()) Result.Success(cached) else Result.Error(e, e.message)
        }
    }

    override suspend fun searchMeals(query: String): Result<List<MealSummary>> {
        return try {
            val localResults = mealSummaryDao.searchByName(query).map { it.toDomain() }
            if (localResults.isNotEmpty()) return Result.Success(localResults)
            val response = mealDbService.searchMeals(query)
            val meals = response.meals?.map { it.toDomain() } ?: emptyList()
            if (meals.isEmpty()) return Result.Success(emptyList())
            Result.Success(translateSummariesInBatches(meals))
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
    }

    override suspend fun getMealsByArea(area: String): Result<List<MealSummary>> {
        val cacheKey = "area_$area"
        return try {
            val cachedAt = mealSummaryDao.getCachedAtByKey(cacheKey)
            val isValid = cachedAt != null && System.currentTimeMillis() - cachedAt < CACHE_TTL_MS
            if (isValid) {
                return Result.Success(mealSummaryDao.getByKey(cacheKey).map { it.toDomain() })
            }
            val summaries = fetchAndTranslateRandomMeals(cacheKey, fetchCount = 15)
            mealSummaryDao.deleteByKey(cacheKey)
            mealSummaryDao.insertAll(summaries.map { it.toCacheEntity(cacheKey) })
            Result.Success(summaries)
        } catch (e: Exception) {
            val cached = mealSummaryDao.getByKey(cacheKey).map { it.toDomain() }
            if (cached.isNotEmpty()) Result.Success(cached) else Result.Error(e, e.message)
        }
    }

    override suspend fun getMealDetail(id: String): Result<Meal> {
        return try {
            val cached = mealDao.getMealById(id)
            if (cached != null) {
                val isValid = System.currentTimeMillis() - cached.cachedAt < CACHE_TTL_MS
                if (isValid) return Result.Success(cached.toDomain())
            }
            val response = mealDbService.getMealById(id)
            val meal = response.meals?.firstOrNull()?.toDomain()
                ?: return Result.Error(Exception("Receta no encontrada"))
            val translatedMeal = translateMealDetail(meal)
            mealDao.insertMeal(translatedMeal.toCacheEntity())
            Result.Success(translatedMeal)
        } catch (e: Exception) {
            val cached = mealDao.getMealById(id)
            if (cached != null) Result.Success(cached.toDomain()) else Result.Error(e, e.message)
        }
    }

    override suspend fun getAllColombianMeals(): Result<List<MealSummary>> {
        return try {
            val cached = mealSummaryDao.getAllByKey(ALL_MEALS_CACHE_KEY).map { it.toDomain() }
            if (cached.isNotEmpty()) return Result.Success(cached)
            Result.Success(emptyList())
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
    }

    override suspend fun preloadAllMeals(): Result<List<MealSummary>> {
        return try {
            val cachedAt = mealSummaryDao.getCachedAtByKey(ALL_MEALS_CACHE_KEY)
            val isValid = cachedAt != null && System.currentTimeMillis() - cachedAt < CACHE_TTL_MS
            if (isValid) {
                val cached = mealSummaryDao.getAllByKey(ALL_MEALS_CACHE_KEY).map { it.toDomain() }
                return Result.Success(cached)
            }
            val allMeals = fetchAllMealsByAlphabet()
            if (allMeals.isEmpty()) return Result.Success(emptyList())
            val translated = translateSummariesInBatches(allMeals)
            mealSummaryDao.deleteByKey(ALL_MEALS_CACHE_KEY)
            mealSummaryDao.insertAll(translated.map { it.toCacheEntity(ALL_MEALS_CACHE_KEY) })
            Result.Success(translated)
        } catch (e: Exception) {
            val cached = mealSummaryDao.getAllByKey(ALL_MEALS_CACHE_KEY).map { it.toDomain() }
            if (cached.isNotEmpty()) Result.Success(cached) else Result.Error(e, e.message)
        }
    }

    override fun getPopularMealsFlow(): Flow<List<MealSummary>> {
        return mealSummaryDao.getByKeyFlow(POPULAR_CACHE_KEY).map { list ->
            list.map { it.toDomain() }
        }
    }

    private suspend fun fetchAllMealsByAlphabet(): List<MealSummary> = coroutineScope {
        ('a'..'z').map { letter ->
            async {
                try {
                    val response = mealDbService.getAllMeals(letter.toString())
                    response.meals?.map { it.toDomain() } ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }.flatMap { it.await() }.distinctBy { it.idMeal }
    }

    private suspend fun translateMealDetail(meal: Meal): Meal = coroutineScope {
        val nameDeferred = async { translationRepository.translate(meal.strMeal) }
        val areaDeferred = async { translationRepository.translate(meal.strArea) }
        val instructionsDeferred = async { translationRepository.translate(meal.strInstructions) }
        val ingredientsDeferred = meal.ingredients.map { ingredient ->
            async {
                val result = translationRepository.translate(ingredient.name)
                ingredient.copy(
                    translatedName = (result as? Result.Success)?.data ?: ingredient.name
                )
            }
        }
        meal.copy(
            translatedName = (nameDeferred.await() as? Result.Success)?.data,
            translatedArea = (areaDeferred.await() as? Result.Success)?.data,
            translatedInstructions = (instructionsDeferred.await() as? Result.Success)?.data,
            ingredients = ingredientsDeferred.map { it.await() }
        )
    }

    private suspend fun fetchAndTranslateRandomMeals(
        cacheKey: String,
        fetchCount: Int
    ): List<MealSummary> = coroutineScope {
        val fetched = (1..fetchCount).map {
            async {
                try {
                    val response = mealDbService.getRandomMeal()
                    val meal = response.meals?.firstOrNull() ?: return@async null
                    MealSummary(idMeal = meal.idMeal, strMeal = meal.strMeal, strMealThumb = meal.strMealThumb)
                } catch (e: Exception) { null }
            }
        }.mapNotNull { it.await() }.distinctBy { it.idMeal }
        translateSummariesInBatches(fetched)
    }

    private suspend fun translateSummariesInBatches(meals: List<MealSummary>): List<MealSummary> {
        val names = meals.map { it.strMeal }
        val translatedNames = translationRepository.translateBatch(names)
        return meals.mapIndexed { index, meal ->
            meal.copy(translatedName = translatedNames.getOrElse(index) { meal.strMeal })
        }
    }
}