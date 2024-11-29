package com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.compose.material.icons.filled.Call
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trex.rexandroidsecureclient.R
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexandroidsecureclient.myclient.utils.NetworkUtils
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.utils.SharedPreferenceManager

// Usage in Activity
class UnlockWithCodeActivity : ComponentActivity() {
    private val stopLockTaskReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                stopLockTask()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val intentFilter = IntentFilter(STOP_LOCK_TASK)
        val vm: UnlockDeviceViewModel by viewModels()
        vm.initNetworkUtils(NetworkUtils(this))

        registerReceiver(stopLockTaskReceiver, intentFilter, RECEIVER_NOT_EXPORTED)

        this.startLockTask()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    UnlockScreen(vm) {
                        ActionExecuter(this).receiveActionsFromShop(
                            ActionMessageDTO(
                                "",
                                Actions.ACTION_UNLOCK_DEVICE,
                            ),
                        )
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
    val context = LocalContext.current
    val uiState by vm.uiState
    var code by remember { mutableStateOf("") }
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.85f)),
    ) {
        Column(
            Modifier
                .padding(horizontal = 26.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(30.dp))

            LockIcon(uiState)

            if (uiState.isUnlocked) {
                LockInfoCard(
                    "Device Unlocked",
                    "Your device is now accessible. Please tap the button below to continue.",
                )
            } else {
                LockInfoCard(
                    "Device Locked",
                    "Your device has been locked due to a pending payment. Please complete the payment process to restore full access to your device.",
                )
            }

            if (!uiState.isUnlocked || !uiState.isInternetAAvailable) {
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
                            .fillMaxWidth()
                            .padding(top = 20.dp),
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
                    if (!uiState.isInternetAAvailable) {
                        Box(modifier = Modifier.padding(vertical = 20.dp)) {
                            UnlockButton(
                                text = "Connect to WIFI",
                                icon = Icons.Default.CheckCircle,
                                onClick = {
                                    openWifiSettings(context as Activity)
                                },
                            )
                        }
                        Box(modifier = Modifier.padding(vertical = 20.dp)) {
                            UnlockButton(
                                text = "Unlock with offline code",
                                icon = Icons.Default.CheckCircle,
                                onClick = {
                                    val sp = SharedPreferenceManager(context)
                                    val masterCode = sp.getMasterUnlockCode()
                                    vm.unlockWithMasterCode(code, masterCode)
                                },
                            )
                        }
                    } else {
                        UnlockButton(text = "Unlock with code", icon = Icons.Default.Done) {
                            val sp = SharedPreferenceManager(context)
                            sp.getShopId()?.let { shopId ->
                                sp.getDeviceId()?.let { deviceId ->
                                    vm.verifyCode(code, shopId, deviceId)
                                }
                            }
                        }
                    }
                }

                UnlockButton(
                    modifier = Modifier.padding(vertical = 20.dp),
                    text = "Contact Support",
                    icon = Icons.Default.Call,
                ) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.setData(Uri.parse("tel:9910000163"))
                    context.startActivity(intent)
                }
            }
        }
    }
}

fun openWifiSettings(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // For Android 10 and above, use the WiFi panel intent
        val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
        activity.startActivity(panelIntent)
    } else {
        // For older versions, open WiFi settings
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        activity.startActivity(intent)
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
        shape = RoundedCornerShape(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.6f),
                contentColor = Color.White,
            ),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                fontSize = 28.sp, // Increased from 24.sp for more prominent heading
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold, // Kept Bold for heading
            )
            Text(
                fontSize = 14.sp, // Reduced from 16.sp for better contrast with heading
                modifier =
                    Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                text = desc,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal, // Changed from Medium to Normal for better readability
            )
        }
    }
}

@Composable
fun LockIcon(uiState: UnlockUiState) {
    val (icon, color) =
        when (uiState.isUnlocked) {
            true -> Icons.Default.Check to colorResource(R.color.primary)
            false -> Icons.Default.Lock to Color.White
        }
    Card(
        shape = RoundedCornerShape(100.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.8f),
                contentColor = Color.White,
            ),
    ) {
        Box(Modifier.padding(35.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = color,
                modifier =
                    Modifier
                        .size(100.dp),
            )
        }
    }
}

@Preview
@Composable
fun Pre() {
}
