package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import com.google.gson.Gson
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexandroidsecureclient.myclient.utils.CommonConstants
import com.trex.rexnetwork.RetrofitClient
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.domain.firebasecore.firesstore.FCMTokenFirestore
import com.trex.rexnetwork.utils.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

open class BaseActionHandler {
    private val fcmFirestore = FCMTokenFirestore()
    private val mSharedPref = SharedPreferenceManager(MyApplication.getAppContext())

    fun sendTo(
        context: Context,
        actionKey: Actions,
        payload: Map<String, String>,
        server: Boolean = true,
    ) {
        if (server) {
            sendToServer(actionKey, payload)
        } else {
            sendToServerViaSMS(context, actionKey, payload)
        }
    }

    fun sendToServer(
        actionKey: Actions,
        payload: Map<String, String>,
    ) {
        try {
            getShopFcmToken { shopToken ->
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.getBuilder.sendOnlineMessage(
                        ActionMessageDTO(
                            shopToken,
                            actionKey,
                                payload,
                        ),
                    )
                }
            }
        } catch (e: HttpException) {
            if (e.code() == 500) {
                // Handle HTTP 500 Internal Server Error
                println("Internal Server Error: ${e.message}")
            } else {
                // Handle other HTTP errors
                println("HTTP Error: ${e.code()} - ${e.message}")
            }
        } catch (error: Exception) {
            Log.e("", "sendToServer: error :: ${error.message}")
        }
    }

    fun sendToServerViaSMS(
        context: Context,
        actionKey: Actions,
        payload: Map<String, String>,
    ) {
        val messageDto = ActionMessageDTO("", actionKey, payload)
        val message = Gson().toJson(messageDto)
        try {
            val smsManager: SmsManager =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
            smsManager.sendTextMessage(CommonConstants.SMS_NUM, null, message, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getShopFcmToken(onSuccess: (String) -> Unit) {
        mSharedPref.getShopId()?.let { shopId ->
            fcmFirestore.getFcmToken(shopId) { shopToken ->
                onSuccess(shopToken)
            }
        }
    }
}
