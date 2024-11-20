package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import com.trex.rexandroidsecureclient.myclient.ui.emireminderscreen.EmiReminderActivity
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.utils.startMyActivity

class EMIScreenReminderHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle(messageDTO: ActionMessageDTO) {
        context.startMyActivity(EmiReminderActivity::class.java, true)
        buildAndSendResponseFromRequest(messageDTO, true, "Screen reminder sent successfully!")
    }
}
