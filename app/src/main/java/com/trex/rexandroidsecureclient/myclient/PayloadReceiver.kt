package com.trex.rexandroidsecureclient.myclient

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTOMapper
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository

class PayloadReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        Log.i("apyload receiver:::", "onReceive: Message received")
        val actionMessageString =
            intent.getStringExtra(Constants.KEY_PAYLOAD_DATA)

        if (actionMessageString.isNullOrBlank()) {
            logErrorMessage("message not found in payload!")
            return
        }
        val actionMessageDTO = ActionMessageDTOMapper.fromJsonToDTO(actionMessageString)

        Log.i("", "onReceive: ${actionMessageDTO.action}")
        val actionExecuter = ActionExecuter(context)
        actionExecuter.execute(actionMessageDTO.action)

//        val shopFcmDto =
//            SendActionMessageRepository().sendActionMessage(actionMessageDTO)
    }

    fun logErrorMessage(errorMessage: String) {
//        Log.e("error receiving payload!", "logErrorMessage: ${errorMessage}")
    }
}
