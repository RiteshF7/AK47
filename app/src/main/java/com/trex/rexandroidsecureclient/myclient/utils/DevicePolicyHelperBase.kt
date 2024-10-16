package com.trex.rexandroidsecureclient.utils

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import com.trex.rexandroidsecureclient.DeviceAdminReceiver
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexandroidsecureclient.R

open class DevicePolicyHelperBase {
    protected val context = MyApplication.getAppContext()
    protected val devicePolicyManager: DevicePolicyManager =
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    protected val componentName: ComponentName =
        ComponentName(context, DeviceAdminReceiver::class.java)

    fun setDeviceProfile() {
        devicePolicyManager.setProfileName(componentName, context.getString(R.string.profile_name))
    }
}
