package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.content.Intent
import android.os.UserManager
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers.RegisterDeviceHandler
import com.trex.rexandroidsecureclient.myclient.ui.emireminderscreen.EmiReminderActivity
import com.trex.rexandroidsecureclient.myclient.ui.lockappscreen.LockAppActivity
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.FcmResponseManager

class ActionExecuter(
    private val context: Context,
) {
    private val TAG: String = "Action Executor"

    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun execute(message: ActionMessageDTO) {
        try {
            when (message.action) {
                Actions.ACTION_GET_PHONE_NUMBER -> getPhoneNumber()
                Actions.ACTION_GET_CONTACTS -> getContacts()
                Actions.ACTION_GET_CONTACTS_VIA_MESSAGE -> getContactsViaMessage()
                Actions.ACTION_GET_LOCATION -> getLocation()
                Actions.ACTION_GET_LOCATION_VIA_MESSAGE -> getLocationViaMessage()
                Actions.ACTION_GET_DEVICE_INFO -> getDeviceInfo()
                Actions.ACTION_GET_UNLOCK_CODE -> getUnlockCode()
                Actions.ACTION_LOCK_DEVICE -> lockDevice()
                Actions.ACTION_UNLOCK_DEVICE -> unlockDevice()
                Actions.ACTION_EMI_AUDIO_REMINDER -> playAudioReminder()
                Actions.ACTION_EMI_SCREEN_REMINDER -> showScreenReminder()
                Actions.ACTION_OFFLINE_LOCK -> offlineLock()
                Actions.ACTION_OFFLINE_UNLOCK -> offlineUnlock()
                Actions.ACTION_APP_UNLOCK -> appUnlock()
                Actions.ACTION_CAMERA_LOCK -> cameraLock()
                Actions.ACTION_CAMERA_UNLOCK -> cameraUnlock()
                Actions.ACTION_SET_WALLPAPER -> setWallpaper()
                Actions.ACTION_REMOVE_WALLPAPER -> removeWallpaper()
                Actions.ACTION_REBOOT_DEVICE -> rebootDevice()
                Actions.ACTION_CALL_LOCK -> callLock()
                Actions.ACTION_CALL_UNLOCK -> callUnlock()
                Actions.ACTION_RESET_PASSWORD -> resetPassword("000000")
//                Actions.ACTION_REACTIVATE_DEVICE -> reactivateDevice()
//                Actions.ACTION_DEACTIVATE_DEVICE -> deactivateDevice()

                Actions.ACTION_REMOVE_DEVICE -> removeDevice()
                Actions.ACTION_LOCK_SCREEN -> {
                    mDevicePolicyManagerGateway.lockNow({}, {})
                }

                Actions.ACTION_REG_DEVICE -> {
                    registerDeviceUsingFCM(message)
                }

                Actions.ACTION_REG_DEVICE_COMPLETED -> {
                    FcmResponseManager.handleResponse(message.requestId, message)
                }
            }
        } catch (error: Exception) {
            Log.e("ActionExecuterError", "execute: ${error.message}")
        }
    }

    private fun lockDevice() {
        mDevicePolicyManagerGateway.setLockTaskPackages(arrayOf(context.packageName), {
            val kioskIntent = Intent(context, LockAppActivity::class.java)
            kioskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(kioskIntent)
            Log.i(TAG, "lockDevice: Success")
        }, { error ->
            Log.i(TAG, "lockDevice: error :: ${error.message}")
        })
    }

    private fun registerDeviceUsingFCM(message: ActionMessageDTO) {
        RegisterDeviceHandler(context).handle(message)
    }

    private fun unlockDevice() {
        val stopLockTaskIntent = Intent(LockAppActivity.STOP_LOCK_TASK)
        context.sendBroadcast(stopLockTaskIntent)
    }

    private fun playAudioReminder() {
        AudioReminderHandler(context).playAudioReminder()
        Log.i("ActionExecuter", "Play audio reminder action triggered")
    }

    private fun showScreenReminder() {
        val emiReminderActivity = Intent(context, EmiReminderActivity::class.java)
        emiReminderActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(emiReminderActivity)
    }

    private fun getPhoneNumber() {
        GetPhoneNumberHandler(context).handle()
    }

    private fun getContacts() {
        GetContactsHandler(context).handle()
    }

    private fun getContactsViaMessage() {
        GetContactsViaMessageHandler(context).handle()
    }

    private fun offlineLock() {
        lockDevice()
    }

    private fun offlineUnlock() {
        unlockDevice()
    }

    private fun appUnlock() {
        // Implement app unlock logic
        Log.i("ActionExecuter", "App unlock action triggered")
    }

    private fun disableCam(boolean: Boolean) {
        val state = if (boolean) "enabled" else "disabled"
        mDevicePolicyManagerGateway.setCameraDisabled(
            boolean,
            {
                Log.i(TAG, "camera$state: Success")
            },
            {
                Log.e(TAG, "camera$state: Error")
            },
        )
    }

    private fun cameraLock() = disableCam(true)

    private fun cameraUnlock() = disableCam(false)

    private fun setWallpaper() {
        SetWallpaperHandler(context).handle()
    }

    private fun removeWallpaper() {
        RemoveWallpaperHandler(context).handle()
    }

    private fun getLocation() {
        GetLocationHandler(context).handle()
    }

    private fun getLocationViaMessage() {
        // check
    }

    private fun rebootDevice() {
        mDevicePolicyManagerGateway.reboot(
            {
                Log.i(TAG, "rebootDevice: rebooting!")
            },
            { error -> Log.i(TAG, "rebootDevice: ${error.message}") },
        )
    }

    private fun callLock() {
        mDevicePolicyManagerGateway.setUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS, true)
        Log.i("ActionExecuter", "Call lock action triggered")
    }

    private fun callUnlock() {
        mDevicePolicyManagerGateway.setUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS, true)
        Log.i("ActionExecuter", "Call unlock action triggered")
    }

    private fun resetPassword(newPassword: String) {
        // check last
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
