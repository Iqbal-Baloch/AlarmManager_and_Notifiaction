package com.example.alarmmanagerplusnotification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.getLongDateFormat
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.alarmmanagerplusnotification.databinding.ActivityMainBinding
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        binding.submitButtion.setOnClickListener{scheduleNotification()}
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = binding.titleET.text.toString();
        val message = binding.messageET.text.toString();
        intent.putExtra(titleExtra, title);
        intent.putExtra(messageExtra, message);
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        // step no need alarm manager to use service
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager ;
        val time = getTime()
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        showAlert(time, title, message)

    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext) ;
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext) ;

        AlertDialog.Builder(this)
            .setTitle("notification Scheduled")
            .setMessage("title: "+title+
                    "\n message: "+message+
                    "\n , At"+dateFormat.format(date)+
                    " "+ timeFormat.format(date))
            .setPositiveButton("okey"){_,_ -> }
            .show()
    }


    private fun getTime(): Long {
        // Create a Calendar instance and set it to the current time
        val day = binding.datepicker.dayOfMonth
        val month = binding.datepicker.month
        val year = binding.datepicker.year

        // Create a Calendar instance and set it to the selected time
        val calendar = Calendar.getInstance()
        binding.timepicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            // Update the Calendar instance with the selected time
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        }
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        // Return the time in milliseconds
        return calendar.timeInMillis
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "custom notification"
        val message = "here is my custom notification for you"
        val importance = NotificationManager.IMPORTANCE_DEFAULT ;

        val channel = NotificationChannel(channelId, name, importance);
        channel.description = message ;
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}