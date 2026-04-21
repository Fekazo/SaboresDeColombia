package com.previo.p2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.previo.p2.data.local.dao.FavoriteDao
import com.previo.p2.data.local.dao.MealDao
import com.previo.p2.data.local.dao.MealSummaryDao
import com.previo.p2.data.local.entity.FavoriteEntity
import com.previo.p2.data.local.entity.MealCacheEntity
import com.previo.p2.data.local.entity.MealSummaryCacheEntity

@Database(
    entities = [MealCacheEntity::class, FavoriteEntity::class, MealSummaryCacheEntity::class],
    version = 2,
    exportSchema = true
)
abstract class SaboresDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun mealSummaryDao(): MealSummaryDao
}