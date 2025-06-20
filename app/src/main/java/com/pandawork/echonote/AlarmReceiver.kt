package com.pandawork.echonote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    // The onReceive method is where all our new logic will go.
    override fun onReceive(context: Context, intent: Intent) {
        // Get the note text from the intent that the alarm triggered.
        val noteText = intent.getStringExtra("EXTRA_NOTE") ?: "Your note is here!"

        // Use the context to get the system's Notification Manager.
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // On modern Android (8.0+), all notifications must belong to a "Channel".
        // You should create the channel before posting any notifications.
        val channelId = "echo_note_channel"
        val channel = NotificationChannel(
            channelId,
            "EchoNote Notifications", // Name visible to user in settings
            NotificationManager.IMPORTANCE_HIGH // High importance makes it pop up
        )
        notificationManager.createNotificationChannel(channel)

        // Now we build the notification, just like we did in the old service.
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // An icon is required
            .setContentTitle("Your EchoNote Has Arrived!")
            .setContentText(noteText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Dismiss the notification when the user taps on it
            .build()

        // Finally, we use the notification manager to display the notification.
        // The number `1` is a unique ID for this notification. If you post another
        // notification with the same ID, it will update the existing one.
        notificationManager.notify(1, notification)
    }
}