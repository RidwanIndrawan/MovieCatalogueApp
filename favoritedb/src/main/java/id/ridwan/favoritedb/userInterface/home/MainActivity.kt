package id.ridwan.favoritedb.userInterface.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import id.ridwan.favoritedb.R
import id.ridwan.favoritedb.adapter.FavoriteAdapter
import id.ridwan.favoritedb.userInterface.favoriteMovie.FavoriteMovieFragment
import id.ridwan.favoritedb.userInterface.favoriteTVShow.FavoriteTVShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0f
    }

    private fun setupViewPager(viewPager: ViewPager) {
        favoriteAdapter = FavoriteAdapter(supportFragmentManager)
        favoriteAdapter.addFragment(FavoriteMovieFragment(), getString(R.string.movie))
        favoriteAdapter.addFragment(FavoriteTVShowFragment(), getString(R.string.tvshow))


        viewPager.adapter = favoriteAdapter
    }

}
