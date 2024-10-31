package com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers

import DeviceInfoUtil
import android.content.Context
import com.google.gson.Gson
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.BaseActionHandler
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.data.DeviceInfo
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager

class RegisterDeviceHandler(
    context: Context,
) : BaseActionHandler() {
    private val fcmTokenManager = FCMTokenManager(context, ClientFCMTokenUpdater(context))
    private val deviceInfoUtils = DeviceInfoUtil()

    fun handle() {
        val deviceModel = "${deviceInfoUtils.getManufacturer()} ${deviceInfoUtils.getDeviceModel()}"
        fcmTokenManager.getFcmToken()?.let { fcmToken ->
            val deviceInfo = DeviceInfo(fcmToken,deviceModel)
            val deviceInfoString = Gson().toJson(deviceInfo)
            sendToServer(
                Actions.ACTION_REG_DEVICE,
                mapOf(
                    Actions.ACTION_REG_DEVICE.name to deviceInfoString,
                ),
            )
        }
    }
}
