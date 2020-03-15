package id.ridwan.moviecatalogueapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import id.ridwan.moviecatalogueapp.model.DatabaseContract.AUTHORITY
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteMovieColumns.Companion.CONTENT_URI_MOVIE
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteMovieColumns.Companion.TABLE_FAVORITE_MOVIE
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.CONTENT_URI_TVSHOW
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.TABLE_FAVORITE_TVSHOW
import id.ridwan.moviecatalogueapp.model.favoriteHelper.FavoriteMovieHelper
import id.ridwan.moviecatalogueapp.model.favoriteHelper.FavoriteTVShowHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_MOVIE = 1
        private const val FAVORITE_TVSHOW = 2

        private const val FAVORITE_MOVIE_ID = 11
        private const val FAVORITE_TVSHOW_ID = 12

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private lateinit var favoriteMovieHelper: FavoriteMovieHelper
        private lateinit var favoriteTVShowHelper: FavoriteTVShowHelper
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE, FAVORITE_MOVIE)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_FAVORITE_MOVIE/#", FAVORITE_MOVIE_ID)

        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_TVSHOW, FAVORITE_TVSHOW)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_FAVORITE_TVSHOW/#", FAVORITE_TVSHOW_ID)
    }

    override fun onCreate(): Boolean {
        favoriteMovieHelper = FavoriteMovieHelper.getInstance(context as Context)
        favoriteTVShowHelper = FavoriteTVShowHelper.getInstance(context as Context)
        favoriteMovieHelper.open()
        favoriteTVShowHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        favoriteMovieHelper.open()
        favoriteTVShowHelper.open()
        return when (sUriMatcher.match(uri)) {
            FAVORITE_MOVIE -> favoriteMovieHelper.getAllFavoriteMovie()
            FAVORITE_MOVIE_ID -> favoriteMovieHelper.getFavoriteMovieById(uri.lastPathSegment.toString())
            FAVORITE_TVSHOW -> favoriteTVShowHelper.getAllFavoriteTVShow()
            FAVORITE_TVSHOW_ID -> favoriteTVShowHelper.getFavoriteTVShowById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        favoriteMovieHelper.open()
        favoriteTVShowHelper.open()
        val added: Long
        if (sUriMatcher.match(uri) == FAVORITE_MOVIE) {
            added = values.let { favoriteMovieHelper.insert(it) }
            return Uri.parse("$CONTENT_URI_MOVIE/$added")
        } else if (sUriMatcher.match(uri) == FAVORITE_TVSHOW) {
            added = values.let { favoriteTVShowHelper.insert(it) }
            context?.contentResolver?.notifyChange(CONTENT_URI_TVSHOW, null)
            return Uri.parse("$CONTENT_URI_TVSHOW/$added")
        }
        return Uri.parse("$CONTENT_URI_MOVIE/0")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        favoriteMovieHelper.open()
        favoriteTVShowHelper.open()
        val updated: Int = when (sUriMatcher.match(uri)) {
            FAVORITE_MOVIE_ID -> favoriteMovieHelper.update(uri.lastPathSegment.toString(), values)
            FAVORITE_TVSHOW_ID -> favoriteTVShowHelper.update(
                uri.lastPathSegment.toString(),
                values
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        favoriteMovieHelper.open()
        favoriteTVShowHelper.open()
        val deleted: Int
        if (sUriMatcher.match(uri) == FAVORITE_MOVIE_ID) {
            deleted = favoriteMovieHelper.deleteById(uri.lastPathSegment)
            context?.contentResolver?.notifyChange(CONTENT_URI_MOVIE, null)
            return deleted
        } else if (sUriMatcher.match(uri) == FAVORITE_TVSHOW_ID) {
            deleted = favoriteTVShowHelper.deleteById(uri.lastPathSegment)
            context?.contentResolver?.notifyChange(CONTENT_URI_TVSHOW, null)
            return deleted
        }
        return 0
    }
}