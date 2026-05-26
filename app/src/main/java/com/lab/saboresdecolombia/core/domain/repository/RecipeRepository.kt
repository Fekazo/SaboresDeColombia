package com.lab.saboresdecolombia.core.domain.repository

import com.lab.saboresdecolombia.core.domain.model.Recipe

interface RecipeRepository {
    suspend fun getPopularRecipes(): Result<List<Recipe>>
    suspend fun getRecipesByRegion(region: String): Result<List<Recipe>>
    suspend fun searchRecipes(query: String): Result<List<Recipe>>
    suspend fun getRecipeDetail(id: String): Result<Recipe>
    suspend fun getAllRecipes(): Result<List<Recipe>>
}
