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
import com.trex.rexnetwork.data.Actions.ACTION_APP_UNLOCK
import com.trex.rexnetwork.data.Actions.ACTION_CALL_LOCK
import com.trex.rexnetwork.data.Actions.ACTION_CALL_UNLOCK
import com.trex.rexnetwork.data.Actions.ACTION_CAMERA_LOCK
import com.trex.rexnetwork.data.Actions.ACTION_CAMERA_UNLOCK
import com.trex.rexnetwork.data.Actions.ACTION_EMI_AUDIO_REMINDER
import com.trex.rexnetwork.data.Actions.ACTION_EMI_SCREEN_REMINDER
import com.trex.rexnetwork.data.Actions.ACTION_GET_CONTACTS
import com.trex.rexnetwork.data.Actions.ACTION_GET_CONTACTS_VIA_MESSAGE
import com.trex.rexnetwork.data.Actions.ACTION_GET_DEVICE_INFO
import com.trex.rexnetwork.data.Actions.ACTION_GET_LOCATION
import com.trex.rexnetwork.data.Actions.ACTION_GET_LOCATION_VIA_MESSAGE
import com.trex.rexnetwork.data.Actions.ACTION_GET_PHONE_NUMBER
import com.trex.rexnetwork.data.Actions.ACTION_GET_UNLOCK_CODE
import com.trex.rexnetwork.data.Actions.ACTION_LOCK_DEVICE
import com.trex.rexnetwork.data.Actions.ACTION_LOCK_SCREEN
import com.trex.rexnetwork.data.Actions.ACTION_OFFLINE_LOCK
import com.trex.rexnetwork.data.Actions.ACTION_OFFLINE_UNLOCK
import com.trex.rexnetwork.data.Actions.ACTION_REBOOT_DEVICE
import com.trex.rexnetwork.data.Actions.ACTION_REG_DEVICE
import com.trex.rexnetwork.data.Actions.ACTION_REMOVE_DEVICE
import com.trex.rexnetwork.data.Actions.ACTION_REMOVE_WALLPAPER
import com.trex.rexnetwork.data.Actions.ACTION_RESET_PASSWORD
import com.trex.rexnetwork.data.Actions.ACTION_SET_WALLPAPER
import com.trex.rexnetwork.data.Actions.ACTION_UNLOCK_DEVICE
import com.trex.rexnetwork.domain.firebasecore.firesstore.FCMTokenFirestore
import com.trex.rexnetwork.utils.SharedPreferenceManager
import com.trex.rexnetwork.utils.isGetRequest

class ActionExecuter(
    private val context: Context,
) {
    private val TAG: String = "Action Executor"
    private val fcmFirestore = FCMTokenFirestore()
    private val sharedPreferenceManager = SharedPreferenceManager(context)

    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun sendActionToShop(message: ActionMessageDTO) {
        val action = message.action
        when {
            action == ACTION_REG_DEVICE -> {
                registerDeviceUsingFCM(message)
            }
        }
    }

    fun receiveResponseFromShop(message: ActionMessageDTO) {
        val action = message.action
        if (action.isGetRequest()) {
            when {
                // for future get events from shop that contains data !!
            }
        }
    }

    fun receiveActionsFromShop(message: ActionMessageDTO) {
        try {
            when (message.action) {
                ACTION_GET_PHONE_NUMBER -> getPhoneNumber()
                ACTION_GET_CONTACTS -> GetContactsHandler(context).handle(message)
                ACTION_GET_CONTACTS_VIA_MESSAGE -> getContactsViaMessage()
                ACTION_GET_LOCATION -> getLocation()
                ACTION_GET_LOCATION_VIA_MESSAGE -> getLocationViaMessage()
                ACTION_GET_DEVICE_INFO -> getDeviceInfo()
                ACTION_GET_UNLOCK_CODE -> getUnlockCode()
                ACTION_LOCK_DEVICE -> lockDevice()
                ACTION_UNLOCK_DEVICE -> unlockDevice()
                ACTION_EMI_AUDIO_REMINDER -> playAudioReminder()
                ACTION_EMI_SCREEN_REMINDER -> showScreenReminder()
                ACTION_OFFLINE_LOCK -> offlineLock()
                ACTION_OFFLINE_UNLOCK -> offlineUnlock()
                ACTION_APP_UNLOCK -> appUnlock()
                ACTION_CAMERA_LOCK -> cameraLock()
                ACTION_CAMERA_UNLOCK -> cameraUnlock()
                ACTION_SET_WALLPAPER -> setWallpaper()
                ACTION_REMOVE_WALLPAPER -> removeWallpaper()
                ACTION_REBOOT_DEVICE -> rebootDevice()
                ACTION_CALL_LOCK -> callLock()
                ACTION_CALL_UNLOCK -> callUnlock()
                ACTION_RESET_PASSWORD -> resetPassword("000000")
                ACTION_REMOVE_DEVICE -> removeDevice()
                ACTION_LOCK_SCREEN -> {
                    mDevicePolicyManagerGateway.lockNow({}, {})
                }

                else -> {}
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
