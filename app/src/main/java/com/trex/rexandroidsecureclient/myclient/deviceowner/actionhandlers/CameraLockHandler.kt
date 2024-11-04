package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexnetwork.data.ActionMessageDTO

class CameraLockHandler(
    context: Context,
) : BaseActionHandler() {
    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun handle(messageDTO: ActionMessageDTO) {
        mDevicePolicyManagerGateway.setCameraDisabled(
            true,
            {
                buildAndSendResponseFromRequest(
                    messageDTO,
                    true,
                    "Camera locked successfully!",
                )
            },
            { error ->
                Log.e("", "error camera: ${error.message}")

                buildAndSendResponseFromRequest(
                    messageDTO,
                    false,
                    "Camera locking Failed!\n Please try again.",
                )
            },
        )
    }
}
