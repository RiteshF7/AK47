package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexcommon.data.DeviceActions

class ActionExecuter(
    private val context: Context,
) {
    private val TAG: String = "Action Executor"
    val mDevicePolicyManagerGatewayImpl = DevicePolicyManagerGatewayImpl(context)

    fun execute(action: DeviceActions) {
//        if (!isDeviceOwner) return

        try {
            when (action) {
                DeviceActions.ACTION_LOCK_DEVICE -> lockDevice()
                DeviceActions.ACTION_UNLOCK_DEVICE -> unlockDevice()
                DeviceActions.ACTION_EMI_AUDIO_REMINDER -> playAudioReminder(context)
                DeviceActions.ACTION_EMI_SCREEN_REMINDER -> showScreenReminder()
                DeviceActions.ACTION_GET_PHONE_NUMBER -> getPhoneNumber()
                DeviceActions.ACTION_GET_CONTACTS -> getContacts()
                DeviceActions.ACTION_GET_CONTACTS_VIA_MESSAGE -> getContactsViaMessage()
                DeviceActions.ACTION_OFFLINE_LOCK_UNLOCK -> offlineLockUnlock()
                DeviceActions.ACTION_APP_UNLOCK -> appUnlock()
                DeviceActions.ACTION_CAMERA_LOCK -> cameraLock()
                DeviceActions.ACTION_CAMERA_UNLOCK -> cameraUnlock()
                DeviceActions.ACTION_SET_WALLPAPER -> setWallpaper()
                DeviceActions.ACTION_REMOVE_WALLPAPER -> removeWallpaper()
                DeviceActions.ACTION_GET_LOCATION -> getLocation()
                DeviceActions.ACTION_GET_LOCATION_VIA_MESSAGE -> getLocationViaMessage()
                DeviceActions.ACTION_REBOOT_DEVICE -> rebootDevice()
                DeviceActions.ACTION_CALL_LOCK -> callLock()
                DeviceActions.ACTION_CALL_UNLOCK -> callUnlock()
                DeviceActions.ACTION_RESET_PASSWORD -> resetPassword("000000")
                DeviceActions.ACTION_REACTIVATE_DEVICE -> reactivateDevice()
                DeviceActions.ACTION_DEACTIVATE_DEVICE -> deactivateDevice()
                DeviceActions.ACTION_GET_DEVICE_INFO -> getDeviceInfo()
                DeviceActions.ACTION_GET_UNLOCK_CODE -> getUnlockCode()
                DeviceActions.ACTION_REMOVE_DEVICE -> removeDevice()
            }
        } catch (error: Exception) {
            Log.e("ActionExecuterError", "execute: ${error.message}")
        }
    }

    private fun lockDevice() {
        mDevicePolicyManagerGatewayImpl.lockNow(
            { Log.i("", "lockDevice: Device locked successfully!") },
            { e -> Log.e("ActionExecuterError", "execute: ${e.message}") },
        )
    }

    private fun unlockDevice() {
        // TODO
    }

    private fun playAudioReminder(context: Context) {
        AudioReminderHandler(context).playAudioReminder()
        Log.i("ActionExecuter", "Play audio reminder action triggered")
    }

    private fun showScreenReminder() {
        // Implement screen reminder logic
        Log.i("ActionExecuter", "Show screen reminder action triggered")
    }

    private fun getPhoneNumber(): String = ""

    private fun getContacts() {
        Log.i("ActionExecuter", "Get contacts action triggered")
    }

    private fun getContactsViaMessage() {
        // Implement contact retrieval via message logic
        Log.i("ActionExecuter", "Get contacts via message action triggered")
    }

    private fun offlineLockUnlock() {
        // Implement offline lock/unlock logic
        Log.i("ActionExecuter", "Offline lock/unlock action triggered")
    }

    private fun appUnlock() {
        // Implement app unlock logic
        Log.i("ActionExecuter", "App unlock action triggered")
    }

    private fun cameraLock() {
        mDevicePolicyManagerGatewayImpl.setCameraDisabled(true, {
            Log.i(TAG, "cameraLock: Success")
        }, {
            Log.e(TAG, "cameraLock: Error")
        })
    }

    private fun cameraUnlock() {
        mDevicePolicyManagerGatewayImpl.setCameraDisabled(false, {
            Log.i(TAG, "cameraUnlock: Success")
        }, {
            Log.e(TAG, "cameraUnlock: Error")
        })
    }

    private fun setWallpaper() {
        SetWallpaperHandler(context).handle()
    }

    private fun removeWallpaper() {
        RemoveWallpaperHandler(context).handle()
    }

    private fun getLocation(): String? {
        var locationUrl: String? = null
        GetLocationHandler(context).handle {
            locationUrl = it
        }
        return locationUrl
    }

    private fun getLocationViaMessage() {
        // Implement location retrieval via message logic
        Log.i("ActionExecuter", "Get location via message action triggered")
    }

    private fun rebootDevice() {
//        devicePolicyManager.reboot()
        Log.i("ActionExecuter", "REboot device")
    }

    private fun callLock() {
        // Implement call lock logic
        Log.i("ActionExecuter", "Call lock action triggered")
    }

    private fun callUnlock() {
        // Implement call unlock logic
        Log.i("ActionExecuter", "Call unlock action triggered")
    }

    private fun resetPassword(newPassword: String) {
    }

    private fun reactivateDevice() {
        // Implement device reactivation logic
        Log.i("ActionExecuter", "Reactivate device action triggered")
    }

    private fun deactivateDevice() {
        // Implement device deactivation logic
        Log.i("ActionExecuter", "Deactivate device action triggered")
    }

    private fun getDeviceInfo(): String {
        // Implement device info retrieval logic
        Log.i("ActionExecuter", "Get device info action triggered")
        return "Device Info"
    }

    private fun getUnlockCode(): String {
        // Implement unlock code retrieval logic
        Log.i("ActionExecuter", "Get unlock code action triggered")
        return "Unlock Code"
    }

    private fun removeDevice() {
        // Implement device removal logic
        Log.i("ActionExecuter", "Remove device action triggered")
    }
}
