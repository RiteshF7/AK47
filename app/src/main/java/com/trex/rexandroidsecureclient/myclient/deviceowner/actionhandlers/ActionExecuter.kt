package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.app.Activity
import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers.RegisterDeviceHandler
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.data.Actions.ACTION_APP_UNLOCK
import com.trex.rexnetwork.data.Actions.ACTION_CALL_LOCK
import com.trex.rexnetwork.data.Actions.ACTION_CALL_UNLOCK
import com.trex.rexnetwork.data.Actions.ACTION_CAMERA_LOCK
import com.trex.rexnetwork.data.Actions.ACTION_CAMERA_UNLOCK
import com.trex.rexnetwork.data.Actions.ACTION_EMI_AUDIO_REMINDER
import com.trex.rexnetwork.data.Actions.ACTION_EMI_SCREEN_REMINDER
import com.trex.rexnetwork.data.Actions.ACTION_GET_CONTACTS
import com.trex.rexnetwork.data.Actions.ACTION_GET_DEVICE_INFO
import com.trex.rexnetwork.data.Actions.ACTION_GET_LOCATION
import com.trex.rexnetwork.data.Actions.ACTION_GET_LOCATION_VIA_MESSAGE
import com.trex.rexnetwork.data.Actions.ACTION_GET_PHONE_NUMBER
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
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.FcmResultActivity
import com.trex.rexnetwork.domain.firebasecore.firesstore.FCMTokenFirestore
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository
import com.trex.rexnetwork.utils.SharedPreferenceManager
import com.trex.rexnetwork.utils.isGetRequest
import com.trex.rexnetwork.utils.startMyActivity

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
        } else {
            context.startMyActivity(FcmResultActivity::class.java, message, true)
        }
    }

    fun receiveActionsFromShop(message: ActionMessageDTO) {
        try {
            when (message.action) {
                ACTION_GET_CONTACTS -> GetContactsHandler(context).handle(message)
                ACTION_GET_LOCATION -> getLocation(message)
                ACTION_GET_DEVICE_INFO -> getDeviceInfo(message)
                ACTION_LOCK_DEVICE -> lockDevice(message)
                ACTION_UNLOCK_DEVICE -> unlockDevice(message)
                ACTION_EMI_AUDIO_REMINDER -> playAudioReminder(message)
                ACTION_EMI_SCREEN_REMINDER -> showScreenReminder(message)
                ACTION_CAMERA_LOCK -> cameraLock(message)
                ACTION_CAMERA_UNLOCK -> cameraUnlock(message)
                ACTION_SET_WALLPAPER -> setWallpaper(message)
                ACTION_REMOVE_WALLPAPER -> removeWallpaper(message)
                ACTION_REBOOT_DEVICE -> rebootDevice(message)
                ACTION_CALL_LOCK -> callLock(message)
                ACTION_CALL_UNLOCK -> callUnlock(message)
                ACTION_REMOVE_DEVICE -> removeDevice(message)
                Actions.ACTION_TEST_MESSAGE -> sendTestResponse(message)
                ACTION_LOCK_SCREEN -> {
                    mDevicePolicyManagerGateway.lockNow({}, {})
                }

                // v2
                ACTION_GET_PHONE_NUMBER -> getPhoneNumber()
                ACTION_RESET_PASSWORD -> resetPassword("000000", message)
                ACTION_GET_LOCATION_VIA_MESSAGE -> getLocationViaMessage()
                ACTION_OFFLINE_LOCK -> offlineLock(message)
                ACTION_OFFLINE_UNLOCK -> offlineUnlock(message)
                ACTION_APP_UNLOCK -> appUnlock(message)
                else -> {}
            }
        } catch (error: Exception) {
            Log.e("ActionExecuterError", "execute: ${error.message}")
        }
    }

    private fun sendTestResponse(message: ActionMessageDTO) {
        sharedPreferenceManager.getShopId()?.let { shopId ->
            fcmFirestore.getFcmToken(shopId) { shopToken ->
                val payload =
                    mapOf(
                        Constants.KEY_RESPOSE_RESULT_STATUS to Constants.RESPONSE_RESULT_SUCCESS,
                        message.action.name to Constants.IS_TEST_MESSAGE,
                    )
                val testResponse = message.copy(fcmToken = shopToken, payload = payload)
                SendActionMessageRepository().sendActionMessage(testResponse)
            }
        }
    }

    private fun lockDevice(message: ActionMessageDTO) {
        LockDeviceHandler(context).handle(message)
    }

    private fun registerDeviceUsingFCM(message: ActionMessageDTO) {
        RegisterDeviceHandler(context as Activity).handle(message)
    }

    private fun unlockDevice(message: ActionMessageDTO) {
        UnlockDeviceHandler(context).handle(message)
    }

    private fun playAudioReminder(message: ActionMessageDTO) {
        AudioReminderHandler(context).playAudioReminder(message)
        Log.i("ActionExecuter", "Play audio reminder action triggered")
    }

    private fun showScreenReminder(message: ActionMessageDTO) {
        EMIScreenReminderHandler(context).handle(message)
    }

    private fun getPhoneNumber() {
        GetPhoneNumberHandler(context).handle()
    }

    private fun getContacts() {
    }

    private fun getContactsViaMessage() {
        GetContactsViaMessageHandler(context).handle()
    }

    private fun offlineLock(message: ActionMessageDTO) {
        lockDevice(message)
    }

    private fun offlineUnlock(message: ActionMessageDTO) {
        unlockDevice(message)
    }

    private fun appUnlock(message: ActionMessageDTO) {
        // Implement app unlock logic
        Log.i("ActionExecuter", "App unlock action triggered")
    }

    private fun cameraLock(message: ActionMessageDTO) {
        CameraLockHandler(context).handle(message)
    }

    private fun cameraUnlock(message: ActionMessageDTO) {
        CameraUnlockHandler(context).handle(message)
    }

    private fun setWallpaper(message: ActionMessageDTO) {
        SetWallpaperHandler(context).handle(message)
    }

    private fun removeWallpaper(message: ActionMessageDTO) {
        RemoveWallpaperHandler(context).handle(message)
    }

    private fun getLocation(message: ActionMessageDTO) {
        GetLocationHandler(context).handle(message)
    }

    private fun getLocationViaMessage() {
        // check
    }

    private fun rebootDevice(message: ActionMessageDTO) {
        RebootDeviceHandler(context).handle(message)
    }

    private fun callLock(message: ActionMessageDTO) {
        CallLockHandler(context).handle(message)
    }

    private fun callUnlock(message: ActionMessageDTO) {
        CallUnlockHandler(context).handle(message)
    }

    private fun resetPassword(
        newPassword: String,
        message: ActionMessageDTO,
    ) {
        ResetPasswordHandler(context).handle(message)
    }

    private fun reactivateDevice() {
        // Implement device reactivation logic
        Log.i("ActionExecuter", "Reactivate device action triggered")
    }

    private fun deactivateDevice() {
        // Implement device deactivation logic
        Log.i("ActionExecuter", "Deactivate device action triggered")
    }

    private fun getDeviceInfo(message: ActionMessageDTO) {
        GetDeviceInfoHandler(context).handle(message)
    }

    private fun getUnlockCode(): String {
        // Implement unlock code retrieval logic
        Log.i("ActionExecuter", "Get unlock code action triggered")
        return "Unlock Code"
    }

    private fun removeDevice(actionMessageDTO: ActionMessageDTO) {
        RemoveDeviceHandler(context).handle(actionMessageDTO)
    }
}
