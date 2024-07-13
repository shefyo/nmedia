package ru.netology.nmedia

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostDaoImpl.DDL))
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, DDls: Array<String>) = DbHelper(
            context, 5, "app.db", DDls,
        ).writableDatabase

    }

    class DbHelper(
        context: Context,
        dbVersion: Int,
        dbName: String,
        private val DDLs: Array<String>
    ) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

        override fun onCreate(db: SQLiteDatabase) {
            DDLs.forEach {
                db.execSQL(it)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (oldVersion < 2) {
                db.execSQL("ALTER TABLE ${PostDaoImpl.PostColumns.TABLE} ADD COLUMN ${PostDaoImpl.PostColumns.COLUMN_REPOSTS} INTEGER NOT NULL DEFAULT 0")
            }
            if (oldVersion < 3) {
                db.execSQL("ALTER TABLE ${PostDaoImpl.PostColumns.TABLE} ADD COLUMN ${PostDaoImpl.PostColumns.COLUMN_REPOSTED_BY_ME} BOOLEAN NOT NULL DEFAULT 0")
            }
            if (oldVersion < 4) {
                db.execSQL("ALTER TABLE ${PostDaoImpl.PostColumns.TABLE} ADD COLUMN ${PostDaoImpl.PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0")
            }
            if (oldVersion < 5) {
                db.execSQL("ALTER TABLE ${PostDaoImpl.PostColumns.TABLE} ADD COLUMN ${PostDaoImpl.PostColumns.COLUMN_VIDEO} TEXT")
            }
        }
    }
}
