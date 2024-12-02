package com.trex.rexandroidsecureclient

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.os.UserManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexandroidsecureclient.myclient.MyExceptionHandler
import com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen.UnlockWithCodeActivity
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager
import com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen.PermissionHandlerActivity
import com.trex.rexnetwork.domain.repositories.DevicePresenceRepo
import com.trex.rexnetwork.domain.repositories.DeviceRegistration
import com.trex.rexnetwork.utils.SharedPreferenceManager
import com.trex.rexnetwork.utils.parcelable
import com.trex.rexnetwork.utils.startMyActivity

class EnterDetailsActivity : Activity() {
    private lateinit var retryBtn: Button
    private lateinit var stopBtn: Button

    // Dependencies
    private val sharedPreferenceManager by lazy {
        SharedPreferenceManager(this)
    }

    private lateinit var mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)
        actionBar?.hide()
//        finish()

        retryBtn = findViewById(R.id.btn_reg_dev_retry)
        stopBtn = findViewById(R.id.btn_reg_dev_stop)
        mDevicePolicyManagerGateway = DevicePolicyManagerGatewayImpl(this)
        saveInitialData()

        stopBtn.setOnClickListener {
            DevicePresenceRepo().stopPresenceMonitoring("1")
        }

        retryBtn.setOnClickListener {
            val deviceRegistration = DeviceRegistration("1", "+919910000163")
            DevicePresenceRepo().registerPresenceMonitoring(deviceRegistration)

            this.startMyActivity(UnlockWithCodeActivity::class.java, true)
//            InitDeviceRegistrationActivity.go(this)
//            finish()
        }
    }

    fun saveInitialData() {
        sharedPreferenceManager.saveRegCompleteStatus("")

        sharedPreferenceManager.saveShopId("+919910000163")
        sharedPreferenceManager.saveDeviceId("")
    }

    fun sendRequest() {
        val clientFCMManager = FCMTokenManager(this, ClientFCMTokenUpdater(this))
        clientFCMManager.refreshToken({})

        ActionExecuter(this).receiveActionsFromShop(
            ActionMessageDTO(
                "",
                Actions.ACTION_REMOVE_DEVICE,
            ),
        )
//        ActionExecuter(this).sendActionToShop(ActionMessageDTO("", Actions.ACTION_REG_DEVICE))
        Log.e("oooo", "onCreate: Token ${clientFCMManager.getFCMToken({})}")
    }

    fun logError() {
        val errorLogs = MyExceptionHandler(this).getErrorLogs()
        Log.i("mlogs", "onCreate: $errorLogs")
        Toast.makeText(this, "$errorLogs", Toast.LENGTH_SHORT).show()
    }

    private fun processProvisioningExtras() {
        val extras = getProvisioningExtras() ?: return
        Log.e("", "processProvisioningExtras: shop id not found")

        extras.getString(Constants.ADMIN_SHOP_ID)?.let { shopId ->
            handleClientRegistration(shopId)
        }
    }

    private fun getProvisioningExtras(): PersistableBundle? = intent.parcelable(DevicePolicyManager.EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE)

    private fun handleClientRegistration(shopId: String) {
        sharedPreferenceManager.saveShopId(shopId)
        ActionExecuter(this).sendActionToShop(ActionMessageDTO("", Actions.ACTION_REG_DEVICE))
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PermissionHandlerActivity.PERMISSION_RESULT_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "ALl permission granted!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun blockAppUninstallation() {
        // Call the method
        mDevicePolicyManagerGateway.setUninstallBlocked(
            this.packageName,
            true, // true to block uninstallation
            {
                makeMyToast("success!")
            },
            {
                makeMyToast("error ${it.message}")
            },
        )
    }

    fun Activity.makeMyToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun hideAppFromDrawer() {
        mDevicePolicyManagerGateway.setApplicationHidden(
            this.packageName,
            true,
            {
                makeMyToast("success!")
            },
            {
                makeMyToast("error ${it.message}")
                Log.e("TAG", "hideAppFromDrawer: $it")
            },
        )
    }

    fun setUserRestrictions(mDevicePolicyManagerGateway: DevicePolicyManagerGatewayImpl) {
        mDevicePolicyManagerGateway.setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, true)
//        mDevicePolicyManagerGateway.setUserRestriction(
//            UserManager.DISALLOW_DEBUGGING_FEATURES,
//            true,
//        )
    }
}
