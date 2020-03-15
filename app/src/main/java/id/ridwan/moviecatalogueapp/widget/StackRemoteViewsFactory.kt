package id.ridwan.moviecatalogueapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.helper.MappingHelper
import id.ridwan.moviecatalogueapp.model.favoriteHelper.FavoriteMovieHelper
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


internal class StackRemoteViewsFactory(private val mContext : Context): RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = ArrayList<Bitmap>()

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            null
        }
    }

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val token = Binder.clearCallingIdentity()

        val favoriteMovieHelper = FavoriteMovieHelper.getInstance(mContext)
        favoriteMovieHelper.open()
        val favoriteMovieCursor = favoriteMovieHelper.getAllFavoriteMovie()
        val favoriteMovie = MappingHelper.mapFavoriteMovieCursorToArrayList(favoriteMovieCursor)

        for(i in 0 until favoriteMovie.size){
            mWidgetItems.add(getBitmapFromURL("${BuildConfig.IMAGE_URL}t/p/w185${favoriteMovie[i].posterMovie}")!!)
        }

        Binder.restoreCallingIdentity(token)
    }

    override fun onDestroy() {

    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageFavM,mWidgetItems[position])

        val extras = bundleOf(
            FavoriteMovieWidget.EXTRA_ITEM to position
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageFavM, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getCount(): Int = mWidgetItems.size

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getViewTypeCount(): Int = 1

}

