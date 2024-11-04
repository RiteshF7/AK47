package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexnetwork.data.ActionMessageDTO

class CameraUnlockHandler(
    context: Context,
) : BaseActionHandler() {
    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun handle(messageDTO: ActionMessageDTO) {
        mDevicePolicyManagerGateway.setCameraDisabled(
            false,
            {
                buildAndSendResponseFromRequest(
                    messageDTO,
                    true,
                    "Camera unlocked successfully!",
                )
            },
            { error ->
                Log.e("", "error camera: ${error.message}")
                buildAndSendResponseFromRequest(
                    messageDTO,
                    false,
                    "Camera unlocking Failed!\n Please try again.",
                )
            },
        )
    }
}
