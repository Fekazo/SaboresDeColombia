package com.lab.saboresdecolombia.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_recipes")
data class CachedRecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnail: String,
    val tags: String,
    val youtubeUrl: String,
    val ingredientsJson: String
)
