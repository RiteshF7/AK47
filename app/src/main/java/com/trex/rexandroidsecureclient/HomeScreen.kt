package com.trex.rexandroidsecureclient

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.domain.firebasecore.firesstore.MasterCodeFirestore
import kotlinx.coroutines.delay

sealed class HomeScreenUiState {
    object Init : HomeScreenUiState()

    data class Success(
        val message: String,
    ) : HomeScreenUiState()

    data class Failed(
        val message: String,
    ) : HomeScreenUiState()
}

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val vm by viewModels<HomeScreenViewModel>()
        setContent {
            RecoveryScreen(vm)
        }
    }
}

@Composable
fun RecoveryScreen(viewModel: HomeScreenViewModel) {
    var masterCode by remember { mutableStateOf("") }
    //turn on this to set initial data
    var showInitButton by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.85f))
                .padding(horizontal = 16.dp, vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_enterprise_blue), // Make sure to add your logo
            contentDescription = "App Logo",
            modifier =
                Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp),
        )

        // Master Code Input
        OutlinedTextField(
            value = masterCode,
            onValueChange = { masterCode = it },
            label = { Text("Enter Master Code") },
            isError = viewModel.uiState.value is HomeScreenUiState.Failed,
            supportingText = {
                if (viewModel.uiState.value is HomeScreenUiState.Failed) {
                    Text(
                        text = (viewModel.uiState.value as HomeScreenUiState.Failed).message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = colorResource(R.color.primary),
                    unfocusedBorderColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedPlaceholderColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.50f),
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
        )

        // Execute Button
        Button(
            onClick = {
                viewModel.execMasterCode(masterCode, context)
                if (viewModel.uiState.value is HomeScreenUiState.Success) {
                    showSuccessMessage = true
                }
            },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black,
                ),
            shape = RoundedCornerShape(8.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
        ) {
            Text(
                text = "Execute Code",
                fontWeight = FontWeight.Bold,
            )
        }

        // Success Message
        if (showSuccessMessage) {
            LaunchedEffect(showSuccessMessage) {
                delay(10000)
                showSuccessMessage = false
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.success), // Add check circle icon
                    contentDescription = "Success",
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = (viewModel.uiState.value as? HomeScreenUiState.Success)?.message ?: "",
                    color = Color.Green,
                )
            }
        }

        // Generate Initial Data Button
        AnimatedVisibility(
            visible = showInitButton,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Button(
                onClick = {
                    viewModel.genInitData()
                    showInitButton = false
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                shape = RoundedCornerShape(8.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(top = 16.dp),
            ) {
                Text(
                    text = "Generate Initial Data",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

class HomeScreenViewModel : ViewModel() {
    private val masterCodeRepo = MasterCodeFirestore()
    private val _uiState = mutableStateOf<HomeScreenUiState>(HomeScreenUiState.Init)
    val uiState: State<HomeScreenUiState> = _uiState

    fun execMasterCode(
        masterCode: String,
        context: Context,
    ) {
        masterCodeRepo.getMasterCodeAction(masterCode, { action ->
            val actionMessageDto = ActionMessageDTO("", action)
            ActionExecuter(context).receiveActionsFromShop(actionMessageDto)
            _uiState.value = HomeScreenUiState.Success("Action completed!\nAction name : $action")
        }, {
            _uiState.value =
                HomeScreenUiState.Failed("Invalid master code!\nPlease enter correct master code!")
            Toast.makeText(context, "Invalid master code", Toast.LENGTH_SHORT).show()
        })
    }

    fun genInitData() {
        masterCodeRepo.genrateInitialData()
    }
}
