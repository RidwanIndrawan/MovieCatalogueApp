package id.ridwan.favoritedb.model.favorite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelDataFavoriteMovie(
    var idMovie: Int? = null,
    var posterMovie: String? = null,
    var titleMovie: String? = null,
    var ratingMovie: Float? = null
) : Parcelable