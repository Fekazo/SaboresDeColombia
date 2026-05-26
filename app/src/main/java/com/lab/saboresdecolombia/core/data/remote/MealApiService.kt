package com.lab.saboresdecolombia.core.data.remote

import com.lab.saboresdecolombia.core.data.remote.dto.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealListResponse

    @GET("lookup.php")
    suspend fun lookupMeal(@Query("i") id: String): MealListResponse

    @GET("filter.php")
    suspend fun filterByArea(@Query("a") area: String): MealListResponse

    @GET("random.php")
    suspend fun randomMeal(): MealListResponse

    @GET("search.php")
    suspend fun filterByFirstLetter(@Query("f") letter: String): MealListResponse
}
