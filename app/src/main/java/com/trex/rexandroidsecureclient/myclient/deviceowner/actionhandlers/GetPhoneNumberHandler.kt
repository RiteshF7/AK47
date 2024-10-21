package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import com.trex.rexcommon.data.DeviceActions

class GetPhoneNumberHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle() {
        sendToServerViaSMS(context, DeviceActions.ACTION_GET_PHONE_NUMBER, emptyMap())
    }
}
