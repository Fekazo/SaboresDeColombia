package com.previo.p2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.previo.p2.data.local.entity.MealSummaryCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealSummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meals: List<MealSummaryCacheEntity>)

    @Query("SELECT * FROM meal_summary_cache WHERE cacheKey = :key")
    suspend fun getByKey(key: String): List<MealSummaryCacheEntity>

    @Query("SELECT * FROM meal_summary_cache WHERE cacheKey = :key")
    fun getByKeyFlow(key: String): Flow<List<MealSummaryCacheEntity>>

    @Query("SELECT cachedAt FROM meal_summary_cache WHERE cacheKey = :key LIMIT 1")
    suspend fun getCachedAtByKey(key: String): Long?

    @Query("DELETE FROM meal_summary_cache WHERE cacheKey = :key")
    suspend fun deleteByKey(key: String)

    @Query("DELETE FROM meal_summary_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpired(expiryTime: Long)
}