package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import DeviceInfoUtil
import android.content.Context
import com.google.gson.Gson
import com.trex.rexnetwork.data.ActionMessageDTO

class GetDeviceInfoHandler(
    private val context: Context,
) : BaseActionHandler() {
    private val deviceInfoUtil = DeviceInfoUtil()

    fun handle(messageDTO: ActionMessageDTO) {
        val payload = deviceInfoUtil.getAllDeviceInfo()

        val payloadString = Gson().toJson(payload)
        val response = buildResponseFromRequest(messageDTO, true, payloadString)
        sendResponseToShop(response)
    }
}
