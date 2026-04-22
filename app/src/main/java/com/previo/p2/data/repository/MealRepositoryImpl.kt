package com.previo.p2.data.repository

import com.previo.p2.data.local.dao.MealDao
import com.previo.p2.data.local.dao.MealSummaryDao
import com.previo.p2.data.mapper.toCacheEntity
import com.previo.p2.data.mapper.toDomain
import com.previo.p2.data.remote.api.MealDbService
import com.previo.p2.domain.model.Meal
import com.previo.p2.domain.model.MealSummary
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val mealDbService: MealDbService,
    private val mealDao: MealDao,
    private val mealSummaryDao: MealSummaryDao
) : MealRepository {

    companion object {
        private const val CACHE_TTL_MS = 30 * 60 * 1000L
        private const val POPULAR_CACHE_KEY = "popular"
    }

    override suspend fun getPopularMeals(): Result<List<MealSummary>> {
        return try {
            val cachedAt = mealSummaryDao.getCachedAtByKey(POPULAR_CACHE_KEY)
            val isValid = cachedAt != null && System.currentTimeMillis() - cachedAt < CACHE_TTL_MS
            if (isValid) {
                val cached = mealSummaryDao.getByKey(POPULAR_CACHE_KEY).map { it.toDomain() }
                return Result.Success(cached)
            }
            val response = mealDbService.getRandomMeal()
            val meals = response.meals?.map { it.toDomain() } ?: emptyList()
            val summaries = meals.map { meal ->
                MealSummary(meal.idMeal, meal.strMeal, meal.strMealThumb)
            }
            mealSummaryDao.deleteByKey(POPULAR_CACHE_KEY)
            mealSummaryDao.insertAll(summaries.map { it.toCacheEntity(POPULAR_CACHE_KEY) })
            Result.Success(summaries)
        } catch (e: Exception) {
            val cached = mealSummaryDao.getByKey(POPULAR_CACHE_KEY).map { it.toDomain() }
            if (cached.isNotEmpty()) Result.Success(cached)
            else Result.Error(e, e.message)
        }
    }

    override suspend fun searchMeals(query: String): Result<List<MealSummary>> {
        return try {
            val response = mealDbService.searchMeals(query)
            val meals = response.meals?.map { it.toDomain() } ?: emptyList()
            Result.Success(meals)
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
                val cached = mealSummaryDao.getByKey(cacheKey).map { it.toDomain() }
                return Result.Success(cached)
            }
            val response = mealDbService.getMealsByArea(area)
            val meals = response.meals?.map { it.toDomain() } ?: emptyList()
            mealSummaryDao.deleteByKey(cacheKey)
            mealSummaryDao.insertAll(meals.map { it.toCacheEntity(cacheKey) })
            Result.Success(meals)
        } catch (e: Exception) {
            val cached = mealSummaryDao.getByKey(cacheKey).map { it.toDomain() }
            if (cached.isNotEmpty()) Result.Success(cached)
            else Result.Error(e, e.message)
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
            mealDao.insertMeal(meal.toCacheEntity())
            Result.Success(meal)
        } catch (e: Exception) {
            val cached = mealDao.getMealById(id)
            if (cached != null) Result.Success(cached.toDomain())
            else Result.Error(e, e.message)
        }
    }

    override fun getPopularMealsFlow(): Flow<List<MealSummary>> {
        return mealSummaryDao.getByKeyFlow(POPULAR_CACHE_KEY).map { list ->
            list.map { it.toDomain() }
        }
    }
}