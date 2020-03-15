package id.ridwan.moviecatalogueapp.userInterface.favoriteTVShow

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.CONTENT_URI_TVSHOW
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.ID_TVSHOW
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.POSTER_TVSHOW
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.RATING_TVSHOW
import id.ridwan.moviecatalogueapp.model.DatabaseContract.FavoriteTVShowColumns.Companion.TITLE_TVSHOW
import id.ridwan.moviecatalogueapp.model.favorite.ModelDataFavoriteTVShow
import id.ridwan.moviecatalogueapp.model.favoriteHelper.FavoriteTVShowHelper
import id.ridwan.moviecatalogueapp.viewModel.DetailTVShowViewModel
import kotlinx.android.synthetic.main.activity_detail_favorite_tvshow.*
import kotlinx.android.synthetic.main.activity_detail_tvshow.favoriteTVShow
import kotlinx.android.synthetic.main.activity_detail_tvshow.progBackdrop
import kotlinx.android.synthetic.main.activity_detail_tvshow.progEpisode
import kotlinx.android.synthetic.main.activity_detail_tvshow.progRating
import kotlinx.android.synthetic.main.activity_detail_tvshow.progSeason
import kotlinx.android.synthetic.main.activity_detail_tvshow.progSynopsis

class DetailFavoriteTVShowActivity : AppCompatActivity() {

    companion object {
        const val KEY_FAV2 = "key_fav2"
        const val RESULT_FAVORITE_TVSHOW = 400
        const val REQUEST_UPDATETV = 300
        const val EXTRA_POSITIONTV = "extra_position"
    }

    private lateinit var detailTVShowViewModel: DetailTVShowViewModel
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite_tvshow)
        backButton()

        val dataFavTVShow = intent.getParcelableExtra<ModelDataFavoriteTVShow>(KEY_FAV2)

        Glide.with(this)
            .load("${BuildConfig.IMAGE_URL}t/p/w500${dataFavTVShow.posterTVShow}")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_warning_black_24dp)
            )
            .into(posterDetailFavTV)
        titleDetailFavTV.text = dataFavTVShow.titleTVShow
        ratingDetailFavTV.text = dataFavTVShow.ratingTVShow.toString()

        detailTVShowViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailTVShowViewModel::class.java
        )
        detailTVShowViewModel.setTVShow(
            dataFavTVShow.idTVShow,
            resources.getString(R.string.languageCode)
        )
        detailTVShowViewModel.getDetailTVShow()?.observe(this, Observer { detailTVShow ->
            showLoading(true)
            if (detailTVShow == null) {
                Toast.makeText(this, resources.getString(R.string.checkCon), Toast.LENGTH_LONG)
                    .show()
            } else {
                showLoading(false)
                Glide.with(this)
                    .load("${BuildConfig.IMAGE_URL}t/p/original${detailTVShow.backdrop}")
                    .into(backdropFavTV)
                dateDetailFavTV.text = detailTVShow.firstAir
                seasonDetailFavTV.text = detailTVShow.seasons.toString()
                episodeDetailFavTV.text = detailTVShow.episodes.toString()
                synopsisDetailFavTV.text = detailTVShow.synopsis
            }
        })

        uriWithId = Uri.parse(CONTENT_URI_TVSHOW.toString() + "/" + dataFavTVShow.idTVShow)

        favoriteTVShow.setOnClickListener {
            if (favoriteTVShow.isChecked) {

                val values = ContentValues()
                values.put(ID_TVSHOW, dataFavTVShow.idTVShow)
                values.put(POSTER_TVSHOW, dataFavTVShow.posterTVShow)
                values.put(TITLE_TVSHOW, dataFavTVShow.titleTVShow)
                values.put(RATING_TVSHOW, dataFavTVShow.ratingTVShow)

                contentResolver.insert(CONTENT_URI_TVSHOW, values)
            } else {
                contentResolver.delete(uriWithId, ID_TVSHOW, null)
            }
            sendResult()
        }

        checkFavoriteState(dataFavTVShow.idTVShow)
    }

    private fun sendResult(){
        val intent = Intent()
        setResult(RESULT_FAVORITE_TVSHOW, intent)
    }


    private fun checkFavoriteState(idTVShow: Int?) {
        val favoriteTVShowHelper = FavoriteTVShowHelper.getInstance(applicationContext)
        favoriteTVShow.isChecked = favoriteTVShowHelper.isFavorite(idTVShow)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progBackdrop.visibility = View.VISIBLE
            progSeason.visibility = View.VISIBLE
            progEpisode.visibility = View.VISIBLE
            progRating.visibility = View.VISIBLE
            progSynopsis.visibility = View.VISIBLE
        } else {
            progBackdrop.visibility = View.GONE
            progSeason.visibility = View.GONE
            progEpisode.visibility = View.GONE
            progRating.visibility = View.GONE
            progSynopsis.visibility = View.GONE
        }
    }

    private fun backButton() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setSupportActionBar(toolbarDetailFavTV)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        toolbarDetailFavTV.setNavigationOnClickListener {
            finish()
        }
    }
}
