package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import com.trex.rexnetwork.data.Actions

class GetPhoneNumberHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle() {
        sendToServerViaSMS(context, Actions.ACTION_GET_PHONE_NUMBER, emptyMap())
    }
}
