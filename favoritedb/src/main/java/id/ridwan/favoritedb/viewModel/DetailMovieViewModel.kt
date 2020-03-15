package id.ridwan.favoritedb.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ridwan.favoritedb.BuildConfig
import id.ridwan.favoritedb.configAPI.Config
import id.ridwan.favoritedb.model.detail.ModelDetailDataMovie
import retrofit2.Call
import retrofit2.Response

class DetailMovieViewModel : ViewModel() {

    private var dataDetailMovie = MutableLiveData<ModelDetailDataMovie>()

    fun setMovie(id: Int?, languageCode: String) {
        Config().instance().getMovieDetail(id, BuildConfig.API_KEY, languageCode)
            .enqueue(object : retrofit2.Callback<ModelDetailDataMovie> {
                override fun onFailure(call: Call<ModelDetailDataMovie>, t: Throwable) {
                    Log.d("Failed", t.message)
                    dataDetailMovie.postValue(null)
                }

                override fun onResponse(
                    call: Call<ModelDetailDataMovie>,
                    response: Response<ModelDetailDataMovie>
                ) {
                    val detailMovie = ModelDetailDataMovie(
                        response.body()?.overview,
                        response.body()?.runtime,
                        response.body()?.backdrop,
                        response.body()?.date
                    )
                    dataDetailMovie.postValue(detailMovie)
                }

            })
    }

    fun getDetailMovie(): MutableLiveData<ModelDetailDataMovie>? {
        return dataDetailMovie
    }
}