package com.trex.rexcommon.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class DeviceActions {
    ACTION_LOCK_DEVICE,
    ACTION_UNLOCK_DEVICE,
    ACTION_EMI_AUDIO_REMINDER,
    ACTION_EMI_SCREEN_REMINDER,
    ACTION_GET_PHONE_NUMBER,
    ACTION_GET_CONTACTS,
    ACTION_GET_CONTACTS_VIA_MESSAGE,
    ACTION_OFFLINE_LOCK_UNLOCK,
    ACTION_APP_UNLOCK,
    ACTION_CAMERA_LOCK,
    ACTION_CAMERA_UNLOCK,
    ACTION_SET_WALLPAPER,
    ACTION_REMOVE_WALLPAPER,
    ACTION_GET_LOCATION,
    ACTION_GET_LOCATION_VIA_MESSAGE,
    ACTION_REBOOT_DEVICE,
    ACTION_CALL_LOCK,
    ACTION_CALL_UNLOCK,
    ACTION_RESET_PASSWORD,
    ACTION_REACTIVATE_DEVICE,
    ACTION_DEACTIVATE_DEVICE,
    ACTION_GET_DEVICE_INFO,
    ACTION_GET_UNLOCK_CODE,
    ACTION_REMOVE_DEVICE,
}

object DeviceActionsMapper {
    // Convert enum to string
    fun fromEnumToString(action: DeviceActions): String = action.name

    // Convert string to enum
    fun fromStringToEnum(actionString: String): DeviceActions? =
        try {
            DeviceActions.valueOf(actionString)
        } catch (e: IllegalArgumentException) {
            null // Return null if no matching enum is found
        }
}

@Parcelize
data class SendMessageDto(
    val to: String?,
    val action: DeviceActions,
    val payload: Map<String, String> = emptyMap(),
) : Parcelable

@Parcelize
data class AndroidConfig(
    val priority: String,
    val delay_while_idle: Boolean = false,
) : Parcelable
