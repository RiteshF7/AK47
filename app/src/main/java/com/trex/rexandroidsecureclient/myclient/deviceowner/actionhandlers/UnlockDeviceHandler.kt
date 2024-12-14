package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.content.Intent
import com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen.UnlockWithCodeActivity
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.domain.firebasecore.firesstore.DeviceFirestore
import com.trex.rexnetwork.utils.SharedPreferenceManager

class UnlockDeviceHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle(messageDTO: ActionMessageDTO) {
        val sharedPreferenceManager = SharedPreferenceManager(context)
        sharedPreferenceManager.getShopId()?.let { shopId ->
            sharedPreferenceManager.getDeviceId()?.let { deviceId ->
                DeviceFirestore(shopId).updateLockStatus(deviceId, false, {
                    val stopLockTaskIntent = Intent(UnlockWithCodeActivity.STOP_LOCK_TASK)
                    context.sendBroadcast(stopLockTaskIntent)
                    buildAndSendResponseFromRequest(
                        messageDTO,
                        true,
                        "Device Unlocked Successfully!",
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
    }
}
