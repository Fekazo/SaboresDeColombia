package com.previo.p2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.previo.p2.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE idMeal = :idMeal")
    suspend fun deleteFavorite(idMeal: String)

    @Query("SELECT * FROM favorites ORDER BY savedAt DESC")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT COUNT(*) FROM favorites WHERE idMeal = :idMeal")
    suspend fun isFavorite(idMeal: String): Int
}