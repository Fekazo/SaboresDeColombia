package com.previo.p2.domain.repository

import com.previo.p2.domain.model.Nutrition
import com.previo.p2.domain.util.Result

interface NutritionRepository {
    suspend fun getNutrition(query: String): Result<Nutrition>
}