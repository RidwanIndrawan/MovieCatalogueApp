package id.ridwan.moviecatalogueapp.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelListMovie(
    @SerializedName("page")
    var page: Int?,
    @SerializedName("results")
    var dataMovie: ArrayList<ModelDataMovie>?
) : Parcelable