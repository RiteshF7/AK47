package com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trex.rexandroidsecureclient.R

// Usage in Activity
class UnlockWithCodeActivity : ComponentActivity() {
    private val viewModel: UnlockDeviceViewModel by viewModels()

    private val stopLockTaskReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                stopLockTask()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter(STOP_LOCK_TASK)
        val vm: UnlockDeviceViewModel by viewModels()

        registerReceiver(stopLockTaskReceiver, intentFilter, RECEIVER_NOT_EXPORTED)

        this.startLockTask()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    UnlockScreen(vm) {
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        const val STOP_LOCK_TASK = "STOP_LOCK_TASK"
    }
}

@Composable
fun UnlockScreen(
    vm: UnlockDeviceViewModel,
    onFinish: () -> Unit,
) {
    val uiState by vm.uiState
    var code by remember { mutableStateOf("") }
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.85f)),
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(100.dp))
            LockIcon(uiState)

            if (uiState.isUnlocked) {
                LockInfoCard(
                    "Device Successfully Unlocked",
                    "Your device is now accessible. Please tap the button below to continue.",
                )
            } else {
                LockInfoCard(
                    "Device Access Restricted",
                    "Your device has been locked due to a pending payment. Please complete the payment process to restore full access to your device.",
                )
            }

            if (!uiState.isUnlocked) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Enter Code", color = Color.White) },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                            focusedContainerColor = Color.White.copy(alpha = 0.1f),
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                        ),
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(20.dp))
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                )
            } else {
                if (uiState.isUnlocked) {
                    UnlockButton(
                        text = "Close",
                        icon = Icons.Default.CheckCircle,
                        onClick = onFinish,
                    )
                } else {
                    UnlockButton(text = "Unlock with code", icon = Icons.Default.Done) {
                        vm.verifyCode(code)
                    }
                }
            }
        }
    }
}

@Composable
fun UnlockButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(45.dp)
                .height(40.dp)
                .clickable { onClick() }
                .background(
                    color = colorResource(R.color.primary).copy(alpha = 0.8f),
                    shape = RoundedCornerShape(5.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                tint = Color.White,
                contentDescription = "",
                modifier = Modifier.size(15.dp),
            )
            Spacer(Modifier.padding(6.dp))
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun LockInfoCard(
    title: String,
    desc: String,
) {
    Card(
        Modifier.fillMaxWidth().padding(top = 20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.1f),
                contentColor = Color.White,
            ),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                fontSize = 20.sp,
                text = title,
                fontWeight = FontWeight.Bold,
            )
            Text(
                fontSize = 16.sp,
                modifier = Modifier.padding(10.dp).fillMaxWidth(),
                text = desc,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun LockIcon(uiState: UnlockUiState) {
    if (uiState.isUnlocked) {
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.1f),
                    contentColor = Color.White,
                ),
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "",
                tint = Color(0xFF00C853),
                modifier =
                    Modifier
                        .size(92.dp)
                        .padding(5.dp),
            )
        }
    } else {
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f),
                    contentColor = Color.White,
                ),
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "",
                tint = Color.Red,
                modifier =
                    Modifier
                        .size(92.dp)
                        .padding(5.dp),
            )
        }
    }
}

@Preview
@Composable
fun Pre() {
}
