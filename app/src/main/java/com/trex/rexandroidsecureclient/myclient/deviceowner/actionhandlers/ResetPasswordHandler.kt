package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import com.trex.rexnetwork.data.ActionMessageDTO

class ResetPasswordHandler(
    private val context: Context,
) : BaseActionHandler() {
    fun handle(messageDTO: ActionMessageDTO) {
        buildAndSendResponseFromRequest(messageDTO, true, "Password reset successfully!")
    }
}
