package com.trex.rexandroidsecureclient

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.Actions

class EnterDetailsActivity : Activity() {
    private lateinit var createNewDeviceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)

//        startActivity(Intent(this, FinalizeActivity::class.java))
        // Initialize views
        createNewDeviceButton = findViewById(R.id.create_new_device)

        // Set click listener for the button
        createNewDeviceButton.setOnClickListener {
            showToast("")

            ActionExecuter(this).execute(Actions.ACTION_GET_CONTACTS)
        }
    }

    private fun showToast(message: String) {
        Log.i("onCLick::", "showToast: clicked")
    }
}
