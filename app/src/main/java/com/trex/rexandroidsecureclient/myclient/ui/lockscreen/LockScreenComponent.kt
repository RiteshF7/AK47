package com.trex.rexandroidsecureclient.myclient.ui.lockscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LockScreen(
    deviceName: String,
    lockReason: String,
    onGetUnlockCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Animation for the lock icon pulse effect
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse,
            ),
    )

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Lock Icon with pulse animation
            Box(
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Lock,
                    contentDescription = "Lock Icon",
                    modifier =
                        Modifier
                            .size(64.dp),
                    tint = Color.White,
                )
            }

            // Device Name
            Text(
                text = deviceName,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
            )

            // Lock Status Card
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.1f),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "DEVICE LOCKED",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = lockReason,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }
            }

            // Unlock Code Button
            Button(
                onClick = onGetUnlockCode,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(56.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    ),
                shape = RoundedCornerShape(28.dp),
            ) {
                Text(
                    text = "GET UNLOCK CODE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                )
            }
        }

        // Footer Text
        Text(
            text = "Please contact support for assistance",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
        )
    }
}

@Preview
@Composable
fun LockScreenPreview() {
    MaterialTheme {
        LockScreen(
            deviceName = "Samsung Galaxy S21",
            lockReason = "This device has been locked due to payment default. Please make the necessary payment to unlock the device.",
            onGetUnlockCode = {},
        )
    }
}

// Theme color definitions (if needed)
val LockScreenColors =
    lightColorScheme(
        primary = Color(0xFF2196F3),
        secondary = Color(0xFF03A9F4),
        tertiary = Color(0xFF00BCD4),
        background = Color.Black,
        surface = Color.Black,
        error = Color(0xFFB00020),
    )

// Usage example:
@Composable
fun LockScreen() {
    MaterialTheme(
        colorScheme = LockScreenColors,
    ) {
        LockScreen(
            deviceName = "Samsung Galaxy S21",
            lockReason = "This device has been locked due to payment default. Please contact your service provider for assistance.",
            onGetUnlockCode = {
                // Handle unlock code request
            },
        )
    }
}
