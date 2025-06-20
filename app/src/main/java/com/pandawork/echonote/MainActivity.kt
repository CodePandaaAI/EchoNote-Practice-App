package com.pandawork.echonote

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pandawork.echonote.ui.theme.EchoNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EchoNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EchoNoteScreen()
                }
            }
        }
    }
}

@Composable
fun EchoNoteScreen() {
    var noteText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }
    val context = LocalContext.current

    // --- NEW: Launcher for Notification Permission ---
    // This is the modern way to ask for permissions in Compose.
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                // You can show a message if permission is denied.
                Toast.makeText(
                    context,
                    "Notifications will not be shown without permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    // This code runs once when the Composable is first displayed.
    LaunchedEffect(key1 = true) {
        // TIRAMISU is Android 13
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
    // --- End of New Permission Logic ---


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("Your future note") }
        )
        OutlinedTextField(
            value = timeText,
            onValueChange = { timeText = it },
            label = { Text("Delay in seconds") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            fun setTheAlarm() {
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("EXTRA_NOTE", noteText)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                val timeInSeconds = timeText.toLongOrNull() ?: 0
                val futureInMillis = System.currentTimeMillis() + (timeInSeconds * 1000)

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent)
                Toast.makeText(context, "EchoNote set!", Toast.LENGTH_LONG).show()
            }

            if (alarmManager.canScheduleExactAlarms()) {
                setTheAlarm()
            } else {
                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also {
                    Toast.makeText(context, "Permission needed for alarms.", Toast.LENGTH_SHORT)
                        .show()
                    context.startActivity(it)
                }
            }
        }) {
            Text("Set EchoNote")
        }

        // The other buttons can remain as they were.
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    EchoNoteTheme {
        EchoNoteScreen()
    }
}