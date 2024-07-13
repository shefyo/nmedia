package ru.netology.nmedia.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE PostEntity ADD COLUMN countlikes INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE PostEntity ADD COLUMN countreposts INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE PostEntity ADD COLUMN countviews INTEGER NOT NULL DEFAULT 0")
    }
}