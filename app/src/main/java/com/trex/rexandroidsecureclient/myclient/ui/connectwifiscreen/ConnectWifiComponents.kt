package com.trex.rexandroidsecureclient.myclient.ui.connectwifiscreen

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ConnectWifiComponents {
    @Composable
    fun WifiConnectionScreen() {
        var ssid by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var showPassword by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Wifi Icon
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "WiFi Icon",
                    tint = Color(0xFF4CAF50),
                    modifier =
                        Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(Color.DarkGray.copy(alpha = 0.3f))
                            .padding(16.dp),
                )

                Text(
                    text = "Connect to WiFi",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                // SSID TextField
                OutlinedTextField(
                    value = ssid,
                    onValueChange = { ssid = it },
                    label = { Text("WiFi Name (SSID)", color = Color.White.copy(alpha = 0.7f)) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color.DarkGray,
                            focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
                        ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                )

                // Password TextField
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.White.copy(alpha = 0.7f)) },
                    visualTransformation =
                        if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector =
                                    if (showPassword) {
                                        Icons.Default.Menu
                                    } else {
                                        Icons.Default.Menu
                                    },
                                contentDescription = "Toggle password visibility",
                                tint = Color.White.copy(alpha = 0.7f),
                            )
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color.DarkGray,
                            focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
                        ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Connect Button
                Button(
                    onClick = { connectToWifi(context, ssid, password) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                        ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = "Connect",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                    )
                }
            }
        }
    }

    private fun connectToWifi(
        context: Context,
        ssid: String,
        password: String,
    ) {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 and above
                val suggestion =
                    WifiNetworkSuggestion
                        .Builder()
                        .setSsid(ssid)
                        .setWpa2Passphrase(password)
                        .build()

                val suggestions = listOf(suggestion)
                wifiManager.addNetworkSuggestions(suggestions)
            } else {
                // For older Android versions
                val conf = android.net.wifi.WifiConfiguration()
                conf.SSID = "\"" + ssid + "\""
                conf.preSharedKey = "\"" + password + "\""

                wifiManager.addNetwork(conf)
                wifiManager.disconnect()
                wifiManager.enableNetwork(conf.networkId, true)
                wifiManager.reconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
