package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import com.trex.rexandroidsecureclient.myclient.network.RetrofitClient
import com.trex.rexcommon.data.DeviceActions
import com.trex.rexcommon.data.SendMessageDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseActionHandler {
    private val shopFCMToken = ""

    fun sendToServer(
        payload: Map<String, String>,
        actionKey: DeviceActions,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (shopFCMToken.isNotBlank()) {
                RetrofitClient.builder.sendMessage(SendMessageDto(shopFCMToken, actionKey, payload))
            }
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
