package id.ridwan.moviecatalogueapp.userInterface.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ridwan.moviecatalogueapp.R
import id.ridwan.moviecatalogueapp.alarm.DailyReminder
import id.ridwan.moviecatalogueapp.alarm.ReleaseTodayReminder
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(toolbarRS)
        supportActionBar?.elevation = 0f
        toolbarRS.setNavigationOnClickListener { finish() }

//        Daily Reminder (7.00 am)
        val dailyReminder = DailyReminder()
        dailySwitch.isChecked = dailyReminder.checkDailyReminder(this)

        dailySwitch.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked){
                dailyReminder.setRepeatingAlarm(this)
            } else {
                dailyReminder.cancelAlarm(this)
            }
        }

//      Today Release Movie (8.00 pm)
        val releaseTodayReminder = ReleaseTodayReminder()
        todaySwitch.isChecked = releaseTodayReminder.checkReleaseToday(this)

        todaySwitch.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked){
                releaseTodayReminder.setRepeatingAlarm(this)

            } else {
                releaseTodayReminder.cancelAlarm(this)
            }

        }
    }

}
