package com.trex.rexandroidsecureclient

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.trex.rexandroidsecureclient.utils.NewDeviceCreator

class EnterDetailsActivity : Activity() {
    private lateinit var enterImeiEditText: EditText
    private lateinit var createNewDeviceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)

        // Initialize views
        enterImeiEditText = findViewById(R.id.enter_imei)
        createNewDeviceButton = findViewById(R.id.create_new_device)

        // Set click listener for the button
        createNewDeviceButton.setOnClickListener {
            val imei = enterImeiEditText.text.toString().trim()
            when {
                imei.isEmpty() -> showToast("Please enter IMEI")
                else -> createNewDevice(imei)
            }
        }
    }

    private fun createNewDevice(imei: String) {
        NewDeviceCreator().saveAfterGettingImei {
            val intent = Intent(this, FinalizeActivity::class.java)
            startActivity(intent)
            showToast("New device created with IMEI: $imei")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
