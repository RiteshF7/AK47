package com.trex.rexandroidsecureclient.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexandroidsecureclient.myclient.utils.QrAdditionalData

open class SharedPrefUtil {
    private val context = MyApplication.getAppContext()
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(ADDITIONAL_QRCODE_DATA, Context.MODE_PRIVATE)

    // Save JSON object to SharedPreferences
    fun <T> saveJson(
        key: String,
        data: T,
    ) {
        val jsonString = Gson().toJson(data)
        sharedPreferences.edit().putString(key, jsonString).apply()
    }

    // Retrieve JSON object from SharedPreferences
    inline fun <reified T> getJson(key: String): T? {
        val jsonString = sharedPreferences.getString(key, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, object : TypeToken<T>() {}.type)
        } else {
            null
        }
    }

    // Remove a key
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun getQrAdditionalData() = getJson<QrAdditionalData>(ADDITIONAL_QRCODE_DATA)

    fun saveQrAdditionalData(qrAdditionalData: QrAdditionalData) =
        saveJson(ADDITIONAL_QRCODE_DATA, qrAdditionalData)

    // Clear all stored data
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val ADDITIONAL_QRCODE_DATA = "ADDITIONAL_QRCODE_DATA"
    }
}

class ClientSharedPrefs : SharedPrefUtil() {
    companion object {
        const val IMEI = "IMEI"
        const val IMEI_NOT_FOUND = "IMEI_NOT_FOUND"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val SHOP_ID = "SHOP_ID"
        const val SHOP_ID_NOT_FOUND = "SHOP_ID_NOT_FOUND"
        const val DUMMY_SHOP_ID = "DUMMY_SHOP_ID"
    }

    fun saveFcmToken(fcmToken: String) {
        saveJson(FCM_TOKEN, fcmToken)
    }

    fun saveImei(imei: String) {
        saveJson(IMEI, imei)
    }

    fun saveShopId(shopId: String) {
        saveJson(SHOP_ID, shopId)
    }

    fun getShopId(): String = getJson<String>(SHOP_ID) ?: DUMMY_SHOP_ID

    fun getImei(): String = getJson<String>(IMEI) ?: IMEI_NOT_FOUND

    fun getFcmToken() = getJson<String>(FCM_TOKEN) ?: "none"
}
