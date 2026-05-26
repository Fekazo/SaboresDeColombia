package com.lab.saboresdecolombia.core.data.repository

import com.lab.saboresdecolombia.core.data.local.dao.RecipeFavoriteDao
import com.lab.saboresdecolombia.core.data.mapper.RecipeMapper.toDomain
import com.lab.saboresdecolombia.core.data.mapper.RecipeMapper.toEntity
import com.lab.saboresdecolombia.core.domain.model.Recipe
import com.lab.saboresdecolombia.core.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val dao: RecipeFavoriteDao
) : FavoritesRepository {

    override fun getFavorites(): Flow<List<Recipe>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun toggleFavorite(recipe: Recipe) {
        if (recipe.isFavorite) {
            dao.deleteFavorite(recipe.toEntity())
        } else {
            dao.insertFavorite(recipe.toEntity())
        }
    }

    override suspend fun deleteFavorite(recipeId: String) {
        val entity = com.lab.saboresdecolombia.core.data.local.entity.RecipeFavoriteEntity(
            id = recipeId,
            name = "",
            category = "",
            area = "",
            thumbnail = "",
            instructions = "",
            ingredients = "",
            tags = ""
        )
        dao.deleteFavorite(entity)
    }

    override fun isFavorite(recipeId: String): Flow<Boolean> {
        return dao.isFavorite(recipeId)
    }
}
