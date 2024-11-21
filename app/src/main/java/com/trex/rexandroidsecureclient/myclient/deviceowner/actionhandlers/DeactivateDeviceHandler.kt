package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexnetwork.data.ActionMessageDTO

class DeactivateDeviceHandler (
    val context: Context,
) : BaseActionHandler() {
    private val devicePolicyManagerGatewayImpl = DevicePolicyManagerGatewayImpl(context)

    fun handle(messageDTO: ActionMessageDTO) {
        devicePolicyManagerGatewayImpl.removeActiveAdmin({
            buildAndSendResponseFromRequest(messageDTO, true, "Device deleted successfully!")
        }, {
            buildAndSendResponseFromRequest(messageDTO, true, "Device deleted failed!")
        })
    }
}
