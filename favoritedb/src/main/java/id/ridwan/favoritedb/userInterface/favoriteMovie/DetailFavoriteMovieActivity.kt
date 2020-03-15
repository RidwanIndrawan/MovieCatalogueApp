package id.ridwan.favoritedb.userInterface.favoriteMovie

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
import id.ridwan.favoritedb.BuildConfig
import id.ridwan.favoritedb.R
import id.ridwan.favoritedb.model.DatabaseContract.FavoriteMovieColumns.Companion.CONTENT_URI_MOVIE
import id.ridwan.favoritedb.model.DatabaseContract.FavoriteMovieColumns.Companion.ID_MOVIE
import id.ridwan.favoritedb.model.DatabaseContract.FavoriteMovieColumns.Companion.POSTER_MOVIE
import id.ridwan.favoritedb.model.DatabaseContract.FavoriteMovieColumns.Companion.RATING_MOVIE
import id.ridwan.favoritedb.model.DatabaseContract.FavoriteMovieColumns.Companion.TITLE_MOVIE
import id.ridwan.favoritedb.model.favorite.ModelDataFavoriteMovie
import id.ridwan.favoritedb.viewModel.DetailMovieViewModel
import kotlinx.android.synthetic.main.activity_detail_favorite_movie.*


class DetailFavoriteMovieActivity : AppCompatActivity() {

    companion object {
        const val KEY_FAV = "key_fav"
        const val RESULT_FAVORITE_MOVIE = 200
        const val REQUEST_UPDATE = 100
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var detailMovieViewModel: DetailMovieViewModel
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite_movie)
        backButton()

        val dataFavMovie = intent.getParcelableExtra<ModelDataFavoriteMovie>(KEY_FAV)

        Glide.with(this)
            .load("${BuildConfig.IMAGE_URL}t/p/w500${dataFavMovie.posterMovie}")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_warning_black_24dp)
            )
            .into(posterDetailFavM)
        titleDetailFavM.text = dataFavMovie.titleMovie
        ratingDetailFavM.text = dataFavMovie.ratingMovie.toString()

        detailMovieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailMovieViewModel::class.java)
        detailMovieViewModel.setMovie(
            dataFavMovie.idMovie,
            resources.getString(R.string.languageCode)
        )
        detailMovieViewModel.getDetailMovie()?.observe(this, Observer { detailMovie ->
            showLoading(true)
            if (detailMovie == null) {
                Toast.makeText(this, resources.getString(R.string.checkCon), Toast.LENGTH_LONG)
                    .show()
            } else {
                showLoading(false)
                Glide.with(this)
                    .load("${BuildConfig.IMAGE_URL}t/p/original${detailMovie.backdrop}")
                    .into(backdropFavM)
                dateDetailFavM.text = detailMovie.date
                runtimeDetailFavM.text = detailMovie.runtime.toString()
                synopsisDetailFavM.text = detailMovie.overview
            }
        })

        uriWithId = Uri.parse(CONTENT_URI_MOVIE.toString() + "/" + dataFavMovie.idMovie)

        favoriteMovie.isChecked = true

        favoriteMovie.setOnClickListener {
            if (favoriteMovie.isChecked) {

                val values = ContentValues()
                values.put(ID_MOVIE, dataFavMovie.idMovie)
                values.put(POSTER_MOVIE, dataFavMovie.posterMovie)
                values.put(TITLE_MOVIE, dataFavMovie.titleMovie)
                values.put(RATING_MOVIE, dataFavMovie.ratingMovie)

                contentResolver.insert(CONTENT_URI_MOVIE, values)
            } else {
                contentResolver.delete(uriWithId, ID_MOVIE, null)
            }
            sendResult()
        }
    }


    private fun sendResult(){
        val intent = Intent()
        setResult(RESULT_FAVORITE_MOVIE, intent)
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progBackdrop.visibility = View.VISIBLE
            progRuntime.visibility = View.VISIBLE
            progRating.visibility = View.VISIBLE
            progSynopsis.visibility = View.VISIBLE
        } else {
            progBackdrop.visibility = View.GONE
            progRuntime.visibility = View.GONE
            progRating.visibility = View.GONE
            progSynopsis.visibility = View.GONE
        }
    }

    private fun backButton() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setSupportActionBar(toolbarDetailFavM)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        toolbarDetailFavM.setNavigationOnClickListener {
            finish()
        }
    }
}
