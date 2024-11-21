package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexnetwork.data.ActionMessageDTO

class RemoveDeviceHandler(
    val context: Context,
) : BaseActionHandler() {
    private val devicePolicyManagerGatewayImpl = DevicePolicyManagerGatewayImpl(context)

    fun handle(messageDTO: ActionMessageDTO) {
        devicePolicyManagerGatewayImpl.clearDeviceOwnerApp({
            buildAndSendResponseFromRequest(messageDTO, true, "Device removed successfully!")

        }, {
            buildAndSendResponseFromRequest(messageDTO, true, "Device removed failed!")
        })
    }
}
