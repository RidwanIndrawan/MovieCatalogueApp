package id.ridwan.favoritedb.configAPI

import id.ridwan.favoritedb.model.detail.ModelDetailDataMovie
import id.ridwan.favoritedb.model.detail.ModelDetailDataTVShow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QueryAPI {


    @GET("3/movie/{id}")
    fun getMovieDetail(
        @Path("id") id: Int?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<ModelDetailDataMovie>

    @GET("3/tv/{id}")
    fun getTVShowsDetail(
        @Path("id") id: Int?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<ModelDetailDataTVShow>


}