package com.previo.p2.domain.repository

import com.previo.p2.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<Favorite>>
    suspend fun addFavorite(favorite: Favorite)
    suspend fun removeFavorite(idMeal: String)
    suspend fun isFavorite(idMeal: String): Boolean
}