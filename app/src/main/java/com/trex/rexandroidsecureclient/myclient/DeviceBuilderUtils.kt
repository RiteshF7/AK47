package com.trex.rexandroidsecureclient.myclient

import DeviceInfoUtil
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.trex.rexandroidsecureclient.myclient.utils.CommonConstants
import com.trex.rexnetwork.RetrofitClient
import com.trex.rexnetwork.data.NewDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceBuilderUtils(
    val context: Context,
) {
    private var shopId = CommonConstants.KEY_SHOP_ID_NOT_FOUND
    private var imei = CommonConstants.KEY_IMEI_NOT_FOUND
    private var fcmToken = CommonConstants.KEY_FCM_TOKEN_NOT_FOUND
    private val deviceInfoUtils = DeviceInfoUtil()

    init {
        getFcmToken {
            this.fcmToken = it
        }
    }

    private fun getFcmToken(onSuccess: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            onSuccess(token)
        }
    }

    fun saveShopId(shopId: String) {
        if (shopId.isNotBlank()) {
            this.shopId = shopId
        }
    }

    fun saveImei(imei: String) {
        // also check IMEIValidator.isValidIMEI(imei) in prod
        if (imei.isNotBlank()) {
            this.imei = imei
        }
    }

    fun createDevice(onSuccess: (Boolean) -> Unit) {
        if (!isAllFieldValid()) return
        val newDevice = buildNewDevice()
        saveDeviceToFirebase(newDevice, onSuccess)
    }

    private fun saveDeviceToFirebase(
        newDevice: NewDevice,
        onSuccess: (Boolean) -> Unit,
    ) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val response = RetrofitClient.getBuilder.registerNewDevice(newDevice)
                Log.e(TAG, "saveDeviceToFirebase: $response")
                if (response.isSuccessful) {
                    onSuccess(true)
                } else {
                    onSuccess(false)
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, "saveDeviceToFirebase: ${e.message}")
            onSuccess(false)
        }
    }

    private fun isAllFieldValid(): Boolean {
        when {
            shopId == CommonConstants.KEY_SHOP_ID_NOT_FOUND -> {
                printInvalidFieldErrorLog(CommonConstants.KEY_SHOP_ID)
                return false
            }

            imei == CommonConstants.KEY_IMEI_NOT_FOUND -> {
                printInvalidFieldErrorLog(CommonConstants.KEY_IMEI)
                return false
            }

            fcmToken == CommonConstants.KEY_FCM_TOKEN_NOT_FOUND -> {
                printInvalidFieldErrorLog(CommonConstants.KEY_FCM_TOKEN)
                return false
            }
        }
        return true
    }

    private fun buildNewDevice(): NewDevice {
        val device =
            NewDevice(
                shopId = shopId,
                imeiOne = imei,
                fcmToken = fcmToken,
                manufacturer = deviceInfoUtils.getManufacturer(),
                brand = deviceInfoUtils.getBrand(),
                modelNumber = deviceInfoUtils.getDeviceModel(),
                androidVersion = deviceInfoUtils.getAndroidVersion(),
                deviceCode = deviceInfoUtils.getDeviceId(),
            )

        return device
    }

    private fun printInvalidFieldErrorLog(key: String) {
        Log.e(TAG, "printErrorLog:  $key not found!")
    }

    companion object {
        private const val TAG = "DeviceBuilderUtils"
    }
}
