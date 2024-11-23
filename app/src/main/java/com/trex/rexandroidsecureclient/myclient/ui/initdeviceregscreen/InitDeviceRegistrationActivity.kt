package com.trex.rexandroidsecureclient.myclient.ui.initdeviceregscreen

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.trex.rexandroidsecureclient.R
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.utils.SharedPreferenceManager
import com.trex.rexnetwork.utils.startMyActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

sealed class InitRegUiState {
    data object RegInit : InitRegUiState()

    data object RegSuccess : InitRegUiState()

    data class RegFailed(
        val error: String? = null,
    ) : InitRegUiState()
}

class InitDeviceRegistrationActivity : ComponentActivity() {
    companion object {
        fun go(context: Context) {
            context.startMyActivity(InitDeviceRegistrationActivity::class.java, true)
        }
    }

    private var isInitilized = false
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private val viewModel: InitDeviceRegViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        sharedPreferenceManager = SharedPreferenceManager(this)

        setContent {
            DeviceRegistrationScreen(
                viewModel = viewModel,
                onRetry = { viewModel.initRegistration(this) },
                onFinish = {
                    setResult(RESULT_OK)
                    finish()
                },
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val isDeviceRegCompleted = sharedPreferenceManager.getRegCompleteStatus()
        if (isDeviceRegCompleted == Constants.RESPONSE_RESULT_SUCCESS) {
            viewModel.onRegistrationSuccess()
        }
        if (isDeviceRegCompleted == Constants.RESPONSE_RESULT_FAILED) {
            viewModel.onRegistrationFailed()
        }

        if (!isInitilized) {
            if (isDeviceRegCompleted.isBlank()) {
                viewModel.initRegistration(this)
            }
            isInitilized = true
        }
    }
}

@Composable
fun DeviceRegistrationScreen(
    viewModel: InitDeviceRegViewModel,
    onRetry: () -> Unit,
    onFinish: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.85f))
                    .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            when (val state = uiState) {
                is InitRegUiState.RegInit -> {
                    LoadingIndicator("Initializing Registration...")
                }

                is InitRegUiState.RegSuccess -> {
                    DeviceRegisterResultScreen(true) {
                        onFinish()
                    }
                }

                is InitRegUiState.RegFailed -> {
                    DeviceRegisterResultScreen(false) {
                        onRetry()
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = colorResource(R.color.primary))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, textAlign = TextAlign.Center)
    }
}

// ViewModel remains the same as in previous example
class InitDeviceRegViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<InitRegUiState>(InitRegUiState.RegInit)
    val uiState: StateFlow<InitRegUiState> = _uiState.asStateFlow()

    fun initRegistration(context: Context) {
        val regAction =
            ActionMessageDTO(
                "",
                Actions.ACTION_REG_DEVICE,
                HashMap(),
                false,
                UUID.randomUUID().toString(),
            )
        ActionExecuter(context).sendActionToShop(regAction)
        _uiState.value = InitRegUiState.RegInit
    }

    fun onRegistrationSuccess() {
        _uiState.value = InitRegUiState.RegSuccess
    }

    fun onRegistrationFailed(errorMessage: String? = null) {
        _uiState.value = InitRegUiState.RegFailed(errorMessage)
    }
}
