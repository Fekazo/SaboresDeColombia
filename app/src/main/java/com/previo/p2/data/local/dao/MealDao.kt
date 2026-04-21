package com.previo.p2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.previo.p2.data.local.entity.MealCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealCacheEntity)

    @Query("SELECT * FROM meal_cache WHERE idMeal = :id")
    suspend fun getMealById(id: String): MealCacheEntity?

    @Query("DELETE FROM meal_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpiredMeals(expiryTime: Long)

    @Query("SELECT * FROM meal_cache")
    fun getAllMeals(): Flow<List<MealCacheEntity>>
}