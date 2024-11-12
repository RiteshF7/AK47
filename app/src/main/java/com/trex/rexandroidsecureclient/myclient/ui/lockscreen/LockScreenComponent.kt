//package com.trex.rexandroidsecureclient.myclient.ui.lockscreen
//
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun LockScreen(
//    lockReason: String,
//    modifier: Modifier = Modifier,
//) {
//    val uiState by viewModel.uiState
//    var code by remember { mutableStateOf("") }
//
//    Box(
//        modifier =
//            modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.85f)),
//    ) {
//        Column(
//            modifier =
//                Modifier
//                    .fillMaxWidth()
//                    .padding(24.dp)
//                    .align(Alignment.Center),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(24.dp),
//        ) {
//            // Lock Status Card
//            Surface(
//                modifier =
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                shape = RoundedCornerShape(16.dp),
//                color = Color.White.copy(alpha = 0.1f),
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Text(
//                        text = if (uiState.isUnlocked) "DEVICE UNLOCKED!" else "DEVICE LOCKED",
//                        color = if (uiState.isUnlocked) colorResource(com.trex.rexnetwork.R.color.primary) else Color.White,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        letterSpacing = 1.sp,
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//                    if (!uiState.isUnlocked) {
//                        Text(
//                            text = lockReason,
//                            color = Color.White.copy(alpha = 0.9f),
//                            textAlign = TextAlign.Center,
//                            lineHeight = 24.sp,
//                            modifier = Modifier.padding(horizontal = 8.dp),
//                        )
//                    }
//                }
//            }
//            OutlinedTextField(
//                value = code,
//                onValueChange = { code = it },
//                label = { Text("Enter Code") },
//                singleLine = true,
//                enabled = !uiState.isLoading,
//                modifier =
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                colors =
//                    OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color.White,
//                        focusedLabelColor = Color.White,
//                        cursorColor = Color.White,
//                    ),
//            )
//
//            // Unlock Code Button
//            Button(
//                onClick = {
//
//                    viewModel.verifyCode(code)
//                },
//                modifier =
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 32.dp)
//                        .height(56.dp),
//                colors =
//                    ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
//                    ),
//                shape = RoundedCornerShape(28.dp),
//            ) {
//                if (uiState.isLoading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(24.dp),
//                        color = Color.White,
//                    )
//                } else {
//                    Text(
//                        text = if (uiState.isUnlocked) "Finish" else "Unlock with code",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        letterSpacing = 1.sp,
//                    )
//                }
//            }
//        }
//
//        // Footer Text
//        Text(
//            text = "Please contact support for assistance",
//            color = Color.White.copy(alpha = 0.7f),
//            fontSize = 14.sp,
//            modifier =
//                Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(bottom = 32.dp),
//        )
//    }
//}
//
//// Theme color definitions (if needed)
//val LockScreenColors =
//    lightColorScheme(
//        primary = Color(0xFF2196F3),
//        secondary = Color(0xFF03A9F4),
//        tertiary = Color(0xFF00BCD4),
//        background = Color.Black,
//        surface = Color.Black,
//        error = Color(0xFFB00020),
//    )
