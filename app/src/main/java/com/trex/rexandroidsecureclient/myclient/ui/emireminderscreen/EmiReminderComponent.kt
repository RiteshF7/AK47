package com.trex.rexandroidsecureclient.myclient.ui.emireminderscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(
    amount: Double,
    dueDate: String,
    onDismiss: () -> Unit,
) {
    val urgentRed = Color(0xFFD32F2F)
    val warningYellow = Color(0xFFFFB74D)
    val darkOverlay = Color(0xFF1A1A1A)

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(darkOverlay)
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Pulsating Warning Icon
            Box(
                modifier =
                    Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(urgentRed.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning Icon",
                    tint = warningYellow,
                    modifier = Modifier.size(108.dp),
                )
            }

            Text(
                text = "URGENT PAYMENT REQUIRED",
                color = urgentRed,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            // Card for description with warning border
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = darkOverlay.copy(alpha = 0.9f),
                    ),
                border = BorderStroke(2.dp, urgentRed),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "IMMEDIATE ACTION REQUIRED",
                        color = warningYellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )

                    if (amount != 0.0 && dueDate.isNotBlank()) {
                        Text(
                            text = "₹${amount.toInt()}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = "Due Date: $dueDate",
                            color = urgentRed,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "⚠️ YOUR DEVICE WILL BE AUTOMATICALLY LOCKED IF PAYMENT FOR YOUR PHONE EMI IS NOT RECEIVED BY THE DUE DATE  ⚠️",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Button(
                onClick = onDismiss,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = urgentRed,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
            ) {
                Text(
                    text = "I Understand - Remind Later",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
