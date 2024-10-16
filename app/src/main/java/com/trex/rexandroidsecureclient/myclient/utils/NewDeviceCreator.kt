package com.trex.rexandroidsecureclient.utils

import DeviceInfoUtil
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
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
        intent: Intent,
        onSaved: () -> Unit,
    ) {
        // get device info
        deviceInfoUtils.saveImei()

        // get shop id from intent
        val shopId = getShopIdFromIntent(intent)

        // create new device
        val newDevice = createNewDevice(context, shopId)

        if (newDevice.imeiOne == ClientSharedPrefs.IMEI_NOT_FOUND || newDevice.shopId == ClientSharedPrefs.DUMMY_SHOP_ID) {
            Toast.makeText(context, "Please Enter Details", Toast.LENGTH_SHORT).show()
            startMainActivity(context, newDevice)
        } else {
            saveDeviceToFirebase(newDevice) {
                onSaved()
            }
        }
    }

    fun createDummyDevice() {
        // get device info
        deviceInfoUtils.saveImei()

        // get shop id from intent
        val shopId = "+919910000163"

        // create new device
        val newDevice = createNewDevice(context, shopId)
        saveDeviceToFirebase(newDevice) {

        }
    }

    private fun getShopIdFromIntent(intent: Intent): String {
        val extras =
            intent.getBundleExtra(DevicePolicyManager.EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE)
        if (extras == null) {
            Log.e(TAG, "saveAndGetShopID: Extras null")
            Toast
                .makeText(
                    context,
                    ClientSharedPrefs.SHOP_ID_NOT_FOUND + " EMPTY BUNDLE!",
                    Toast.LENGTH_SHORT,
                ).show()
            return ClientSharedPrefs.DUMMY_SHOP_ID
        }
        val shopId = extras.getString(ClientSharedPrefs.SHOP_ID)
        if (shopId == null) {
            Toast
                .makeText(
                    context,
                    "${ClientSharedPrefs.SHOP_ID_NOT_FOUND} Shopid is null in extras",
                    Toast.LENGTH_SHORT,
                ).show()
            return ClientSharedPrefs.DUMMY_SHOP_ID
        }
        clientSharedPrefs.saveShopId(shopId)
        return shopId
    }

    private fun createNewDevice(
        context: Context,
        shopId: String,
    ): NewDevice {
        showNotificationOnDummyId(shopId, context)
        val device =
            NewDevice(
                shopId = shopId,
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

    private fun startMainActivity(
        context: Context,
        newDevice: NewDevice,
    ) {
        //TODO
//        val launchIntent = Intent(context, MainActivity::class.java)
//        launchIntent.apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            putExtra(MainActivity.NEW_DEVICE, newDevice)
//        }
//        context.startActivity(launchIntent)
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
