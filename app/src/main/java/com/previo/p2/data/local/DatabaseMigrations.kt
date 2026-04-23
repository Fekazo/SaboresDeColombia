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

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE meal_summary_cache ADD COLUMN translatedName TEXT")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS meal_summary_cache")
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS meal_summary_cache (
                    idMeal TEXT NOT NULL,
                    strMeal TEXT NOT NULL,
                    strMealThumb TEXT NOT NULL,
                    cacheKey TEXT NOT NULL,
                    translatedName TEXT,
                    cachedAt INTEGER NOT NULL,
                    PRIMARY KEY(idMeal, cacheKey)
                )
                """.trimIndent()
            )
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE meal_cache ADD COLUMN translatedName TEXT")
            db.execSQL("ALTER TABLE meal_cache ADD COLUMN translatedArea TEXT")
            db.execSQL("ALTER TABLE meal_cache ADD COLUMN translatedInstructions TEXT")
            db.execSQL("ALTER TABLE favorites ADD COLUMN translatedName TEXT")
        }
    }
}