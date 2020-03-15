package id.ridwan.moviecatalogueapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.configAPI.Config
import id.ridwan.moviecatalogueapp.model.movie.ModelDataMovie
import id.ridwan.moviecatalogueapp.model.movie.ModelListMovie
import retrofit2.Call
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val movies = MutableLiveData<ArrayList<ModelDataMovie>>()

    fun setMovies(languageCode: String, page: Int) {
        Config().instance().getMovies(BuildConfig.API_KEY, languageCode, page)
            .enqueue(object : retrofit2.Callback<ModelListMovie> {
                override fun onFailure(call: Call<ModelListMovie>, t: Throwable) {
                    Log.d("Failed", t.message)
                    movies.postValue(null)
                }

                override fun onResponse(
                    call: Call<ModelListMovie>,
                    response: Response<ModelListMovie>
                ) {
                    val listMovies = response.body()?.dataMovie
                    movies.postValue(listMovies)
                }
            })

    }

    fun getMoviees(): LiveData<ArrayList<ModelDataMovie>> {
        return movies
    }

    fun movieSize(): Int? {
        return this.movies.value?.size
    }
}