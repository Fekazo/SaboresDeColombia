package com.previo.p2.data.repository

import com.previo.p2.data.local.dao.FavoriteDao
import com.previo.p2.data.mapper.toDomain
import com.previo.p2.data.mapper.toEntity
import com.previo.p2.domain.model.Favorite
import com.previo.p2.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<Favorite>> {
        return favoriteDao.getFavorites().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addFavorite(favorite: Favorite) {
        favoriteDao.insertFavorite(favorite.toEntity())
    }

    override suspend fun removeFavorite(idMeal: String) {
        favoriteDao.deleteFavorite(idMeal)
    }

    override suspend fun isFavorite(idMeal: String): Boolean {
        return favoriteDao.isFavorite(idMeal) > 0
    }
}