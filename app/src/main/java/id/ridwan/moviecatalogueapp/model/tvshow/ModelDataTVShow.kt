package id.ridwan.moviecatalogueapp.model.tvshow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelDataTVShow(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("poster_path")
    var poster: String? = null,
    @SerializedName("original_name")
    var title: String? = null,
    @SerializedName("vote_average")
    var vote: Float? = null
) : Parcelable