package com.previo.p2.data.remote.api

import com.previo.p2.data.remote.dto.EdamamResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamService {
    @GET("recipes/v2")
    suspend fun getNutrition(
        @Query("q") query: String,
        @Query("type") type: String = "public",
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): EdamamResponseDto
}