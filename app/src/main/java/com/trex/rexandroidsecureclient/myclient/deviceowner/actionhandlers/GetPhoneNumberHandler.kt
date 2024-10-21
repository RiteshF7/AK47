package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import com.trex.rexcommon.data.DeviceActions
import com.trex.rexcommon.data.SendMessageDto

class GetPhoneNumberHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle() {
        val payload = SendMessageDto("shopID", DeviceActions.ACTION_GET_PHONE_NUMBER, emptyMap())
        sendToServerViaSMS(context, payload)
    }
}
