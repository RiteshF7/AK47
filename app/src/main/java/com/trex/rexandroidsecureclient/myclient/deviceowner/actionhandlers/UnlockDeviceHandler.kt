package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.os.PowerManager
import androidx.core.content.ContextCompat.getSystemService

class UnlockDeviceHandler : BaseActionHandler() {
    fun handle() {
    }

    private fun unlockDevice() {
        // Ensure the screen is on
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock =
            powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "UnlockService:WakeLock",
            )
        wakeLock.acquire(10000) // 10 seconds timeout

//        // Attempt to unlock the device
//        if (policyManager.isAdminActive(adminComponentName)) {
//            powerManager.resetPassword("", 0)
//
//            // For API 28+, use setLockTaskPackages() if needed
//            // devicePolicyManager.setLockTaskPackages(adminComponentName, arrayOf(packageName))
//
//            // Dismiss keyguard
//            sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
//        }

        wakeLock.release()
    }
}
