package id.ridwan.moviecatalogueapp.helper

import android.database.Cursor
import id.ridwan.moviecatalogueapp.model.DatabaseContract
import id.ridwan.moviecatalogueapp.model.favorite.ModelDataFavoriteMovie
import id.ridwan.moviecatalogueapp.model.favorite.ModelDataFavoriteTVShow

object MappingHelper {
    fun mapFavoriteMovieCursorToArrayList(favMovieCursor: Cursor): ArrayList<ModelDataFavoriteMovie> {
        val favMovieList = ArrayList<ModelDataFavoriteMovie>()
        while (favMovieCursor.moveToNext()) {
            val idMovie = favMovieCursor.getInt(
                favMovieCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteMovieColumns.ID_MOVIE
                )
            )
            val posterMovie = favMovieCursor.getString(
                favMovieCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteMovieColumns.POSTER_MOVIE
                )
            )
            val titleMovie = favMovieCursor.getString(
                favMovieCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteMovieColumns.TITLE_MOVIE
                )
            )
            val ratingMovie = favMovieCursor.getFloat(
                favMovieCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteMovieColumns.RATING_MOVIE
                )
            )
            favMovieList.add(ModelDataFavoriteMovie(idMovie, posterMovie, titleMovie, ratingMovie))
        }
        return favMovieList
    }

    fun mapFavoriteTVShowCursorToArrayList(favTVShowCursor: Cursor): ArrayList<ModelDataFavoriteTVShow> {
        val favTVShowList = ArrayList<ModelDataFavoriteTVShow>()

        while (favTVShowCursor.moveToNext()) {
            val idTVShow = favTVShowCursor.getInt(
                favTVShowCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteTVShowColumns.ID_TVSHOW
                )
            )
            val posterTVShow = favTVShowCursor.getString(
                favTVShowCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteTVShowColumns.POSTER_TVSHOW
                )
            )
            val titleTVShow = favTVShowCursor.getString(
                favTVShowCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteTVShowColumns.TITLE_TVSHOW
                )
            )
            val ratingTVShow = favTVShowCursor.getFloat(
                favTVShowCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavoriteTVShowColumns.RATING_TVSHOW
                )
            )
            favTVShowList.add(
                ModelDataFavoriteTVShow(
                    idTVShow,
                    posterTVShow,
                    titleTVShow,
                    ratingTVShow
                )
            )
        }
        return favTVShowList
    }
}