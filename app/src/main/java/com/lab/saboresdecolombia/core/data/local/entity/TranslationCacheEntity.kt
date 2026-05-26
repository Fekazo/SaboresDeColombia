package com.lab.saboresdecolombia.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_cache")
data class TranslationCacheEntity(
    @PrimaryKey val originalText: String,
    val translatedText: String
)
