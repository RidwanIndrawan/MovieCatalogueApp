package id.ridwan.moviecatalogueapp.userInterface.home

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.adapter.FavoriteAdapter
import id.ridwan.moviecatalogueapp.model.favoriteHelper.FavoriteMovieHelper
import id.ridwan.moviecatalogueapp.model.favoriteHelper.FavoriteTVShowHelper
import id.ridwan.moviecatalogueapp.userInterface.favoriteMovie.FavoriteMovieFragment
import id.ridwan.moviecatalogueapp.userInterface.favoriteTVShow.FavoriteTVShowFragment
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteMovieHelper: FavoriteMovieHelper
    private lateinit var favoriteTVShowHelper: FavoriteTVShowHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteMovieHelper = FavoriteMovieHelper(applicationContext)
        favoriteTVShowHelper = FavoriteTVShowHelper(applicationContext)

        setupViewPager(viewPagerFav)
        tabsFav.setupWithViewPager(viewPagerFav)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(toolbarFav)
        supportActionBar?.elevation = 0f

        backButton()

    }

    private fun setupViewPager(viewPager: ViewPager) {
        favoriteAdapter = FavoriteAdapter(supportFragmentManager)
        favoriteAdapter.addFragment(FavoriteMovieFragment(), getString(R.string.movie))
        favoriteAdapter.addFragment(FavoriteTVShowFragment(), getString(R.string.tvshow))


        viewPager.adapter = favoriteAdapter
    }

    private fun backButton() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setSupportActionBar(toolbarFav)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        toolbarFav.setNavigationOnClickListener {
            finish()
            favoriteMovieHelper.close()
            favoriteTVShowHelper.close()
        }
    }
}
