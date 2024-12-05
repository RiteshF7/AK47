package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.domain.repositories.DevicePresenceRepo
import com.trex.rexnetwork.utils.SharedPreferenceManager

class RemoveDeviceHandler(
    val context: Context,
) : BaseActionHandler() {
    private val devicePolicyManagerGatewayImpl = DevicePolicyManagerGatewayImpl(context)

    fun handle(messageDTO: ActionMessageDTO) {
        devicePolicyManagerGatewayImpl.clearDeviceOwnerApp({
            SharedPreferenceManager(context).getDeviceId()?.let { deviceId ->
                DevicePresenceRepo().stopPresenceMonitoring(deviceId)
            }

            buildAndSendResponseFromRequest(messageDTO, true, "Device removed successfully!")
        }, {
            buildAndSendResponseFromRequest(messageDTO, true, "Device removed failed!")
        })
    }
}
