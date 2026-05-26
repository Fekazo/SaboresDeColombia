package com.lab.saboresdecolombia.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lab.saboresdecolombia.core.data.local.entity.RecipeFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: RecipeFavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: RecipeFavoriteEntity)

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<RecipeFavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :recipeId)")
    fun isFavorite(recipeId: String): Flow<Boolean>
}
