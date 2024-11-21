package com.trex.rexandroidsecureclient.myclient.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.FcmRequestActivity
import com.trex.rexnetwork.utils.SharedPreferenceManager
import java.util.UUID

class InitDeviceRegistrationActivity : ComponentActivity() {
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var isDeviceRegCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferenceManager = SharedPreferenceManager(this)
        isDeviceRegCompleted = sharedPreferenceManager.getRegCompleteStatus()
        initDeviceRegistration()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FcmRequestActivity.FCM_REQUEST_KEY) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK)
                if (isDeviceRegCompleted) {
                    handleDeviceRegSuccessUi()
                } else {
                    handleDeviceRegFailureUi()
                }
            } else {
                setResult(RESULT_CANCELED)
                handleDeviceRegFailureUi()
            }
        }
    }

    fun handleDeviceRegSuccessUi() {
        // todo
        // make ui for result
        // don't touch test reg device flow using enter detail activity !!
        // also take device id and save to pref
        sharedPreferenceManager.getDeviceId()?.let { deviceId ->
            // update success ui yaha pe
        }
    }

    fun handleDeviceRegFailureUi() {
        finish()
    }

    fun initDeviceRegistration() {
        val regAction =
            ActionMessageDTO(
                "",
                Actions.ACTION_REG_DEVICE,
                HashMap(),
                false,
                UUID.randomUUID().toString(),
            )
        ActionExecuter(this).sendActionToShop(regAction)
    }

    companion object {
        const val REG_DEVICE_REQUEST_CODE = 700

        fun go(activity: Activity) {
            val intent = Intent(activity, InitDeviceRegistrationActivity::class.java)
            activity.startActivityForResult(
                intent,
                REG_DEVICE_REQUEST_CODE,
            )
        }
    }
}
