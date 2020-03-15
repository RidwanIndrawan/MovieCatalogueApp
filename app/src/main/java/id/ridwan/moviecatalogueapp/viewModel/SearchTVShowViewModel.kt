package id.ridwan.moviecatalogueapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.configAPI.Config
import id.ridwan.moviecatalogueapp.model.tvshow.ModelDataTVShow
import id.ridwan.moviecatalogueapp.model.tvshow.ModelListTVShow
import retrofit2.Call
import retrofit2.Response

class SearchTVShowViewModel : ViewModel() {

    private val tvShows = MutableLiveData<ArrayList<ModelDataTVShow>>()

    fun setTVShows(languageCode: String, query: String?) {
        Config().instance().searchTVShow(BuildConfig.API_KEY, languageCode, query)
            .enqueue(object : retrofit2.Callback<ModelListTVShow> {
                override fun onFailure(call: Call<ModelListTVShow>, t: Throwable) {
                    Log.d("Failed", t.message)
                    tvShows.postValue(null)
                }

                override fun onResponse(
                    call: Call<ModelListTVShow>,
                    response: Response<ModelListTVShow>
                ) {
                    val listTVshows = response.body()?.dataTVShows
                    tvShows.postValue(listTVshows)
                }
            })

    }

    fun getTVShows(): LiveData<ArrayList<ModelDataTVShow>> {
        return tvShows
    }

}