package com.previo.p2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strArea: String,
    val savedAt: Long = System.currentTimeMillis()
)