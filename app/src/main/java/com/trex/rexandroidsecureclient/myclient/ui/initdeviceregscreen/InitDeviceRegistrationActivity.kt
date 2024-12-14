package com.trex.rexandroidsecureclient.myclient.ui.initdeviceregscreen

import DeviceInfoUtil
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexandroidsecureclient.R
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.data.NewDevice
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.PermissionHandlerActivity
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore
import com.trex.rexnetwork.domain.firebasecore.firesstore.Shop
import com.trex.rexnetwork.domain.firebasecore.firesstore.ShopFirestore
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

    data class InputState(
        val imei: String = "",
    ) : InitRegUiState()
}

class InitDeviceRegistrationActivity : ComponentActivity() {
    companion object {
        fun go(context: Context) {
            context.startMyActivity(InitDeviceRegistrationActivity::class.java, true)
        }
    }

    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private val viewModel: InitDeviceRegViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        sharedPreferenceManager = SharedPreferenceManager(this)
        PermissionHandlerActivity.go(this)

        setContent {
            DeviceRegistrationScreen(
                viewModel = viewModel,
                onRetry = {
                    viewModel.retry(this)
                },
                onFinish = {
                    setResult(RESULT_OK)
                    finish()
                },
            )
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

@Composable
fun DeviceRegistrationScreen(
    viewModel: InitDeviceRegViewModel,
    onRetry: () -> Unit,
    onFinish: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
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
                is InitRegUiState.InputState -> {
                    ImeiInputScreen(
                        imei = state.imei,
                        onImeiChange = { viewModel.updateImei(it) },
                        onSubmit = { viewModel.initRegistration(context, state.imei) },
                    )
                }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImeiInputScreen(
    imei: String,
    onImeiChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            value = imei,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            onValueChange = onImeiChange,
            label = { Text("Enter IMEI") },
            singleLine = true,
            colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = colorResource(R.color.primary),
                    unfocusedLabelColor = Color.White,
                    focusedLabelColor = colorResource(R.color.primary),
                ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary),
                ),
        ) {
            Text("Register Device")
        }
    }
}

class InitDeviceRegViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<InitRegUiState>(InitRegUiState.InputState())
    val uiState: StateFlow<InitRegUiState> = _uiState.asStateFlow()
    var maxRetryCount = 3

    fun updateImei(imei: String) {
        _uiState.value = InitRegUiState.InputState(imei)
    }

    fun retry(context: Context) {
        if (maxRetryCount < 0) {
            _uiState.value =
                InitRegUiState.RegFailed("Unable to create device please reset device and try again!")
            val devicePolicyManagerGatewayImpl = DevicePolicyManagerGatewayImpl(context)
            if (devicePolicyManagerGatewayImpl.isDeviceOwnerApp) {
                devicePolicyManagerGatewayImpl.clearDeviceOwnerApp({}, {})
                devicePolicyManagerGatewayImpl.removeActiveAdmin({}, {})
            }
        }
        _uiState.value = InitRegUiState.InputState()
        maxRetryCount--
    }

    fun initRegistration(
        context: Context,
        imei: String,
    ) {
        if (imei.isBlank()) {
            _uiState.value = InitRegUiState.RegFailed("IMEI cannot be empty")
            return
        } else if (!imei.matches(Regex("^[0-9]{15}$"))) {
            _uiState.value = InitRegUiState.RegFailed("Invalid IMEI format (should be 15 digits)")
            return
        }
        _uiState.value = InitRegUiState.RegInit
        createDevice(context, imei)
    }

    private fun createDevice(
        context: Context,
        imei: String,
    ) {
        val sharedPreferenceManager = SharedPreferenceManager(context)
        val fcmTokenManager = FCMTokenManager(context, ClientFCMTokenUpdater(context))
        val deviceInfoUtils = DeviceInfoUtil()
        val deviceModel = "${deviceInfoUtils.getManufacturer()} ${deviceInfoUtils.getDeviceModel()}"

        sharedPreferenceManager.getShopId()?.let { shopId ->
            val deviceFirestore = DeviceFirestore(shopId)
            sharedPreferenceManager.getDeviceId()?.let { deviceId ->
                val newDevice = NewDevice()
                newDevice.shopId = shopId
                newDevice.deviceId = deviceId
                newDevice.imeiOne = imei
                newDevice.modelNumber = deviceModel
                newDevice.isLocked = false

                deviceFirestore.createOrUpdateDevice(deviceId, newDevice, {
                    fcmTokenManager.refreshToken { deviceToken ->
                        val regAction =
                            ActionMessageDTO(
                                "",
                                Actions.ACTION_REG_DEVICE,
                                HashMap(),
                                false,
                                UUID.randomUUID().toString(),
                            )
                        ActionExecuter(context).sendActionToShop(regAction)
                    }

                    Toast
                        .makeText(
                            context,
                            "Device registered successfully!",
                            Toast.LENGTH_SHORT,
                        ).show()
                    removeTokenFromBalance(newDevice.deviceId, newDevice.shopId)
                }, {
                    onRegistrationFailed()
                })
            } ?: run { onRegistrationFailed() }
        } ?: run { onRegistrationFailed() }
    }

    fun onRegistrationSuccess() {
        _uiState.value = InitRegUiState.RegSuccess
    }

    fun removeTokenFromBalance(
        tokenId: String,
        shopId: String,
    ) {
        val shopRepo = ShopFirestore()
        shopRepo.getTokenBalanceList(shopId) {
            val consumableTokenList = it.toMutableList()
            val consumableToken = tokenId
            consumableTokenList.remove(consumableToken)
            shopRepo.updateSingleField(
                shopId,
                Shop::tokenBalance.name,
                consumableTokenList,
                {
                    onRegistrationSuccess()
                    Log.i("some", "removeTokenFromBalance: Token consumed!!")
                },
                {
                    onRegistrationFailed()
                    Log.e("TAG", "removeTokenFromBalance: error consuming token!! $it")
                },
            )
        }
    }

    fun onRegistrationFailed(errorMessage: String? = null) {
        _uiState.value = InitRegUiState.RegFailed(errorMessage)
    }
}
