package com.previo.p2.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "meal_summary_cache",
    primaryKeys = ["idMeal", "cacheKey"]
)
data class MealSummaryCacheEntity(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val cacheKey: String,
    val translatedName: String? = null,
    val cachedAt: Long = System.currentTimeMillis()
)