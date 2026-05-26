package com.lab.saboresdecolombia.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class RecipeFavoriteEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val area: String,
    val thumbnail: String,
    val instructions: String,
    val ingredients: String,
    val tags: String,
    val timestamp: Long = System.currentTimeMillis()
)
