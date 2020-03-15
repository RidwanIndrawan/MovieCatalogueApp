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
import id.ridwan.moviecatalogueapp.model.tvshow.ModelDataTVShow
import id.ridwan.moviecatalogueapp.userInterface.customClick.CustomOnItemClickListener
import id.ridwan.moviecatalogueapp.userInterface.tvshow.DetailTVShowActivity
import kotlinx.android.synthetic.main.item_row_tvshows.view.*

class DataTVShowAdapter : RecyclerView.Adapter<DataTVShowAdapter.ListViewHolder>() {

    private val listTVShow = ArrayList<ModelDataTVShow>()

    fun setTVShow(tvshow: ArrayList<ModelDataTVShow>) {
        listTVShow.clear()
        listTVShow.addAll(tvshow)
        notifyDataSetChanged()
    }

    fun addTVShow(tvshow: ArrayList<ModelDataTVShow>) {
        listTVShow.addAll(tvshow)
        notifyDataSetChanged()
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
        fun bind(dataTVShow: ModelDataTVShow) {
            Glide.with(itemView.context)
                .load("${BuildConfig.IMAGE_URL}t/p/w185${dataTVShow.poster}")
                .into(itemView.posterTV)
            itemView.ratingTV.text = dataTVShow.vote.toString()
            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            Toast.makeText(itemView.context, dataTVShow.title, Toast.LENGTH_LONG)
                                .show()

                            val intent = Intent(itemView.context, DetailTVShowActivity::class.java)

                            intent.putExtra(DetailTVShowActivity.KEY2, dataTVShow)
                            itemView.context.startActivity(intent)

                        }
                    })
            )

        }


    }

}