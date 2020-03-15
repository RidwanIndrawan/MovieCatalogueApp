package id.ridwan.favoritedb.userInterface.favoriteMovie


import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import id.ridwan.favoritedb.R
import id.ridwan.favoritedb.adapter.DataFavoriteMovieAdapter
import id.ridwan.favoritedb.helper.MappingHelper.mapFavoriteMovieCursorToArrayList
import id.ridwan.favoritedb.model.DatabaseContract.FavoriteMovieColumns.Companion.CONTENT_URI_MOVIE
import id.ridwan.favoritedb.model.favorite.ModelDataFavoriteMovie
import id.ridwan.favoritedb.userInterface.favoriteMovie.DetailFavoriteMovieActivity.Companion.EXTRA_POSITION
import id.ridwan.favoritedb.userInterface.favoriteMovie.DetailFavoriteMovieActivity.Companion.REQUEST_UPDATE
import id.ridwan.favoritedb.userInterface.favoriteMovie.DetailFavoriteMovieActivity.Companion.RESULT_FAVORITE_MOVIE
import kotlinx.android.synthetic.main.fragment_favorite_movie.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    private lateinit var dataFavoriteMovieAdapter: DataFavoriteMovieAdapter
    private lateinit var favoriteMovieFragmentView: View

    private var favMovie = ArrayList<ModelDataFavoriteMovie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        favoriteMovieFragmentView =
            inflater.inflate(R.layout.fragment_favorite_movie, container, false)

        dataFavoriteMovieAdapter = DataFavoriteMovieAdapter( this)
        favoriteMovieFragmentView.rvFavMovie.layoutManager = GridLayoutManager(context, 2)
        favoriteMovieFragmentView.rvFavMovie.setHasFixedSize(true)
        dataFavoriteMovieAdapter.notifyDataSetChanged()
        favoriteMovieFragmentView.rvFavMovie.adapter = dataFavoriteMovieAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavMovieAsync()
            }
        }

        context?.contentResolver?.registerContentObserver(CONTENT_URI_MOVIE, true, myObserver)

        if (savedInstanceState == null) {
            loadFavMovieAsync()
        } else {
            val list =
                savedInstanceState.getParcelableArrayList<ModelDataFavoriteMovie>(EXTRA_STATE)
            if (list != null) {
                dataFavoriteMovieAdapter.listMovie = list
                favoriteMovieFragmentView.doge.visibility = View.GONE
                favoriteMovieFragmentView.dogeText.visibility = View.GONE
            }
        }


        return favoriteMovieFragmentView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, dataFavoriteMovieAdapter.listMovie)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when(requestCode)
            {
                REQUEST_UPDATE -> if (resultCode == RESULT_FAVORITE_MOVIE){
                    val position = data.getIntExtra(EXTRA_POSITION,0)

                    dataFavoriteMovieAdapter.removeItem(position)
                }
            }
        }
    }

    private fun loadFavMovieAsync() {
        if (favMovie.isEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredFavMovie = async(Dispatchers.IO) {
                    val cursor = context?.contentResolver?.query(
                        CONTENT_URI_MOVIE,
                        null,
                        null,
                        null,
                        null
                    ) as Cursor
                    mapFavoriteMovieCursorToArrayList(cursor)
                }
                favMovie = deferredFavMovie.await()
                if (favMovie.size > 0) {
                    dataFavoriteMovieAdapter.listMovie = favMovie
                    favoriteMovieFragmentView.doge.visibility = View.GONE
                    favoriteMovieFragmentView.dogeText.visibility = View.GONE
                } else {
                    dataFavoriteMovieAdapter.listMovie = ArrayList()
                    favoriteMovieFragmentView.doge.visibility = View.VISIBLE
                    favoriteMovieFragmentView.dogeText.visibility = View.VISIBLE
                }
            }
        }
    }

}
