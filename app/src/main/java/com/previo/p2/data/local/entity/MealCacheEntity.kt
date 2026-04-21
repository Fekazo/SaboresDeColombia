package com.previo.p2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_cache")
data class MealCacheEntity(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strArea: String,
    val strMealThumb: String,
    val strInstructions: String,
    val ingredientsJson: String,
    val strYoutube: String?,
    val strTags: String?,
    val cachedAt: Long = System.currentTimeMillis()
)