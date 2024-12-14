package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.DevicePolicyManagerGatewayImpl
import com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen.UnlockWithCodeActivity
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore
import com.trex.rexnetwork.utils.SharedPreferenceManager
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
            SharedPreferenceManager(context).getShopId()?.let { shopId ->
                SharedPreferenceManager(context).getDeviceId()?.let { deviceId ->
                    DeviceFirestore(shopId).updateLockStatus(deviceId, true, {
                        context.startMyActivity(UnlockWithCodeActivity::class.java, true)
                        buildAndSendResponseFromRequest(
                            messageDTO,
                            true,
                            "Device Locked Successfully!",
                        )
                    }, {
                        buildAndSendResponseFromRequest(
                            messageDTO,
                            false,
                            "Please try again!",
                        )
                    })
                }
            }
            Log.i("", "lockDevice: Success")
        }, { error ->
            buildAndSendResponseFromRequest(messageDTO, false, "No Admin access!")
            Log.i("", "lockDevice: error :: ${error.message}")
        })
    }
}
