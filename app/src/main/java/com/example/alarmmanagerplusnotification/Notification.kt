package com.example.alarmmanagerplusnotification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// to use this service need to define permission in manifisto as well
// -- for alarm service
//      -- <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
// -- for receiver service
//      -- <applicaiton>
//              <receiver android:name=".Notification"
//                android:enabled="true"/>
//          </applicaiton>


// variable need
const val notificationID = 1
const val channelId = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

// Notification class extends broadcastreaceiver
// whenever it reacive any intent it will generate notification for that task
class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // step 1. need a notification object
        // to get a notification object we use notification builder.
        // it will get notification tittle and message from intent itself.
        val notification = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(intent!!.getStringExtra(titleExtra))
            .setContentText(intent!!.getStringExtra(messageExtra))
            .build()
        // step 2. need a notification manager
        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // step 3. using notification manager to send notification
        manager.notify(notificationID, notification)
    }

}