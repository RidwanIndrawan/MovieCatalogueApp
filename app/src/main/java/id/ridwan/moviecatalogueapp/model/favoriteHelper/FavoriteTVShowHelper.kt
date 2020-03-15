package id.ridwan.moviecatalogueapp.model.favoriteHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.ID_TVSHOW
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.TABLE_FAVORITE_TVSHOW
import java.sql.SQLException

class FavoriteTVShowHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_FAVORITE_TVSHOW
        private lateinit var favoriteHelper: FavoriteHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: FavoriteTVShowHelper? = null
        fun getInstance(context: Context): FavoriteTVShowHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = FavoriteTVShowHelper(context)
                    }
                }
            }
            return INSTANCE as FavoriteTVShowHelper
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

    fun getAllFavoriteTVShow(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ID_TVSHOW ASC"
        )
    }

    fun getFavoriteTVShowById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID_TVSHOW = ?",
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
        return database.delete(DATABASE_TABLE, "$ID_TVSHOW = '$id'", null)
    }

    fun isFavorite(idTVShow: Int?): Boolean {
        val query = "SELECT * FROM $TABLE_FAVORITE_TVSHOW WHERE $ID_TVSHOW = $idTVShow"
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