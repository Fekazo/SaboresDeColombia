package com.lab.saboresdecolombia.di

import android.content.Context
import androidx.room.Room
import com.lab.saboresdecolombia.core.data.local.AppDatabase
import com.lab.saboresdecolombia.core.data.local.dao.CachedRecipeDao
import com.lab.saboresdecolombia.core.data.local.dao.RecipeFavoriteDao
import com.lab.saboresdecolombia.core.data.local.dao.TranslationCacheDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sabores_de_colombia.db"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    @Singleton
    fun provideRecipeFavoriteDao(database: AppDatabase): RecipeFavoriteDao {
        return database.recipeFavoriteDao()
    }

    @Provides
    @Singleton
    fun provideTranslationCacheDao(database: AppDatabase): TranslationCacheDao {
        return database.translationCacheDao()
    }

    @Provides
    @Singleton
    fun provideCachedRecipeDao(database: AppDatabase): CachedRecipeDao {
        return database.cachedRecipeDao()
    }
}
