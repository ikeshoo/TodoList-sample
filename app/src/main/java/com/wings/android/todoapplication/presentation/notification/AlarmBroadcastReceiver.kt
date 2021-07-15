package com.wings.android.todoapplication.presentation.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.wings.android.todoapplication.R

class AlarmBroadcastReceiver :BroadcastReceiver(){
    companion object{
        const val CHANNEL_ID = "com.wings.android.todoapplication.channel1"
    }
    private lateinit var notificationManager:NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
            val contentText = intent.getStringExtra("content")
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(
                CHANNEL_ID,
                "シンプルToDoリスト Notification Channel",
                "Display the item in the app."
            )
            displayNotification(context, contentText)

    }

    //Notification作成
    private fun displayNotification(context:Context,contentText:String?){
        val notificationId = 1
        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.homeFragment)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("リマインド")
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)

        with(NotificationManagerCompat.from(context)){
            notify(notificationId,notification.build())
        }
    }

    //NotificationChannel作成
    private fun createNotificationChannel(id:String,name:String,channelDescription: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel1 = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel1)
        }
    }
}