package ru.netology.nmedia.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.PostEntity

@Database(entities = [PostEntity::class], version = 2)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE PostEntity ADD COLUMN countlikes INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE PostEntity ADD COLUMN countreposts INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE PostEntity ADD COLUMN countviews INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE PostEntity DROP COLUMN likes")
                database.execSQL("ALTER TABLE PostEntity DROP COLUMN reposts")
                database.execSQL("ALTER TABLE PostEntity DROP COLUMN views")
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDb::class.java,
                "app.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}
