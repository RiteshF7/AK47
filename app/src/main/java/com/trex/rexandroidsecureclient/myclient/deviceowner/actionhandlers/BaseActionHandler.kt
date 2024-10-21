package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import com.trex.rexandroidsecureclient.myclient.network.RetrofitClient
import com.trex.rexcommon.data.DeviceActions
import com.trex.rexcommon.data.SendMessageDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

open class BaseActionHandler {
    private val shopFCMToken = ""

    fun sendToServer(
        actionKey: DeviceActions,
        payload: Map<String, String>,
    ) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                if (shopFCMToken.isBlank()) {
                    RetrofitClient.builder.sendMessage(
                        SendMessageDto(
                            shopFCMToken,
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
        messageDto: SendMessageDto,
    ) {
        val phoneNumber = "+919910000163"
        val message = messageDto.toString()
        try {
            val smsManager: SmsManager =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            println("SMS Sent!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
