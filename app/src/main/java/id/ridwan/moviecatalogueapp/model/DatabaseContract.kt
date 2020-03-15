package id.ridwan.moviecatalogueapp.model

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "id.ridwan.moviecatalogueapp"
    const val SCHEME = "content"

    class FavoriteMovieColumns : BaseColumns {

        companion object {
            const val TABLE_FAVORITE_MOVIE = "favorite_movie"
            const val _ID = "id"
            const val ID_MOVIE = "id_movie"
            const val POSTER_MOVIE = "poster_movie"
            const val TITLE_MOVIE = "title_movie"
            const val DATE_MOVIE = "date_movie"
            const val RATING_MOVIE = "rating_movie"

            val CONTENT_URI_MOVIE: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_MOVIE)
                .build()
        }
    }

    class FavoriteTVShowColumns : BaseColumns {

        companion object {
            const val TABLE_FAVORITE_TVSHOW = "favorite_tvshow"
            const val _ID = "id"
            const val ID_TVSHOW = "id_tvshow"
            const val POSTER_TVSHOW = "poster_tvshow"
            const val TITLE_TVSHOW = "title_tvshow"
            const val DATE_TVSHOW = "date_tvshow"
            const val RATING_TVSHOW = "rating_tvshow"

            val CONTENT_URI_TVSHOW: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_TVSHOW)
                .build()
        }
    }
}