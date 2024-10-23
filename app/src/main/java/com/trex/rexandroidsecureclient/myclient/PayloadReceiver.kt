package com.trex.rexandroidsecureclient.myclient

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.ActionMessageDTOFactory
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository

class PayloadReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val actionMessageString =
            intent.getStringExtra(ActionMessageDTO::class.java.simpleName)

        if (actionMessageString.isNullOrBlank()) {
            logErrorMessage("message not found in payload!")
            return
        }
        val actionMessageDTO = ActionMessageDTOFactory.fromJsonToDTO(actionMessageString)

        if (actionMessageDTO == null) {
            logErrorMessage("unable to cast from string to dto using gson!")
            return
        }

        Log.i("", "onReceive: ${actionMessageDTO.actions}")
        ActionExecuter(context).execute(actionMessageDTO.actions)

        // try to send back to shop
        // make this receiver in both apps
        // send from shop app suing sdk
        // receive from client app
        // send back data to shop using sdk
        // receive in shop app
        // make actionExecuters for shop and client app and perform action needed  !!
        // ypou are doing good dont take too much stress on yourself
        // everything will work out just keed trying and beleive in yourself
        val shopFcmDto =
            SendActionMessageRepository().sendActionMessage(actionMessageDTO)
    }

    fun logErrorMessage(errorMessage: String) {
//        Log.e("error receiving payload!", "logErrorMessage: ${errorMessage}")
    }
}
