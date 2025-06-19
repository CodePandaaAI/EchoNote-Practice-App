package com.pandawork.echonote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pandawork.echonote.ui.theme.EchoNoteTheme

class LogActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val received = intent
        val title = received.getStringExtra("SCREEN_TITLE") ?: "EchoNote"
        enableEdgeToEdge()
        setContent {
            EchoNoteTheme {

                LogScreen(title = title)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LogScreen(title: String) {
        Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Logs will appear here...")
            }
        }
    }
}