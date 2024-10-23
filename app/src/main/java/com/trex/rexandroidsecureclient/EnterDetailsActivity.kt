package com.trex.rexandroidsecureclient

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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
            ActionExecuter(this).execute(Actions.ACTION_GET_PHONE_NUMBER)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
