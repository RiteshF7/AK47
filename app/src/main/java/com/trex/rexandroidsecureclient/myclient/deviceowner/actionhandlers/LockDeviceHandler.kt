package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.content.Intent
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexandroidsecureclient.myclient.ui.lockappscreen.LockAppActivity
import com.trex.rexnetwork.data.ActionMessageDTO

class LockDeviceHandler(
    private val context: Context,
) : BaseActionHandler() {
    private val mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl =
        DevicePolicyManagerGatewayImpl(
            context,
        )

    fun handle(messageDTO: ActionMessageDTO) {
        mDevicePolicyManagerGateway.setLockTaskPackages(arrayOf(context.packageName), {
            val kioskIntent = Intent(context, LockAppActivity::class.java)
            kioskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(kioskIntent)
            buildAndSendResponseFromRequest(messageDTO, true, "Device Locked Successfully!")
            Log.i("", "lockDevice: Success")
        }, { error ->
            Log.i("", "lockDevice: error :: ${error.message}")
        })
    }
}
