package id.ridwan.moviecatalogueapp.model.tvshow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelListTVShow(
    @SerializedName("page")
    var page: Int?,
    @SerializedName("results")
    var dataTVShows: ArrayList<ModelDataTVShow>?
) : Parcelable