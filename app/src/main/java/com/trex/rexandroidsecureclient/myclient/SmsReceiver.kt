package com.trex.rexandroidsecureclient.myclient

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.google.gson.Gson
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexandroidsecureclient.myclient.utils.CommonConstants
import com.trex.rexcommon.data.SendMessageDto

class SMSReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "SMSReceiver"
    }

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            messages.forEach { smsMessage ->
                val sender = smsMessage.displayOriginatingAddress
                val messageBody = smsMessage.messageBody

                Log.d(TAG, "SMS received from: $sender")
                Log.d(TAG, "Message: $messageBody")
                if (sender == CommonConstants.SMS_NUM) {
                    messageBody?.let { messages ->
                        if (messages.contains("action") &&
                            messages.contains("payload") &&
                            messages.contains(
                                "to",
                            )
                        ) {
                            val messageDto = Gson().fromJson(messages, SendMessageDto::class.java)
                            Log.i(TAG, "onReceive: ${messageDto.action}")
                            val actionExecuter = ActionExecuter(context)
                            actionExecuter.setPayload(messageDto.payload)
                            actionExecuter.execute(messageDto.action)
                        }
                    }
                }
            }
        }
    }
}
