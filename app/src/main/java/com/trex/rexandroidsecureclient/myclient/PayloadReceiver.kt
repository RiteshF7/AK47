package com.trex.rexandroidsecureclient.myclient

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTOMapper
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.FcmResponseManager

class PayloadReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        ActionMessageDTOMapper.getMessageDTOFromIntent(intent)?.let { actionMessageDTO ->
            Log.i("", "onReceive: ${actionMessageDTO.action}")
            val actionExecuter = ActionExecuter(context)
            if (FcmResponseManager.hasRequest(actionMessageDTO.requestId)) {
                FcmResponseManager.handleResponse(actionMessageDTO.requestId, actionMessageDTO)
                actionExecuter.receiveResponseFromShop(actionMessageDTO)
                return
            }

            actionExecuter.receiveActionsFromShop(actionMessageDTO)
        }
    }
}
