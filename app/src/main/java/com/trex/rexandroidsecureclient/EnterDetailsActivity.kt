package com.trex.rexandroidsecureclient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.trex.rexandroidsecureclient.myclient.DeviceBuilderUtils
import com.trex.rexandroidsecureclient.provision.GetProvisioningModeActivity
import com.trex.rexandroidsecureclient.utils.ClientSharedPrefs

class EnterDetailsActivity : Activity() {
    private lateinit var enterImeiEditText: EditText
    private lateinit var createNewDeviceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)

        startActivity(Intent(this, FinalizeActivity::class.java))
        // Initialize views
        enterImeiEditText = findViewById(R.id.enter_imei)
        createNewDeviceButton = findViewById(R.id.create_new_device)

        // Set click listener for the button
        createNewDeviceButton.setOnClickListener {
            val imei = enterImeiEditText.text.toString().trim()
            when {
                imei.isEmpty() -> showToast("Please enter IMEI")
                else -> createNewDevice(imei, this)
            }
        }
    }

    private fun createNewDevice(
        imei: String,
        context: Context,
    ) {
//        val shopId = DeviceBuilderUtils(context).getShopId()
//        Toast.makeText(context, shopId, Toast.LENGTH_SHORT).show()
//        getErrorLogs()
        val log = ClientSharedPrefs().getImei()
        Log.i("lifecycle:: ", "createNewDevice: $log")
//        NewDeviceCreator().saveAfterGettingImei {
//            val intent = Intent(this, FinalizeActivity::class.java)
//            startActivity(intent)
//            showToast("New device created with IMEI: $imei")
//        }
    }

    fun getErrorLogs() {
        val sharedPreferences = getSharedPreferences("CrashLogPrefs", Context.MODE_PRIVATE)
        val errorLog = sharedPreferences.getString("error_log", "No crash log found")
        Log.d("CrashLog", errorLog ?: "Not found error")
        Toast.makeText(this, "$errorLog", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
