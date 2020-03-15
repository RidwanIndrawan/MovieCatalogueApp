package id.ridwan.moviecatalogueapp.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelDetailDataMovie(
    @SerializedName("overview")
    var overview: String?,
    @SerializedName("runtime")
    var runtime: Int?,
    @SerializedName("backdrop_path")
    var backdrop: String?,
    @SerializedName("release_date")
    var date: String? = null
) : Parcelable