package com.previo.p2.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS meal_summary_cache (
                    idMeal TEXT NOT NULL PRIMARY KEY,
                    strMeal TEXT NOT NULL,
                    strMealThumb TEXT NOT NULL,
                    cacheKey TEXT NOT NULL,
                    cachedAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }
}