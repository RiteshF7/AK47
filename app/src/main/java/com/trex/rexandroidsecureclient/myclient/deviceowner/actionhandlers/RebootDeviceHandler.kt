package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexnetwork.data.ActionMessageDTO

class RebootDeviceHandler(
    private val context: Context,
) : BaseActionHandler() {
    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun handle(messageDTO: ActionMessageDTO) {
        mDevicePolicyManagerGateway.reboot(
            {
                buildAndSendResponseFromRequest(messageDTO, true, "reboot device successfully!")
                Log.i("", "rebootDevice: rebooting!")
            },
            { error ->
                buildAndSendResponseFromRequest(messageDTO, false, "Reboot device failed!")
                Log.i("", "rebootDevice: ${error.message}")
            },
        )
    }
}
