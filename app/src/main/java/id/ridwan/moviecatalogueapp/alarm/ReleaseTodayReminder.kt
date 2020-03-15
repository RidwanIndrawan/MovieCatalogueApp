package id.ridwan.moviecatalogueapp.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import id.ridwan.moviecatalogueapp.BuildConfig
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.configAPI.Config
import id.ridwan.moviecatalogueapp.model.NotificationItem
import id.ridwan.moviecatalogueapp.model.movie.ModelDataMovie
import id.ridwan.moviecatalogueapp.model.movie.ModelListMovie
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ReleaseTodayReminder:BroadcastReceiver(){

    private var REQUEST_CODE = 99

    private lateinit var title : String

    val movies = MutableLiveData<ArrayList<ModelDataMovie>>()
    private val stackNotif = ArrayList<NotificationItem>()

    companion object{
        private const val MAX_NOTIFICATION = 104
        private const val GROUP_MOVIE = "group_movies"
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            getMovies(context)
        }
    }

    private fun sendNotification(context: Context?, title:String?,message: String?,id:Int){


        val CHANNEL_ID = context?.getString(R.string.chTodayID)
        val CHANNEL_NAME = context?.getString(R.string.chTodayName)

//        val notifIntent = Intent(context,DetailMovieActivity::class.java)
//        notifIntent.putExtra(DetailMovieActivity.KEY, movies)
//        val pendingIntent = TaskStackBuilder.create(context)
//            .addParentStack(ReleaseTodayActivity::class.java)
//            .addNextIntent(notifIntent)
//            .getPendingIntent(99, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(REQUEST_CODE <= MAX_NOTIFICATION) {
            val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notifBuilder = NotificationCompat.Builder(context, CHANNEL_ID!!)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSoundUri)
//            .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

                notifBuilder.setChannelId(CHANNEL_ID)

                notificationManager.createNotificationChannel(channel)
            }

            val notification = notifBuilder.build()

            notificationManager.notify(id, notification)
        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine(context.resources?.getString(R.string.NewMovie) + stackNotif[id].title)
                .addLine(context.resources?.getString(R.string.NewMovie) + stackNotif[id-1].title)
                .setBigContentTitle("$id new Movie")
                .setSummaryText(context.resources?.getString(R.string.TodayMessage))
            val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notifBuilder = NotificationCompat.Builder(context, CHANNEL_ID!!)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSoundUri)
                .setGroup(GROUP_MOVIE)
                .setAutoCancel(true)
                .setStyle(inboxStyle)

            val notification = notifBuilder.build()

            notificationManager.notify(id, notification)
        }
    }

        private fun getMovies(context: Context) {
            val languageCode : String = context.resources.getString(R.string.languageCode)
            val date : String = getCurrentDate()
            Config().instance().getTodayReleasedMovie(BuildConfig.API_KEY, languageCode, date, date)
                .enqueue(object : retrofit2.Callback<ModelListMovie> {
                    override fun onFailure(call: Call<ModelListMovie>, t: Throwable) {
                        Log.d("Failed", t.message)
                        movies.postValue(null)
                    }

                    override fun onResponse(
                        call: Call<ModelListMovie>,
                        response: Response<ModelListMovie>
                    ) {
                        val listMovies = response.body()?.dataMovie
                        for(i in 0 until listMovies!!.size)
                            title = listMovies[i].title!!
                            val message = context.getString(R.string.TodayMessage)
                            val notifId = REQUEST_CODE

                            stackNotif.add(NotificationItem(REQUEST_CODE,title))

                            sendNotification(context,title,message,notifId)
                            REQUEST_CODE++
                        movies.postValue(listMovies)
                    }
                })

        }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar)
    }


    fun setRepeatingAlarm(context: Context){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseTodayReminder::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE,intent,0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun cancelAlarm(context: Context){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseTodayReminder::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    fun checkReleaseToday(context: Context):Boolean{
        val intent = Intent(context, ReleaseTodayReminder::class.java)

        return PendingIntent.getBroadcast(context,REQUEST_CODE,intent,PendingIntent.FLAG_NO_CREATE) != null
    }

}