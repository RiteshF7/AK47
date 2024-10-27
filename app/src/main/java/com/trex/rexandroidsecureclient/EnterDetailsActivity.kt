package com.trex.rexandroidsecureclient

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.trex.rexnetwork.utils.SharedPreferenceManager

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
            val shopId =SharedPreferenceManager(this).getShopId()
            shopId?.let { id->
                Toast.makeText(this, "$id is shopid", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun showToast(message: String) {
        Log.i("onCLick::", "showToast: clicked")
    }
}
