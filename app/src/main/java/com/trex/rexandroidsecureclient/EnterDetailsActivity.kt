package com.trex.rexandroidsecureclient

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.utils.SharedPreferenceManager
import com.trex.rexnetwork.utils.parcelable

class EnterDetailsActivity : Activity() {
    // Dependencies
    private val sharedPreferenceManager by lazy {
        SharedPreferenceManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)
        processProvisioningExtras()
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
