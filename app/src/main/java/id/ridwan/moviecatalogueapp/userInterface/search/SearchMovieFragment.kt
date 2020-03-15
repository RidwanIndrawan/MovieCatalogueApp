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
import id.ridwan.moviecatalogueapp.adapter.DataMovieAdapter
import id.ridwan.moviecatalogueapp.viewModel.SearchMovieViewModel
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.android.synthetic.main.fragment_search_movie.view.*

/**
 * A simple [Fragment] subclass.
 */
class SearchMovieFragment : Fragment(), SearchActivity.MovieSearchListener {

    private lateinit var query: String
    private lateinit var searchMovieView: View
    private lateinit var dataMovieAdapter: DataMovieAdapter
    private lateinit var searchMovieViewModel: SearchMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        searchMovieView = inflater.inflate(R.layout.fragment_search_movie, container, false)

        (activity as SearchActivity).setQueryMovieListener(this)

        dataMovieAdapter = DataMovieAdapter()
        dataMovieAdapter.notifyDataSetChanged()
        searchMovieView.rvSearchMovie.adapter = dataMovieAdapter
        searchMovieView.rvSearchMovie.layoutManager = GridLayoutManager(context, 2)
        searchMovieView.rvSearchMovie.setHasFixedSize(true)

        searchMovieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchMovieViewModel::class.java)

        searchMovieView.refreshLayoutParentSM.setOnRefreshListener {
            searchMovieViewModel.setMovies(resources.getString(R.string.languageCode), query)
        }

        return searchMovieView
    }

    override fun querySearchMovie(query: String?) {
        this.query = query.toString()
        searchMovieViewModel.setMovies(resources.getString(R.string.languageCode), query)
        showLoading(true)
        searchMovieViewModel.getMoviees().observe(this, Observer { movie ->
            searchMovieView.refreshLayoutParentSM.finishRefresh(true)
            searchMovieView.refreshLayoutParentSM.finishLoadMore(true)

            if (movie != null) {
                showLoading(false)
                if (movie.size == 0) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.notFound),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    dataMovieAdapter.setMovie(movie)
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
