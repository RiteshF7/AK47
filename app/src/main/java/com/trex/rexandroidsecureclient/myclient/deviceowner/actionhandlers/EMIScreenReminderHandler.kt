package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.content.Intent
import com.trex.rexandroidsecureclient.myclient.ui.emireminderscreen.EmiReminderActivity
import com.trex.rexnetwork.data.ActionMessageDTO

class EMIScreenReminderHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle(messageDTO: ActionMessageDTO) {
        val emiReminderActivity = Intent(context, EmiReminderActivity::class.java)
        emiReminderActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(emiReminderActivity)
        buildAndSendResponseFromRequest(messageDTO, true, "Screen reminder sent successfully!")
    }
}
