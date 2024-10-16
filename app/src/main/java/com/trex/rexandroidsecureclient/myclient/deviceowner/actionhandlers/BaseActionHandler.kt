package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import com.trex.rexandroidsecureclient.MyApplication

open class BaseActionHandler {
    val context  = MyApplication.getAppContext()
    val policyManager = MyApplication.getDevicePolicyManager()
}
