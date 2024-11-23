package com.trex.rexandroidsecureclient.myclient.ui.initdeviceregscreen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trex.rexandroidsecureclient.R

@Composable
fun DeviceRegisterResultScreen(
    isSuccess: Boolean,
    onFinishSetup: (Boolean) -> Unit = {},
) {
    val title =
        remember {
            if (isSuccess) {
                "Device Registered successfully!"
            } else {
                "Device Registration failed"
            }
        }
    val description =
        remember {
            if (isSuccess) {
                "This device has been added to your shop!"
            } else {
                "Device failed to register!\nPlease try again."
            }
        }
    val buttonText =
        remember {
            if (isSuccess) {
                "Finish Setup"
            } else {
                "Retry"
            }
        }

    val iconLogo =
        remember {
            if (isSuccess) {
                Icons.Default.Check
            } else {
                Icons.Default.Clear
            }
        }
    Box(
        modifier =
            Modifier
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Animated check icon in circle
            val infiniteTransition = rememberInfiniteTransition()
            val translateY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -20f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse,
                    ),
                label = "",
            )

            Box(
                modifier =
                    Modifier
                        .size(80.dp)
                        .offset(y = translateY.dp)
                        .background(
                            color = Color.Green,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = iconLogo,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp),
                )
            }

            // Success text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )
            }

            Button(
                onClick = { onFinishSetup(isSuccess) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary)),
            ) {
                Text(
                    buttonText,
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
