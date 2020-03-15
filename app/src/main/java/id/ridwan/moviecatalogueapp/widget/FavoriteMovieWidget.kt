package id.ridwan.moviecatalogueapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import id.ridwan.moviecatalogueapp.R

/**
 * Implementation of App Widget functionality.
 */
class FavoriteMovieWidget : AppWidgetProvider() {

    companion object{

        private const val TOAST_ACTION = "id.ridwan.moviecatalogueapp.TOAST_ACTION"
        const val EXTRA_ITEM = "id.ridwan.moviecatalogueapp.EXTRA_ITEM"


        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int){
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.favorite_movie_widget)
            views.setRemoteAdapter(R.id.stackViewM,intent)
            views.setEmptyView(R.id.stackViewM,R.id.emptyM)

            val toastIntent = Intent(context, FavoriteMovieWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastPendingIntent = PendingIntent.getBroadcast(context,0,toastIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stackViewM,toastPendingIntent)

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stackViewM)
            appWidgetManager.updateAppWidget(appWidgetId,views)
        }

    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if(intent.action != null){
            if(intent.action == TOAST_ACTION){
                val viewIndex = intent.getIntExtra(EXTRA_ITEM,0)
                Toast.makeText(context,"Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

