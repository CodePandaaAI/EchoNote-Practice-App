package com.pandawork.echonote

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        enableEdgeToEdge()
        setContent {
            EchoNoteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreenUi(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreenUi(modifier: Modifier) {
    // In MainActivity.kt inside the setContent block
    // Use `remember` to have Compose keep track of the text field's state
    var noteText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }
    val context = LocalContext.current // Get the context for Intents

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
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

        Button(onClick = { /* We'll add this logic later */ }) {
            Text("Set EchoNote")
        }

        Button(onClick = {
            // This is the Explicit Intent, same logic, new home!
            // It lives inside the onClick lambda of a Composable button.
            val intent = Intent(context, LogActivity::class.java)
            intent.putExtra("SCREEN_TITLE", "Charger Status Log")
            context.startActivity(intent)
        }) {
            Text("View Charger Log")
        }

        // In MainActivity.kt, inside the Column composable

        Button(onClick = {
            // This is the Implicit Intent logic. It's exactly the same as before.
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, noteText) // Use the state variable
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share this note via...")

            // Execute the intent
            context.startActivity(shareIntent)
        }) {
            Text("Share Note")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview(modifier: Modifier = Modifier) {
    EchoNoteTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            MainScreenUi(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}