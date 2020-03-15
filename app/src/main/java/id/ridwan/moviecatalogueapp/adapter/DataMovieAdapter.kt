package id.ridwan.moviecatalogueapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.model.movie.ModelDataMovie
import id.ridwan.moviecatalogueapp.userInterface.customClick.CustomOnItemClickListener
import id.ridwan.moviecatalogueapp.userInterface.movie.DetailMovieActivity
import kotlinx.android.synthetic.main.item_row_movies.view.*

class DataMovieAdapter : RecyclerView.Adapter<DataMovieAdapter.ListViewHolder>() {

    private val listMovie = ArrayList<ModelDataMovie>()

    fun setMovie(movie: ArrayList<ModelDataMovie>) {
        listMovie.clear()
        listMovie.addAll(movie)
        notifyDataSetChanged()
    }

    fun addMovie(movie: ArrayList<ModelDataMovie>) {
        listMovie.addAll(movie)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_movies, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataMovie: ModelDataMovie) {
            Glide.with(itemView.context)
                .load("${BuildConfig.IMAGE_URL}t/p/w185${dataMovie.poster}")
                .into(itemView.posterM)
            itemView.ratingM.text = dataMovie.vote.toString()
            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            Toast.makeText(itemView.context, dataMovie.title, Toast.LENGTH_LONG)
                                .show()

                            val intent = Intent(itemView.context, DetailMovieActivity::class.java)

                            intent.putExtra(DetailMovieActivity.KEY, dataMovie)
                            itemView.context.startActivity(intent)
                        }
                    })
            )


        }

    }

}