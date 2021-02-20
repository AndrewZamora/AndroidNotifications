package com.example.androidnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
//    https://developer.android.com/training/notify-user/build-notification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        findViewById<Button>(R.id.button_regular).setOnClickListener{ view ->
            createRegularNotification("Regular Notification Title","Regular Notification Text", "Default")
        }
        findViewById<Button>(R.id.button_custom).setOnClickListener{ view ->
            createCustomNotification()
        }
    }

    private fun createNotificationChannel() {
        val name = "Default"
        val descriptionText = "Default description text"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Default", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createRegularNotification(contentTitle: String, contentText: String, channelId: String) {
        //https://material.io/design/platform-guidance/android-notifications.html#anatomy-of-a-notification
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contentText)
                .setContentText(contentTitle)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        // Not the best way to create a unique ID but a unique ID is needed to prevent the last notification from being overwritten
        val uniqueId = Random(System.currentTimeMillis()).nextInt(1000)
        notificationManager.notify(uniqueId, notificationBuilder.build())
    }

    private  fun createCustomNotification() {
        val notificationLayoutExpanded = RemoteViews(packageName, R.layout.custom_notification_large)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, "Default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
        val uniqueId = Random(System.currentTimeMillis()).nextInt(1000)
        notificationManager.notify(uniqueId, notificationBuilder.build())
    }
}