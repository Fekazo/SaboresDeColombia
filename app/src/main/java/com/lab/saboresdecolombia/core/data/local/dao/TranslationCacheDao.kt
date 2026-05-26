package com.lab.saboresdecolombia.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lab.saboresdecolombia.core.data.local.entity.TranslationCacheEntity

@Dao
interface TranslationCacheDao {

    @Query("SELECT translatedText FROM translation_cache WHERE originalText = :text LIMIT 1")
    suspend fun getCachedTranslation(text: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(cache: TranslationCacheEntity)
}
