package id.ridwan.favoritedb.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ridwan.favoritedb.BuildConfig
import id.ridwan.favoritedb.R
import id.ridwan.favoritedb.model.favorite.ModelDataFavoriteTVShow
import id.ridwan.favoritedb.userInterface.customClick.CustomOnItemClickListener
import id.ridwan.favoritedb.userInterface.favoriteTVShow.DetailFavoriteTVShowActivity
import kotlinx.android.synthetic.main.item_row_tvshows.view.*

class DataFavoriteTVShowAdapter(private val fragment: Fragment) : RecyclerView.Adapter<DataFavoriteTVShowAdapter.ListViewHolder>() {

    var listTVShow = ArrayList<ModelDataFavoriteTVShow>()
        set(listMovie) {
            if (listMovie.size > 0) {
                this.listTVShow.clear()
            }
            this.listTVShow.addAll(listMovie)

            notifyDataSetChanged()
        }

    fun removeItem(position: Int){
        this.listTVShow.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, this.listTVShow.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_tvshows, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listTVShow.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listTVShow[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataFavTVShow: ModelDataFavoriteTVShow) {
            Glide.with(itemView.context)
                .load("${BuildConfig.IMAGE_URL}t/p/w185${dataFavTVShow.posterTVShow}")
                .into(itemView.posterTV)
            itemView.ratingTV.text = dataFavTVShow.ratingTVShow.toString()
            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            Toast.makeText(
                                itemView.context,
                                dataFavTVShow.titleTVShow,
                                Toast.LENGTH_LONG
                            ).show()

                            val intent =
                                Intent(fragment.context, DetailFavoriteTVShowActivity::class.java)
                            intent.putExtra(DetailFavoriteTVShowActivity.EXTRA_POSITIONTV, position)
                            intent.putExtra(DetailFavoriteTVShowActivity.KEY_FAV2, dataFavTVShow)
                            fragment.startActivityForResult(intent, DetailFavoriteTVShowActivity.REQUEST_UPDATETV)

                        }
                    })
            )

        }


    }
}