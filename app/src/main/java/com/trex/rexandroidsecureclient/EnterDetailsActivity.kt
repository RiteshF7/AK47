package com.trex.rexandroidsecureclient

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager
import com.trex.rexnetwork.utils.SharedPreferenceManager

class EnterDetailsActivity : Activity() {
    private lateinit var createNewDeviceButton: Button
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var fcmTokenManager: FCMTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)
        fcmTokenManager = FCMTokenManager(this, ClientFCMTokenUpdater(this))
        sharedPreferenceManager = SharedPreferenceManager(this)
        sharedPreferenceManager.saveShopId("+919910000163")

//        startActivity(Intent(this, FinalizeActivity::class.java))
        // Initialize views
        createNewDeviceButton = findViewById(R.id.create_new_device)

        // Set click listener for the button
        createNewDeviceButton.setOnClickListener {
            val regMessage = ActionMessageDTO("", Actions.ACTION_REG_DEVICE)
            ActionExecuter(this).execute(
                regMessage,
            )
        }
    }

    private fun showToast(message: String) {
        Log.i("onCLick::", "showToast: clicked")
    }

    private fun getErrorLogs() {
        val sharedPreferences = getSharedPreferences("CrashLogPrefs", Context.MODE_PRIVATE)
        val errorLog = sharedPreferences.getString("error_log", "No crash log found")
        Log.d("CrashLog", errorLog ?: "Not found error")
        Toast.makeText(this, "$errorLog", Toast.LENGTH_SHORT).show()
    }
}
