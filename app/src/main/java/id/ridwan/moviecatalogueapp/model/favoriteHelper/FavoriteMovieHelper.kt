package id.ridwan.moviecatalogueapp.model.favoriteHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteMovieColumns.Companion.ID_MOVIE
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteMovieColumns.Companion.TABLE_FAVORITE_MOVIE
import java.sql.SQLException

class FavoriteMovieHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_FAVORITE_MOVIE
        private lateinit var favoriteHelper: FavoriteHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: FavoriteMovieHelper? = null
        fun getInstance(context: Context): FavoriteMovieHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = FavoriteMovieHelper(context)
                    }
                }
            }
            return INSTANCE as FavoriteMovieHelper
        }
    }

    init {
        favoriteHelper = FavoriteHelper(context)
    }


    @Throws(SQLException::class)
    fun open() {
        database = favoriteHelper.writableDatabase
    }

    fun close() {
        favoriteHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun getAllFavoriteMovie(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID_MOVIE ASC"
        )
    }

    fun getFavoriteMovieById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID_MOVIE = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "${BaseColumns._ID} = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$ID_MOVIE = '$id'", null)
    }

    fun isFavorite(idMovie: Int?): Boolean {
        val query = "SELECT * FROM $TABLE_FAVORITE_MOVIE WHERE $ID_MOVIE = $idMovie"
        open()
        val cursor = database.rawQuery(query, null)
        if (cursor.count > 0) {
            cursor.close()
            return true
        }
        cursor.close()
        return false
    }
}