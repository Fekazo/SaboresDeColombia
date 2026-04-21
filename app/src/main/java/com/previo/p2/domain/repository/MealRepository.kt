package com.previo.p2.domain.repository

import com.previo.p2.domain.model.Meal
import com.previo.p2.domain.model.MealSummary
import com.previo.p2.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    suspend fun getPopularMeals(): Result<List<MealSummary>>
    suspend fun searchMeals(query: String): Result<List<MealSummary>>
    suspend fun getMealsByArea(area: String): Result<List<MealSummary>>
    suspend fun getMealDetail(id: String): Result<Meal>
    fun getPopularMealsFlow(): Flow<List<MealSummary>>
}