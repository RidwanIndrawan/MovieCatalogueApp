package id.ridwan.favoritedb.userInterface.favoriteTVShow


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
import id.ridwan.favoritedb.adapter.DataFavoriteTVShowAdapter
import id.ridwan.favoritedb.helper.MappingHelper
import id.ridwan.favoritedb.model.DatabaseContract.FavoriteTVShowColumns.Companion.CONTENT_URI_TVSHOW
import id.ridwan.favoritedb.model.favorite.ModelDataFavoriteTVShow
import kotlinx.android.synthetic.main.fragment_favorite_tvshow.*
import kotlinx.android.synthetic.main.fragment_favorite_tvshow.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTVShowFragment : Fragment() {

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    private var favTVShow = ArrayList<ModelDataFavoriteTVShow>()
    private lateinit var dataFavoriteTVShowAdapter: DataFavoriteTVShowAdapter
    private lateinit var favoriteTVShowFragmentView: View

    private var mSavedInstanceState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        favoriteTVShowFragmentView =
            inflater.inflate(R.layout.fragment_favorite_tvshow, container, false)


        dataFavoriteTVShowAdapter = DataFavoriteTVShowAdapter(this)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavTVShowAsync()
            }
        }

        context?.contentResolver?.registerContentObserver(CONTENT_URI_TVSHOW, true, myObserver)

        if (savedInstanceState != null) {
            loadFavTVShowAsync()
        } else {
            val listFavTV = savedInstanceState?.getParcelableArrayList<ModelDataFavoriteTVShow>(
                EXTRA_STATE
            )
            if (listFavTV != null) {
                dataFavoriteTVShowAdapter.listTVShow = listFavTV
            }
        }


        favoriteTVShowFragmentView.rvFavTV.layoutManager = GridLayoutManager(context, 2)
        favoriteTVShowFragmentView.rvFavTV.setHasFixedSize(true)
        favoriteTVShowFragmentView.rvFavTV.adapter = dataFavoriteTVShowAdapter

        setData()

        return favoriteTVShowFragmentView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when(requestCode)
            {
                DetailFavoriteTVShowActivity.REQUEST_UPDATETV -> if (resultCode == DetailFavoriteTVShowActivity.RESULT_FAVORITE_TVSHOW){
                    val position = data.getIntExtra(DetailFavoriteTVShowActivity.EXTRA_POSITIONTV,0)

                    dataFavoriteTVShowAdapter.removeItem(position)
                }
            }
        }
    }

    private fun loadFavTVShowAsync() {
        if (favTVShow.isEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredFavTVShow = async(Dispatchers.IO) {
                    val cursor = context?.contentResolver?.query(
                        CONTENT_URI_TVSHOW,
                        null,
                        null,
                        null,
                        null
                    ) as Cursor
                    MappingHelper.mapFavoriteTVShowCursorToArrayList(cursor)
                }
                favTVShow = deferredFavTVShow.await()
                if (favTVShow.size > 0) {
                    dataFavoriteTVShowAdapter.listTVShow = favTVShow
                    rvFavTV.visibility = View.VISIBLE
                    dogeTV.visibility = View.GONE
                    dogeTextTV.visibility = View.GONE
                } else {
                    dataFavoriteTVShowAdapter.listTVShow = ArrayList()
                    rvFavTV.visibility = View.GONE
                    dogeTV.visibility = View.VISIBLE
                    dogeTextTV.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, dataFavoriteTVShowAdapter.listTVShow)

    }


    private fun setData() {
        if (mSavedInstanceState == null) {
            context?.let { loadFavTVShowAsync() }
        } else {
            val favoriteTVShow =
                mSavedInstanceState!!.getParcelableArrayList<ModelDataFavoriteTVShow>(EXTRA_STATE)
            if (favoriteTVShow != null) {
                dataFavoriteTVShowAdapter.listTVShow = favoriteTVShow
            }
        }
    }

}
