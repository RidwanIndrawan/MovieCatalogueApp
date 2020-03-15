package id.ridwan.moviecatalogueapp.userInterface.movie


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.adapter.DataMovieAdapter
import id.ridwan.moviecatalogueapp.viewModel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.view.*


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieView: View
    private lateinit var dataMovieAdapter: DataMovieAdapter


    private var page = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        movieView = inflater.inflate(R.layout.fragment_movie, container, false)

        dataMovieAdapter = DataMovieAdapter()
        dataMovieAdapter.notifyDataSetChanged()

        movieView.rvMovie.layoutManager = GridLayoutManager(context, 2)
        movieView.rvMovie.setHasFixedSize(true)
        movieView.rvMovie.adapter = dataMovieAdapter


        movieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)

        if (movieViewModel.movieSize() == null) {
            movieViewModel.setMovies(resources.getString(R.string.languageCode), page)
        }

        movieView.refreshLayoutParentM.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page += 1
                movieViewModel.setMovies(resources.getString(R.string.languageCode), page)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                movieViewModel.setMovies(resources.getString(R.string.languageCode), page)
            }

        })

        movieViewModel.getMoviees().observe(viewLifecycleOwner, Observer { movies ->
            movieView.refreshLayoutParentM.finishRefresh(true)
            movieView.refreshLayoutParentM.finishLoadMore(true)
            showLoading(true)
            if (movies != null) {
                showLoading(false)
                if (page == 1) {
                    dataMovieAdapter.setMovie(movies)
                } else {
                    dataMovieAdapter.addMovie(movies)
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.checkCon), Toast.LENGTH_LONG)
                    .show()
            }
        })
        return movieView
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}




