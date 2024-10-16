package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import TelephonyHelper
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexcommon.data.DeviceActions

class ActionExecuter(
    private val context: Context,
) {
    private val isDeviceOwner = MyApplication.isDeviceOwner()
    private val devicePolicyManager: DevicePolicyManager = MyApplication.getDevicePolicyManager()
    private val telephonyHelper = TelephonyHelper(context)

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
        devicePolicyManager.lockNow()
    }

    private fun unlockDevice() {
        // Note: Direct unlocking is not possible through DevicePolicyManager
        // You might need to reset the password to a known value
        UnlockDeviceHandler().handle()
        Log.i("ActionExecuter", "Unlock device action triggered")
    }

    private fun playAudioReminder(context: Context) {
        AudioReminderHandler(context).playAudioReminder()
        Log.i("ActionExecuter", "Play audio reminder action triggered")
    }

    private fun showScreenReminder() {
        // Implement screen reminder logic
        Log.i("ActionExecuter", "Show screen reminder action triggered")
    }

    private fun getPhoneNumber(): String?  {
        return null
    }

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
        devicePolicyManager.setCameraDisabled(null, true)
    }

    private fun cameraUnlock() {
        devicePolicyManager.setCameraDisabled(null, false)
    }

    private fun setWallpaper() {
        // Implement wallpaper setting logic
        Log.i("ActionExecuter", "Set wallpaper action triggered")
    }

    private fun removeWallpaper() {
        // Implement wallpaper removal logic
        Log.i("ActionExecuter", "Remove wallpaper action triggered")
    }

    private fun getLocation() {
        // Implement location retrieval logic
        Log.i("ActionExecuter", "Get location action triggered")
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
        devicePolicyManager.resetPassword(newPassword, 0)
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
