package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen.UnlockWithCodeActivity
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.utils.startMyActivity

class LockDeviceHandler(
    private val context: Context,
) : BaseActionHandler() {
    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun handle(messageDTO: ActionMessageDTO) {
        mDevicePolicyManagerGateway.setLockTaskPackages(arrayOf(context.packageName), {
            context.startMyActivity(UnlockWithCodeActivity::class.java, true)
            buildAndSendResponseFromRequest(messageDTO, true, "Device Locked Successfully!")
            Log.i("", "lockDevice: Success")
        }, { error ->
            Log.i("", "lockDevice: error :: ${error.message}")
        })
    }
}
