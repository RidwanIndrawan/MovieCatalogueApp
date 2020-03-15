package id.ridwan.moviecatalogueapp.userInterface.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.adapter.TabAdapter
import id.ridwan.moviecatalogueapp.language.LanguageManager
import id.ridwan.moviecatalogueapp.userInterface.movie.MovieFragment
import id.ridwan.moviecatalogueapp.userInterface.search.SearchActivity
import id.ridwan.moviecatalogueapp.userInterface.setting.SettingActivity
import id.ridwan.moviecatalogueapp.userInterface.tvshow.TVShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tabAdapter: TabAdapter
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        languageManager = LanguageManager(this)
        languageManager.loadLocale()


        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        if (isIndonesian(languageManager.getLang())) {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_indonesian)
        } else {
            menu?.getItem(2)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_english)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.btnFavorite -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.btnSearch -> {
                Intent(this, SearchActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.btnLanguage -> showChangeLanguageDialog()

            R.id.btnSetting -> {
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        tabAdapter = TabAdapter(supportFragmentManager)
        tabAdapter.addFragment(MovieFragment(), getString(R.string.movie))
        tabAdapter.addFragment(TVShowFragment(), getString(R.string.tvshow))


        viewPager.adapter = tabAdapter
    }

    private fun isIndonesian(myLang: String?): Boolean {
        return myLang == "in"
    }

    @SuppressLint("PrivateResource")
    private fun reOpen() {
        finish()
        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom)
        startActivity(intent)
    }

    private fun showChangeLanguageDialog() {
        val checkedItem: Int = if (isIndonesian(languageManager.getLang())) {
            1
        } else {
            0
        }
        val listItems = arrayOf(getString(R.string.english), getString(R.string.indonesia))
        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle(getString(R.string.choose))
        mBuilder.setSingleChoiceItems(listItems, checkedItem) { dialogInterface, i ->
            if (i == 0) {
                languageManager.setLocale("en")
                reOpen()
            } else if (i == 1) {
                languageManager.setLocale("in")
                reOpen()
            }
            dialogInterface.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

}
