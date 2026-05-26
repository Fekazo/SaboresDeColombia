package com.lab.saboresdecolombia.core.domain.repository

import com.lab.saboresdecolombia.core.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(): Flow<List<Recipe>>
    suspend fun toggleFavorite(recipe: Recipe)
    suspend fun deleteFavorite(recipeId: String)
    fun isFavorite(recipeId: String): Flow<Boolean>
}
