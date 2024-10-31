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
            FcmResponseManager.handleResponse(actionMessageDTO.requestId, actionMessageDTO)
            val actionExecuter = ActionExecuter(context)
            actionExecuter.execute(actionMessageDTO)
        }
    }
}
