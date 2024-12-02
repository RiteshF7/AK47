package com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trex.rexandroidsecureclient.myclient.utils.NetworkUtils
import com.trex.rexnetwork.domain.firebasecore.firesstore.ShopFirestore
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository
import kotlinx.coroutines.launch

// ViewModel
class UnlockDeviceViewModel : ViewModel() {
    private val _uiState = mutableStateOf(UnlockUiState())
    val uiState: State<UnlockUiState> = _uiState
    private val repo = SendActionMessageRepository()
    private val shopRepo = ShopFirestore()

    fun initNetworkUtils(networkUtils: NetworkUtils) {
        viewModelScope.launch {
            networkUtils.observeConnectivity().collect { isConnected ->
                _uiState.value = _uiState.value.copy(isInternetAAvailable = isConnected)
            }
        }
    }

    fun updateShopNumber(shopId: String) {
        shopRepo.getShopById(shopId, { shop ->
            _uiState.value = _uiState.value.copy(shopPhoneNumber = shop.shopPhoneNumber)
        }, {})
    }

    fun verifyCode(
        code: String,
        shopId: String,
        deviceId: String,
    ) {
        if (code.isBlank()) {
            _uiState.value =
                _uiState.value.copy(
                    error = "Please enter a code",
                    isLoading = false,
                )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val isSuccess = repo.verifyCode(code, shopId, deviceId)
                if (isSuccess) {
                    handleSuccessfulUnlock()
                } else {
                    _uiState.value =
                        _uiState.value.copy(
                            error = "Incorrect code. Please try again.",
                            isLoading = false,
                        )
                }
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        error = "Network error. Please check your connection.",
                        isLoading = false,
                    )
            }
        }
    }

    private fun handleSuccessfulUnlock() {
        _uiState.value =
            _uiState.value.copy(
                isUnlocked = true,
                isLoading = false,
            )
    }

    fun unlockWithMasterCode(
        enteredCode: String,
        offlineCode: String,
    ) {
        if (enteredCode == offlineCode) {
            handleSuccessfulUnlock()
        } else {
            _uiState.value =
                _uiState.value.copy(
                    error = "Incorrect code. Please try again.",
                    isLoading = false,
                )
        }
    }
}

// UI State
data class UnlockUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUnlocked: Boolean = false,
    val isInternetAAvailable: Boolean = false,
    val shopPhoneNumber: String = "",
)
