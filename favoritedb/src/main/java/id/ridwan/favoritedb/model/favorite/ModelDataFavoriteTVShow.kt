package id.ridwan.favoritedb.model.favorite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelDataFavoriteTVShow(
    var idTVShow: Int? = null,
    var posterTVShow: String? = null,
    var titleTVShow: String? = null,
    var ratingTVShow: Float? = null
) : Parcelable