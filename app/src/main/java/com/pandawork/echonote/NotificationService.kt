package com.pandawork.echonote

// In NotificationService.kt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

// Our Butler class must be a type of `Service`.
class NotificationService : Service() {

    // This function is called when the service is first started.
    // The `intent` parameter will contain the data sent from our AlarmReceiver (the note).
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // We get the note text from the intent that started this service.
        // `getStringExtra` is used to get a String value.
        // "EXTRA_NOTE" is the key we will use to store the note.
        // We provide a default value "No note found." in case something goes wrong.
        val noteText = intent?.getStringExtra("EXTRA_NOTE") ?: "No note found."

        // We call a helper function to actually create and show the notification.
        showNotification(noteText)

        // This tells the system what to do if it kills our service for memory reasons.
        // `START_NOT_STICKY` means "If you kill me, don't bother restarting me automatically."
        // This is fine for us, as the service's job is done once the notification is shown.
        return START_NOT_STICKY
    }

    private fun showNotification(text: String) {
        val channelId = "echonote_channel"

        // A NotificationManager is the system service that manages all notifications.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // IMPORTANT: For Android 8.0 (Oreo) and higher, you MUST create a Notification Channel.
        // The user can use this channel to control settings for all notifications of this type.
        val channel = NotificationChannel(
            channelId,
            "EchoNote Notifications", // Name the user sees in settings
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Now we build the actual notification's content and appearance.
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // A default icon
            .setContentTitle("Your EchoNote has arrived!")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build() // This creates the Notification object.

        // Finally, we tell the NotificationManager to display it.
        // The `1` is a unique ID for this notification. If we posted another
        // notification with the same ID, it would update the existing one.
        notificationManager.notify(1, notification)
    }

    // This function is required for any Service, but we don't need it for our simple case.
    // It's used for "bound" services, which is a more advanced topic. We just return null.
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}