package com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers

import DeviceInfoUtil
import android.content.Context
import com.google.gson.Gson
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.BaseActionHandler
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.data.NewDevice
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager
import com.trex.rexnetwork.utils.SharedPreferenceManager

class RegisterDeviceHandler(
    context: Context,
) : BaseActionHandler() {
    private val sharedPreferences = SharedPreferenceManager(context)
    private val fcmTokenManager = FCMTokenManager(context, ClientFCMTokenUpdater(context))
    private val shopId = sharedPreferences.getShopId()
    private val deviceInfoUtils = DeviceInfoUtil()

    fun handle() {
        val devicePayloadDataString = Gson().toJson(buildNewDevice())
        sendToServer(
            Actions.ACTION_REG_DEVICE,
            mapOf(Actions.ACTION_REG_DEVICE.name to devicePayloadDataString),
        )
    }

    private fun buildNewDevice(): NewDevice {
        val device =
            NewDevice(
                shopId = shopId!!,
                imeiOne = "00000000",
                fcmToken = fcmTokenManager.getFcmToken()!!,
                manufacturer = deviceInfoUtils.getManufacturer(),
                brand = deviceInfoUtils.getBrand(),
                modelNumber = deviceInfoUtils.getDeviceModel(),
                androidVersion = deviceInfoUtils.getAndroidVersion(),
                deviceCode = deviceInfoUtils.getDeviceId(),
            )

        return device
    }
}
