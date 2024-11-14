package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.trex.rexnetwork.data.ActionMessageDTO

open class GetContactsHandler(
    private val context: Context,
) : BaseActionHandler() {
    open fun handle(messageDTO: ActionMessageDTO) {
        val contactsList = getAllContacts(context.contentResolver)
        if (contactsList.isNotEmpty()) {
            val contactsString = contactsList.joinToString(",")

            val response = buildResponseFromRequest(messageDTO, true, contactsString)
            sendResponseToShop(response)
        } else {
            val response = buildResponseFromRequest(messageDTO, false, "Contacts: No Contacts")
            sendResponseToShop(response)
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
