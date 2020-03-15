package id.ridwan.moviecatalogueapp.model.favoriteHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteMovieColumns
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteMovieColumns.Companion.TABLE_FAVORITE_MOVIE
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.TABLE_FAVORITE_TVSHOW


internal class FavoriteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbFavorite"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORITE_MOVIE = "CREATE TABLE $TABLE_FAVORITE_MOVIE" +
                "(${FavoriteMovieColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${FavoriteMovieColumns.ID_MOVIE} TEXT NOT NULL, " +
                "${FavoriteMovieColumns.POSTER_MOVIE} TEXT, " +
                "${FavoriteMovieColumns.TITLE_MOVIE} TEXT, " +
                "${FavoriteMovieColumns.DATE_MOVIE} TEXT, " +
                "${FavoriteMovieColumns.RATING_MOVIE} TEXT) "

        private val SQL_CREATE_TABLE_FAVORITE_TVSHOW = "CREATE TABLE $TABLE_FAVORITE_TVSHOW" +
                "(${FavoriteTVShowColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${FavoriteTVShowColumns.ID_TVSHOW} TEXT NOT NULL, " +
                "${FavoriteTVShowColumns.POSTER_TVSHOW} TEXT, " +
                "${FavoriteTVShowColumns.TITLE_TVSHOW} TEXT, " +
                "${FavoriteTVShowColumns.DATE_TVSHOW} TEXT, " +
                "${FavoriteTVShowColumns.RATING_TVSHOW} TEXT)"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIE)
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_TVSHOW)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE_TVSHOW")
        onCreate(db)
    }

}