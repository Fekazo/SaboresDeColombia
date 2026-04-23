package com.previo.p2.di

import android.content.Context
import androidx.room.Room
import com.previo.p2.data.local.DatabaseMigrations.MIGRATION_1_2
import com.previo.p2.data.local.DatabaseMigrations.MIGRATION_2_3
import com.previo.p2.data.local.DatabaseMigrations.MIGRATION_3_4
import com.previo.p2.data.local.DatabaseMigrations.MIGRATION_4_5
import com.previo.p2.data.local.SaboresDatabase
import com.previo.p2.data.local.dao.FavoriteDao
import com.previo.p2.data.local.dao.MealDao
import com.previo.p2.data.local.dao.MealSummaryDao
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
    fun provideDatabase(@ApplicationContext context: Context): SaboresDatabase {
        return Room.databaseBuilder(
            context,
            SaboresDatabase::class.java,
            "sabores_db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
            .build()
    }

    @Provides
    fun provideMealDao(db: SaboresDatabase): MealDao = db.mealDao()

    @Provides
    fun provideMealSummaryDao(db: SaboresDatabase): MealSummaryDao = db.mealSummaryDao()

    @Provides
    fun provideFavoriteDao(db: SaboresDatabase): FavoriteDao = db.favoriteDao()
}