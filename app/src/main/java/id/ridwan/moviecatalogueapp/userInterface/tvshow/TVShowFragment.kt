package id.ridwan.moviecatalogueapp.userInterface.tvshow


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
import id.ridwan.moviecatalogueapp.adapter.DataTVShowAdapter
import id.ridwan.moviecatalogueapp.viewModel.TVShowViewModel
import kotlinx.android.synthetic.main.fragment_tvshow.view.*

/**
 * A simple [Fragment] subclass.
 */
class TVShowFragment : Fragment() {

    private lateinit var tvShowViewModel: TVShowViewModel
    private lateinit var tvShowView: View
    private lateinit var dataTVShowAdapter: DataTVShowAdapter

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tvShowView = inflater.inflate(R.layout.fragment_tvshow, container, false)

        dataTVShowAdapter = DataTVShowAdapter()
        dataTVShowAdapter.notifyDataSetChanged()

        tvShowView.rvTVShow.layoutManager = GridLayoutManager(context, 2)
        tvShowView.rvTVShow.setHasFixedSize(true)
        tvShowView.rvTVShow.adapter = dataTVShowAdapter

        tvShowViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TVShowViewModel::class.java)

        if (tvShowViewModel.tvShowSize() == null) {
            tvShowViewModel.setTVShows(resources.getString(R.string.languageCode), page)
        }

        tvShowView.refreshLayoutParentTV.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page += 1
                tvShowViewModel.setTVShows(resources.getString(R.string.languageCode), page)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                tvShowViewModel.setTVShows(resources.getString(R.string.languageCode), page)
            }

        })

        tvShowViewModel.getTVShows().observe(viewLifecycleOwner, Observer { tvShows ->
            tvShowView.refreshLayoutParentTV.finishRefresh(true)
            tvShowView.refreshLayoutParentTV.finishLoadMore(true)
            if (tvShows != null) {
                if (page == 1) {
                    dataTVShowAdapter.setTVShow(tvShows)
                } else {
                    dataTVShowAdapter.addTVShow(tvShows)
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.checkCon), Toast.LENGTH_LONG)
                    .show()
            }
        })

        return tvShowView
    }


}
