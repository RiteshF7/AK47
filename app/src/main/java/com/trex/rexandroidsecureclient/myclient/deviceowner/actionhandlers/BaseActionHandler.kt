package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.app.Activity
import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import com.google.gson.Gson
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexandroidsecureclient.myclient.utils.CommonConstants
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.FcmRequestActivity
import com.trex.rexnetwork.domain.firebasecore.firesstore.FCMTokenFirestore
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository
import com.trex.rexnetwork.utils.SharedPreferenceManager
import com.trex.rexnetwork.utils.startMyActivityForResult

open class BaseActionHandler {
    private val fcmFirestore = FCMTokenFirestore()
    private val mSharedPref = SharedPreferenceManager(MyApplication.getAppContext())
    private val sendActionMessageRepository = SendActionMessageRepository()

    fun sendTo(
        context: Activity,
        actionKey: Actions,
        payload: Map<String, String>,
        server: Boolean = true,
        waitForResult: Boolean = false,
    ) {
        getShopFcmToken { shopToken ->
            val message =
                ActionMessageDTO(
                    shopToken,
                    actionKey,
                    payload,
                )
            if (waitForResult) {
                context.startMyActivityForResult(
                    FcmRequestActivity::class.java,
                    message,
                    FcmRequestActivity.FCM_REQUEST_KEY,
                    true,
                )
            } else {
                sendActionMessageRepository.sendActionMessage(message)
            }
        }
    }

    fun sendResponseToShop(messageDTO: ActionMessageDTO) {
        getShopFcmToken { shopFcmToken ->
            sendActionMessageRepository.sendActionMessage(messageDTO.copy(fcmToken = shopFcmToken))
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

    fun buildResponseFromRequest(
        messageDTO: ActionMessageDTO,
        isSuccess: Boolean,
        payload: String,
    ): ActionMessageDTO {
        val status =
            if (isSuccess) Constants.RESPONSE_RESULT_SUCCESS else Constants.RESPONSE_RESULT_FAILED
        val payload =
            mapOf(
                Constants.KEY_RESPOSE_RESULT_STATUS to status,
                messageDTO.action.name to payload,
            )
        return messageDTO.copy(payload = payload)
    }

    fun buildAndSendResponseFromRequest(
        messageDTO: ActionMessageDTO,
        isSuccess: Boolean,
        payload: String,
    ) {
        val status =
            if (isSuccess) Constants.RESPONSE_RESULT_SUCCESS else Constants.RESPONSE_RESULT_FAILED
        val payloadMap =
            mapOf(
                Constants.KEY_RESPOSE_RESULT_STATUS to status,
                messageDTO.action.name to payload,
            )
        val response = messageDTO.copy(payload = payloadMap)
        sendResponseToShop(response)
    }

    fun getShopFcmToken(onSuccess: (String) -> Unit) {
        mSharedPref.getShopId()?.let { shopId ->
            fcmFirestore.getFcmToken(shopId) { shopToken ->
                onSuccess(shopToken)
            }
        }
    }
}
