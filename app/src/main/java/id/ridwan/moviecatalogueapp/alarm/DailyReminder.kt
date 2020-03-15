package id.ridwan.moviecatalogueapp.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.userInterface.home.MainActivity
import java.util.*

class DailyReminder : BroadcastReceiver() {

    private val REQUEST_CODE_DAILY = 999

    override fun onReceive(context: Context?, intent: Intent?) {
       val title = context?.getString(R.string.dailyReminder)
        val message = context?.getString(R.string.DailyMassage)
        val notifId = REQUEST_CODE_DAILY

        sendNotification(context,title,message,notifId)
    }

    private fun sendNotification(context: Context?, title:String?, message:String?, id:Int){
        val CHANNEL_ID = context?.getString(R.string.chDailyID)
        val CHANNEL_NAME = context?.getString(R.string.chDailyName)

        val notifIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,notifIntent,0)

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifBuilder = NotificationCompat.Builder(context,CHANNEL_ID!!)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_play_circle_filled_black_24dp)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setSound(alarmSoundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)

            notifBuilder.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = notifBuilder.build()
        notificationManager.notify(id,notification)
    }

    fun setRepeatingAlarm(context: Context){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,DailyReminder::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_DAILY,intent,0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun cancelAlarm(context: Context){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_DAILY, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    fun checkDailyReminder(context: Context):Boolean{
        val intent = Intent(context, DailyReminder::class.java)

        return PendingIntent.getBroadcast(context,REQUEST_CODE_DAILY,intent,PendingIntent.FLAG_NO_CREATE) != null
    }
}