package com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers

import DeviceInfoUtil
import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.BaseActionHandler
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.data.DeviceInfo
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager

class RegisterDeviceHandler(
    private val context: Activity,
) : BaseActionHandler() {
    private val fcmTokenManager = FCMTokenManager(context, ClientFCMTokenUpdater(context))
    private val deviceInfoUtils = DeviceInfoUtil()

    fun handle(message: ActionMessageDTO) {
        val deviceModel = "${deviceInfoUtils.getManufacturer()} ${deviceInfoUtils.getDeviceModel()}"
        fcmTokenManager.getFCMToken { result ->
            result.fold(
                onSuccess = { token ->
                    val deviceInfo = DeviceInfo(token, deviceModel)
                    val deviceInfoString = Gson().toJson(deviceInfo)
                    sendTo(
                        context,
                        message.action,
                        mapOf(
                            Actions.ACTION_REG_DEVICE.name to deviceInfoString,
                        ),
                        waitForResult = true,
                    )
                },
                onFailure = { exception ->
                    Log.e("FCM", "Failed to get token", exception)
                },
            )
        }
    }
}
