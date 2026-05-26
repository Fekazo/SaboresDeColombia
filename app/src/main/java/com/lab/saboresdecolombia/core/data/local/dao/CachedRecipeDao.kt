package com.lab.saboresdecolombia.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lab.saboresdecolombia.core.data.local.entity.CachedRecipeEntity

@Dao
interface CachedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<CachedRecipeEntity>)

    @Query("SELECT * FROM cached_recipes")
    suspend fun getAllRecipes(): List<CachedRecipeEntity>

    @Query("SELECT * FROM cached_recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): CachedRecipeEntity?

    @Query("SELECT * FROM cached_recipes ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomRecipes(count: Int): List<CachedRecipeEntity>

    @Query("SELECT * FROM cached_recipes WHERE area = :area")
    suspend fun getRecipesByArea(area: String): List<CachedRecipeEntity>

    @Query("SELECT COUNT(*) FROM cached_recipes")
    suspend fun getCount(): Int

    @Query("SELECT * FROM cached_recipes")
    fun getAllRecipesFlow(): kotlinx.coroutines.flow.Flow<List<CachedRecipeEntity>>
}
