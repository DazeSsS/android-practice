package com.example.core.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.core.R as notificationsR

class NotificationsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val text = intent.getStringExtra(NOTIFICATION_KEY).orEmpty()

        notificationManager.sendNotification(
            messageBody = text,
            applicationContext = context,
        )
    }

    private fun NotificationManager.sendNotification(
        messageBody: String,
        applicationContext: Context
    ) {
        val activityIntent = Intent().apply {
            action = applicationContext.packageName + ".OPEN_MAIN"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(notificationsR.string.notifications_channel_id)
        )

        builder.setSmallIcon(notificationsR.drawable.ic_notification)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

        notify(0, builder.build())
    }

    companion object {
        const val NOTIFICATION_KEY: String = "NOTIFICATION"
    }
}