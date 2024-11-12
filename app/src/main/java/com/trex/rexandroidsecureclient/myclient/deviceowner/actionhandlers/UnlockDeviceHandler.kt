package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.content.Intent
import com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen.UnlockWithCodeActivity
import com.trex.rexnetwork.data.ActionMessageDTO

class UnlockDeviceHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle(messageDTO: ActionMessageDTO) {
        val stopLockTaskIntent = Intent(UnlockWithCodeActivity.STOP_LOCK_TASK)
        context.sendBroadcast(stopLockTaskIntent)
        buildAndSendResponseFromRequest(messageDTO, true, "Device unlocked successfully!")
    }
}
