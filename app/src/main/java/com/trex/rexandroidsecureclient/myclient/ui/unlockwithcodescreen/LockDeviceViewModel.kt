package com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository
import kotlinx.coroutines.launch

// ViewModel
class UnlockDeviceViewModel : ViewModel() {
    private val _uiState = mutableStateOf(UnlockUiState())
    val uiState: State<UnlockUiState> = _uiState
    private val repo = SendActionMessageRepository()

    fun verifyCode(code: String) {
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
                val isSuccess = repo.verifyCode(code)
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
        // Add any additional unlock logic here
    }
}

// UI State
data class UnlockUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUnlocked: Boolean = false,
)
