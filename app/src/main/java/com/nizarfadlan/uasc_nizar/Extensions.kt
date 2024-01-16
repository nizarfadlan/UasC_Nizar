package com.nizarfadlan.uasc_nizar

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object Extensions {
    private const val CHANNEL_ID = "book_app"
    private const val CHANNEL_NAME = "Book App"
    private const val NOTIFICATION_ID = 1001
    private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

    fun Activity.toast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun Context.showNotification(activity: Activity, id: Int, title: String, message: String) {
        createNotificationChannel()
        checkPermission(activity)

        val uniqueNotificationId = NOTIFICATION_ID + id
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_24px)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            checkPermission(activity)
            notify(uniqueNotificationId, builder.build())
        }
    }

    private fun checkPermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED &&
            Build.VERSION_CODES.TIRAMISU == Build.VERSION.SDK_INT
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    private fun Context.createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Todo APP Notification"
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}