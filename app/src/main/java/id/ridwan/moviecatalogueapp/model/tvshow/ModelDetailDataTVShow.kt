package id.ridwan.moviecatalogueapp.model.tvshow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelDetailDataTVShow(
    @SerializedName("backdrop_path")
    var backdrop: String?,
    @SerializedName("overview")
    var synopsis: String?,
    @SerializedName("number_of_seasons")
    var seasons: Int?,
    @SerializedName("number_of_episodes")
    var episodes: Int?,
    @SerializedName("first_air_date")
    var firstAir: String? = null
) : Parcelable