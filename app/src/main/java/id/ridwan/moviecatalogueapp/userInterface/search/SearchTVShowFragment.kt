package id.ridwan.moviecatalogueapp.userInterface.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.adapter.DataTVShowAdapter
import id.ridwan.moviecatalogueapp.viewModel.SearchTVShowViewModel
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.android.synthetic.main.fragment_search_tvshow.view.*

/**
 * A simple [Fragment] subclass.
 */
class SearchTVShowFragment : Fragment(), SearchActivity.TVShowSearchListener {

    private lateinit var query: String
    private lateinit var searchTVShowView: View
    private lateinit var dataTVShowAdapter: DataTVShowAdapter
    private lateinit var searchTVShowViewModel: SearchTVShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        searchTVShowView = inflater.inflate(R.layout.fragment_search_tvshow, container, false)

        (activity as SearchActivity).setQueryTVShowListener(this)

        dataTVShowAdapter = DataTVShowAdapter()
        dataTVShowAdapter.notifyDataSetChanged()
        searchTVShowView.rvSearchTVShow.adapter = dataTVShowAdapter
        searchTVShowView.rvSearchTVShow.layoutManager = GridLayoutManager(context, 2)
        searchTVShowView.rvSearchTVShow.setHasFixedSize(true)

        searchTVShowViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SearchTVShowViewModel::class.java
        )

        searchTVShowView.refreshLayoutParentSTV.setOnRefreshListener {
            searchTVShowViewModel.setTVShows(resources.getString(R.string.languageCode), query)
        }

        return searchTVShowView
    }

    override fun querySearchTVShow(query: String?) {
        this.query = query.toString()
        searchTVShowViewModel.setTVShows(resources.getString(R.string.languageCode), query)
        showLoading(true)
        searchTVShowViewModel.getTVShows().observe(this, Observer { tvshow ->
            searchTVShowView.refreshLayoutParentSTV.finishRefresh(true)
            searchTVShowView.refreshLayoutParentSTV.finishLoadMore(true)

            if (tvshow != null) {
                showLoading(false)
                if (tvshow.size == 0) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.notFound),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    dataTVShowAdapter.setTVShow(tvshow)
                }
            } else {
                showLoading(true)
                Toast.makeText(context, resources.getString(R.string.checkCon), Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
