package id.ridwan.moviecatalogueapp.userInterface.tvshow

import android.content.ContentValues
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
import id.ridwan.moviecatalogueapp.model.favoriteHelper.FavoriteTVShowHelper
import id.ridwan.moviecatalogueapp.model.tvshow.ModelDataTVShow
import id.ridwan.moviecatalogueapp.viewModel.DetailTVShowViewModel
import kotlinx.android.synthetic.main.activity_detail_tvshow.*

class DetailTVShowActivity : AppCompatActivity() {

    companion object {
        const val KEY2 = "key2"
    }

    private lateinit var detailTVShowViewModel: DetailTVShowViewModel
    private lateinit var uriWithIdTv: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tvshow)

        backButton()

        val dataTVShow = intent.getParcelableExtra<ModelDataTVShow>(KEY2)

        Glide.with(this)
            .load("${BuildConfig.IMAGE_URL}t/p/w500${dataTVShow.poster}")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_warning_black_24dp)
            )
            .into(posterDetailTV)
        titleDetailTV.text = dataTVShow.title
        ratingDetailTV.text = dataTVShow.vote.toString()

        detailTVShowViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailTVShowViewModel::class.java
        )
        detailTVShowViewModel.setTVShow(dataTVShow.id, resources.getString(R.string.languageCode))
        detailTVShowViewModel.getDetailTVShow()?.observe(this, Observer { detailTVShow ->
            showLoading(true)
            if (detailTVShow == null) {
                Toast.makeText(this, resources.getString(R.string.checkCon), Toast.LENGTH_LONG)
                    .show()
            } else {
                showLoading(false)
                Glide.with(this)
                    .load("${BuildConfig.IMAGE_URL}t/p/original${detailTVShow.backdrop}")
                    .into(backdropTV)
                seasonDetailTV.text = detailTVShow.seasons.toString()
                episodeDetailTV.text = detailTVShow.episodes.toString()
                synopsisDetailTV.text = detailTVShow.synopsis
                dateDetailTV.text = detailTVShow.firstAir
            }
        })

        uriWithIdTv = Uri.parse(CONTENT_URI_TVSHOW.toString() + "/" + dataTVShow.id)

        favoriteTVShow.setOnClickListener {
            if (favoriteTVShow.isChecked) {

                val values = ContentValues()
                values.put(ID_TVSHOW, dataTVShow.id)
                values.put(POSTER_TVSHOW, dataTVShow.poster)
                values.put(TITLE_TVSHOW, dataTVShow.title)
                values.put(RATING_TVSHOW, dataTVShow.vote)

                contentResolver.insert(CONTENT_URI_TVSHOW, values)
            } else {

                contentResolver.delete(uriWithIdTv, ID_TVSHOW, null)
            }
        }

        checkFavoriteState(dataTVShow.id)
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

        setSupportActionBar(toolbarDetailTV)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        toolbarDetailTV.setNavigationOnClickListener {
            finish()
        }
    }
}