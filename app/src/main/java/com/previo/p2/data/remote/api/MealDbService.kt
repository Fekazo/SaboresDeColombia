package com.previo.p2.data.remote.api

import com.previo.p2.data.remote.dto.MealDetailResponseDto
import com.previo.p2.data.remote.dto.MealSummaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbService {
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealSummaryResponseDto

    @GET("search.php")
    suspend fun getAllMeals(@Query("s") query: String = ""): MealSummaryResponseDto

    @GET("filter.php")
    suspend fun getMealsByArea(@Query("a") area: String): MealSummaryResponseDto

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealDetailResponseDto

    @GET("random.php")
    suspend fun getRandomMeal(): MealDetailResponseDto
}