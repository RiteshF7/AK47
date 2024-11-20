package com.trex.rexandroidsecureclient.myclient.ui.emireminderscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

class EmiReminderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    EmiReminderScreen()
                }
            }
        }
    }

    @Composable
    fun EmiReminderScreen() {
        Box(
            Modifier
                .background(color = Color.Black.copy(alpha = 0.85f))
                .fillMaxSize(),
        ) {
            EmiReminderComponents().MainScreen(2.0, "11/20/2040") {
            }
        }
    }

    @Preview
    @Composable
    fun preview() {
        EmiReminderScreen()
    }
}
