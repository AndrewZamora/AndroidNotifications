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
//        https://material.io/design/platform-guidance/android-notifications.html#anatomy-of-a-notification
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contentText)
                .setContentText(contentTitle)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Bacon ipsum dolor amet ball tip hamburger corned beef, turkey fatback beef cow strip steak venison meatloaf bacon pastrami. Frankfurter ball tip sausage, andouille beef shankle chicken drumstick. Ball tip flank bacon, pig cow leberkas frankfurter alcatra drumstick rump tri-tip turkey fatback picanha. Meatloaf buffalo pork beef pastrami cupim porchetta flank meatball prosciutto turkey beef ribs frankfurter pork loin. T-bone cow venison pig, beef ribs chislic turducken. Swine pig pastrami corned beef picanha cupim. Pork loin chislic venison pork salami jerky pork chop beef turducken ball tip porchetta filet mignon chicken rump turkey."))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        // Not the best way to create a unique ID but a unique ID is needed to prevent the last notification from being overwritten
        val uniqueId = Random(System.currentTimeMillis()).nextInt(1000)
        notificationManager.notify(uniqueId, notificationBuilder.build())
    }
}