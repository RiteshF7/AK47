package com.trex.rexandroidsecureclient

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button

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
//            ActionExecuter(this).execute(Actions.ACTION_GET_PHONE_NUMBER)
        }
    }

    private fun showToast(message: String) {
        Log.i("onCLick::", "showToast: clicked")
    }
}
