package id.ridwan.moviecatalogueapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.model.favorite.ModelDataFavoriteMovie
import id.ridwan.moviecatalogueapp.userInterface.customClick.CustomOnItemClickListener
import id.ridwan.moviecatalogueapp.userInterface.favoriteMovie.DetailFavoriteMovieActivity
import kotlinx.android.synthetic.main.item_row_movies.view.*

class DataFavoriteMovieAdapter(private val fragment: Fragment) : RecyclerView.Adapter<DataFavoriteMovieAdapter.ListViewHolder>() {


    var listMovie = ArrayList<ModelDataFavoriteMovie>()
        set(listMovie) {
            if (listMovie.size > 0) {
                this.listMovie.clear()
            }
            this.listMovie.addAll(listMovie)

            notifyDataSetChanged()
        }

    fun removeItem(position: Int){
        this.listMovie.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, this.listMovie.size)
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
        fun bind(dataFavoriteMovie: ModelDataFavoriteMovie) {
            Glide.with(itemView.context)
                .load("${BuildConfig.IMAGE_URL}t/p/w185${dataFavoriteMovie.posterMovie}")
                .into(itemView.posterM)
            itemView.ratingM.text = dataFavoriteMovie.ratingMovie.toString()
            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            Toast.makeText(
                                itemView.context,
                                dataFavoriteMovie.titleMovie,
                                Toast.LENGTH_LONG
                            ).show()


                            val intent =
                                Intent(fragment.context, DetailFavoriteMovieActivity::class.java)
                            intent.putExtra(DetailFavoriteMovieActivity.EXTRA_POSITION,position)
                            intent.putExtra(DetailFavoriteMovieActivity.KEY_FAV, dataFavoriteMovie)
                            fragment.startActivityForResult(intent,DetailFavoriteMovieActivity.REQUEST_UPDATE)

                        }
                    })
            )

        }

    }


}