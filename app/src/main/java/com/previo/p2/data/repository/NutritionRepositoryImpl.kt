package com.previo.p2.data.repository

import com.previo.p2.BuildConfig
import com.previo.p2.data.mapper.toDomain
import com.previo.p2.data.remote.api.EdamamService
import com.previo.p2.domain.model.Nutrition
import com.previo.p2.domain.repository.NutritionRepository
import com.previo.p2.domain.util.Result
import javax.inject.Inject

class NutritionRepositoryImpl @Inject constructor(
    private val edamamService: EdamamService
) : NutritionRepository {

    override suspend fun getNutrition(query: String): Result<Nutrition> {
        return try {
            val response = edamamService.getNutrition(
                query = query,
                appId = BuildConfig.EDAMAM_APP_ID,
                appKey = BuildConfig.EDAMAM_APP_KEY
            )
            val nutrition = response.toDomain()
                ?: return Result.Error(Exception("Sin datos nutricionales"))
            Result.Success(nutrition)
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
    }
}