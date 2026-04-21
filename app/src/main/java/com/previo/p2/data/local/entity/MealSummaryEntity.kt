package com.previo.p2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_summary_cache")
data class MealSummaryCacheEntity(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val cacheKey: String,
    val cachedAt: Long = System.currentTimeMillis()
)