package id.ridwan.moviecatalogueapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.configAPI.Config
import id.ridwan.moviecatalogueapp.model.tvshow.ModelDetailDataTVShow
import retrofit2.Call
import retrofit2.Response

class DetailTVShowViewModel : ViewModel() {

    private var dataDetailTVShow = MutableLiveData<ModelDetailDataTVShow>()

    fun setTVShow(id: Int?, languageCode: String) {
        Config().instance().getTVShowsDetail(id, BuildConfig.API_KEY, languageCode)
            .enqueue(object : retrofit2.Callback<ModelDetailDataTVShow> {
                override fun onFailure(call: Call<ModelDetailDataTVShow>, t: Throwable) {
                    Log.d("Failed", t.message)
                    dataDetailTVShow.postValue(null)
                }

                override fun onResponse(
                    call: Call<ModelDetailDataTVShow>,
                    response: Response<ModelDetailDataTVShow>
                ) {
                    val detailTVShow = ModelDetailDataTVShow(
                        response.body()?.backdrop,
                        response.body()?.synopsis,
                        response.body()?.seasons,
                        response.body()?.episodes,
                        response.body()?.firstAir
                    )
                    dataDetailTVShow.postValue(detailTVShow)
                }

            })
    }

    fun getDetailTVShow(): MutableLiveData<ModelDetailDataTVShow>? {
        return dataDetailTVShow
    }
}