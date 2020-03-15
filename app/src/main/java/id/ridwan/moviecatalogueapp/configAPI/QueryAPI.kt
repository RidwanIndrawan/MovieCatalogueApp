package id.ridwan.moviecatalogueapp.configAPI

import id.ridwan.moviecatalogueapp.model.movie.ModelDetailDataMovie
import id.ridwan.moviecatalogueapp.model.movie.ModelListMovie
import id.ridwan.moviecatalogueapp.model.tvshow.ModelDetailDataTVShow
import id.ridwan.moviecatalogueapp.model.tvshow.ModelListTVShow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QueryAPI {

    @GET("3/discover/movie")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<ModelListMovie>

    @GET("3/movie/{id}")
    fun getMovieDetail(
        @Path("id") id: Int?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<ModelDetailDataMovie>

    @GET("3/search/movie")
    fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String?
    ): Call<ModelListMovie>

    @GET("3/discover/movie")
    fun getTodayReleasedMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("primary_release_date.gte") releaseDateGte: String,
        @Query("primary_release_date.ite") releaseDateIte: String
    ): Call<ModelListMovie>


    @GET("3/discover/tv")
    fun getTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<ModelListTVShow>

    @GET("3/tv/{id}")
    fun getTVShowsDetail(
        @Path("id") id: Int?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<ModelDetailDataTVShow>

    @GET("3/search/tv")
    fun searchTVShow(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String?
    ): Call<ModelListTVShow>
}