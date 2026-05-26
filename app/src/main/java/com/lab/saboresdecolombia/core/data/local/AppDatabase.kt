package com.lab.saboresdecolombia.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lab.saboresdecolombia.core.data.local.dao.CachedRecipeDao
import com.lab.saboresdecolombia.core.data.local.dao.RecipeFavoriteDao
import com.lab.saboresdecolombia.core.data.local.dao.TranslationCacheDao
import com.lab.saboresdecolombia.core.data.local.entity.CachedRecipeEntity
import com.lab.saboresdecolombia.core.data.local.entity.RecipeFavoriteEntity
import com.lab.saboresdecolombia.core.data.local.entity.TranslationCacheEntity

@Database(
    entities = [RecipeFavoriteEntity::class, TranslationCacheEntity::class, CachedRecipeEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeFavoriteDao(): RecipeFavoriteDao
    abstract fun translationCacheDao(): TranslationCacheDao
    abstract fun cachedRecipeDao(): CachedRecipeDao
}
