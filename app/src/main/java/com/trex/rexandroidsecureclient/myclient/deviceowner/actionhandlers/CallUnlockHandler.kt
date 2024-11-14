package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.os.UserManager
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexnetwork.data.ActionMessageDTO

class CallUnlockHandler(
    private val context: Context,
) : BaseActionHandler() {
    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun handle(messageDTO: ActionMessageDTO) {
        mDevicePolicyManagerGateway.setUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS, true)
        buildAndSendResponseFromRequest(messageDTO, true, "Call unclocked successfully!")
    }
}
