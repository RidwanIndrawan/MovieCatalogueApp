package id.ridwan.favoritedb.configAPI

import id.ridwan.favoritedb.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Config {
    private fun doRequest(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun instance(): QueryAPI {
        return doRequest().create(QueryAPI::class.java)
    }
}