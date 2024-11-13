package com.trex.rexandroidsecureclient

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexandroidsecureclient.myclient.ui.unlockwithcodescreen.UnlockWithCodeActivity
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.utils.SharedPreferenceManager
import com.trex.rexnetwork.utils.parcelable
import com.trex.rexnetwork.utils.startMyActivity

class EnterDetailsActivity : Activity() {
    private lateinit var retryBtn: Button

    // Dependencies
    private val sharedPreferenceManager by lazy {
        SharedPreferenceManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)
        retryBtn = findViewById(R.id.btn_reg_dev_retry)
//        this.startMyActivity(UnlockWithCodeActivity::class.java)
//
//        FCMTokenManager(this, ClientFCMTokenUpdater(this)).refreshToken("")
        retryBtn.setOnClickListener {
            sharedPreferenceManager.saveShopId("+919910000163")
            sharedPreferenceManager.saveDeviceId("123456789009876")
            // for testing only
            this.startMyActivity(UnlockWithCodeActivity::class.java)
//            val errorLogs = MyExceptionHandler(this).getErrorLogs()
//            Log.i("mlogs", "onCreate: $errorLogs")
//            Toast.makeText(this, "$errorLogs", Toast.LENGTH_SHORT).show()

//            sharedPreferenceManager.saveShopId("+919910000163")
//            ActionExecuter(this).sendActionToShop(ActionMessageDTO("", Actions.ACTION_REG_DEVICE))
        }

//        processProvisioningExtras()
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
}
