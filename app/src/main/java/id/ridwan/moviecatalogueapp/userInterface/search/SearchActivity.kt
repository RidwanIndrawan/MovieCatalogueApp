package id.ridwan.moviecatalogueapp.userInterface.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.adapter.SearchAdapter
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

    interface MovieSearchListener {
        fun querySearchMovie(query: String?)
    }

    interface TVShowSearchListener {
        fun querySearchTVShow(query: String?)
    }

    private lateinit var searchAdapter: SearchAdapter

    private var movieSearchListener: MovieSearchListener? = null
    private var tvShowSearchListener: TVShowSearchListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbarS)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.elevation = 0f

        setupViewPager(viewPagerS)
        tabsS.setupWithViewPager(viewPagerS)

        backButton()
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchButton = menu?.findItem(R.id.btnSearch)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchButton?.actionView as SearchView

        val backButton =
            searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        backButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))

        val editText = searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))

        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search_24dp))

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                movieSearchListener?.querySearchMovie(query)
                tvShowSearchListener?.querySearchTVShow(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onPrepareOptionsMenu(menu)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        searchAdapter = SearchAdapter(supportFragmentManager)
        searchAdapter.addFragment(SearchMovieFragment(), getString(R.string.movie))
        searchAdapter.addFragment(SearchTVShowFragment(), getString(R.string.tvshow))


        viewPager.adapter = searchAdapter
    }

    fun setQueryMovieListener(movieSearchListener: MovieSearchListener) {
        this.movieSearchListener = movieSearchListener
    }

    fun setQueryTVShowListener(tvShowSearchListener: TVShowSearchListener) {
        this.tvShowSearchListener = tvShowSearchListener
    }

    private fun backButton() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setSupportActionBar(toolbarS)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        toolbarS.setNavigationOnClickListener {
            finish()
        }
    }
}
