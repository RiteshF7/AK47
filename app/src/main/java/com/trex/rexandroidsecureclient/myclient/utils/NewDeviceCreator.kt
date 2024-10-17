package com.trex.rexandroidsecureclient.utils

import DeviceInfoUtil
import android.content.Context
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexandroidsecureclient.myclient.network.RetrofitClient
import com.trex.rexcommon.data.NewDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewDeviceCreator {
    private val TAG: String = "New Device Creator"
    private val context = MyApplication.getAppContext()
    private val clientSharedPrefs by lazy {
        ClientSharedPrefs()
    }
    private val deviceInfoUtils by lazy {
        DeviceInfoUtil()
    }

    fun saveDevice(
        context: Context,
        extras: PersistableBundle,
        onResult: (Boolean) -> Unit,
    ) {
        // can comment this in production
        deviceInfoUtils.saveFcmToken()
        getShopIdFromIntent(extras)
        deviceInfoUtils.saveImei()
        val newDevice = createNewDevice(context)
        Log.i(TAG, "saveDevice: ${newDevice.imeiOne}")
//        if (newDevice.imeiOne == ClientSharedPrefs.IMEI_NOT_FOUND || newDevice.imeiOne.isBlank()) {
//            onResult(false)
//        }
        Log.i(TAG, "saveDevice: trying to save")
        saveDeviceToFirebase(newDevice) {
            onResult(true)
        }
    }

    fun saveAfterGettingImei(onResult: (Boolean) -> Unit) {
        val newDevice = createNewDevice(context)
        Log.i(TAG, "saveAfterGettingImei: $newDevice")
        saveDeviceToFirebase(newDevice) {
            onResult(true)
        }
    }

    fun getShopIdFromIntent(extras: PersistableBundle): String =

        extras.getString(ClientSharedPrefs.SHOP_ID)?.also {
            clientSharedPrefs.saveShopId(it)
        } ?: run {
            Log.e(TAG, "getShopIdFromIntent: Shop ID not found in extras")
            Toast
                .makeText(
                    context,
                    "${ClientSharedPrefs.SHOP_ID_NOT_FOUND} Shop ID missing!",
                    Toast.LENGTH_SHORT,
                ).show()
            ClientSharedPrefs.DUMMY_SHOP_ID
        }

    private fun createNewDevice(context: Context): NewDevice {
        val shopId = clientSharedPrefs.getShopId()
        showNotificationOnDummyId(shopId, context)
        val device =
            NewDevice(
                shopId = clientSharedPrefs.getShopId(),
                imeiOne = clientSharedPrefs.getImei(),
                fcmToken = clientSharedPrefs.getFcmToken(),
                manufacturer = deviceInfoUtils.getManufacturer(),
                brand = deviceInfoUtils.getBrand(),
                modelNumber = deviceInfoUtils.getDeviceModel(),
                androidVersion = deviceInfoUtils.getAndroidVersion(),
                deviceCode = deviceInfoUtils.getDeviceId(),
            )

        return device
    }

    private fun showNotificationOnDummyId(
        shopId: String,
        context: Context,
    ) {
        if (shopId.equals(ClientSharedPrefs.DUMMY_SHOP_ID)) {
            Log.e(TAG, "createNewDevice: dumping device into dump shop!")
            Toast
                .makeText(
                    context,
                    "${ClientSharedPrefs.SHOP_ID_NOT_FOUND} SAVING INTO DUMMY SHOP",
                    Toast.LENGTH_SHORT,
                ).show()
        }
    }

    fun saveDeviceToFirebase(
        newDevice: NewDevice,
        onResult: (Response<Unit>) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val respose = RetrofitClient.builder.registerNewDevice(newDevice)
            onResult(respose)
        }
    }
}
