package com.pandawork.echonote

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

// In ChargerReceiver.kt
// To create a broadcast receiver, your class MUST inherit from the BroadcastReceiver class.
// This gives it all the special properties of a receiver.
class ChargerReceiver : BroadcastReceiver() {

    // This is the ONLY method you are required to implement.
    // `onReceive` is the code that automatically runs when your receiver hears an
    // announcement that it was tuned to.
    override fun onReceive(context: Context, intent: Intent) {

        // The `intent` parameter here is the announcement itself! It's the broadcast from the system.
        // We need to check WHAT the announcement was about.
        // The `intent.action` is a String that identifies the event.
        val action = intent.action

        // We check if the action string matches the one for "Power Connected".
        // `Intent.ACTION_POWER_CONNECTED` is a constant value provided by Android
        // that equals "android.intent.action.ACTION_POWER_CONNECTED".
        if (action == Intent.ACTION_POWER_CONNECTED) {
            // Our reaction: For now, we will just print a debug message.
            // `Log.d()` prints a message to a special window in Android Studio called Logcat.
            // It's perfect for testing and debugging.
            // The first part, "ChargerReceiver", is a TAG so we can easily filter and find our messages.
            Log.d("ChargerReceiver", "Power has been CONNECTED.")
        }
        // We can also check for the "Power Disconnected" announcement.
        else if (action == Intent.ACTION_POWER_DISCONNECTED) {
            Log.d("ChargerReceiver", "Power has been DISCONNECTED.")
        }
    }
}