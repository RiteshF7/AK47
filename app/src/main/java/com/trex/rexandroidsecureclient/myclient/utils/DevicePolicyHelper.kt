package com.trex.rexandroidsecureclient.utils

import android.app.admin.DevicePolicyManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class DevicePolicyHelper : DevicePolicyHelperBase() {
    // Lock the device
    fun lockDevice() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow()
        }
    }

    // Wipe the device (factory reset)
    fun wipeDevice() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.wipeData(0)
        }
    }

    // Disable or enable the camera
    fun disableCamera(disable: Boolean) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setCameraDisabled(componentName, disable)
            Log.i("Camera disabled", "disableCamera: success ")
        }
    }

    // Set the password quality (e.g., requiring a PIN, alphanumeric, etc.)
    fun setPasswordQuality(quality: Int) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setPasswordQuality(componentName, quality)
        }
    }

    // Set the minimum password length
    fun setMinimumPasswordLength(length: Int) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setPasswordMinimumLength(componentName, length)
        }
    }

    // Set password expiration timeout (time in milliseconds)
    fun setPasswordExpirationTimeout(timeout: Long) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setPasswordExpirationTimeout(componentName, timeout)
        }
    }

    // Force password reset (password must be set after next unlock)
    fun resetPassword(newPassword: String) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.resetPassword(
                newPassword,
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY,
            )
        }
    }

    // Set whether to require encryption on the device
//    fun requireEncryption(enable: Boolean) {
//        if (devicePolicyManager.isAdminActive(componentName)) {
//            val encryptionStatus = if (enable) {
//                devicePolicyManager.encryptStorage(DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE)
//            } else {
//                devicePolicyManager.encryptStorage(DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE)
//            }
//        }
//    }

    // Disable or enable keyguard features (e.g., disable secure keyguard features like fingerprint)
    fun setKeyguardDisabledFeatures(features: Int) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setKeyguardDisabledFeatures(componentName, features)
        }
    }

    // Set whether the screen capture is disabled (i.e., screenshots and video captures)
    fun setScreenCaptureDisabled(disable: Boolean) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setScreenCaptureDisabled(componentName, disable)
        }
    }

    // Set whether auto time update is required (disable user from changing time manually)
    fun setAutoTimeRequired(enable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (devicePolicyManager.isAdminActive(componentName)) {
                devicePolicyManager.setAutoTimeRequired(componentName, enable)
            }
        }
    }

    // Enable or disable the status bar
    fun setStatusBarDisabled(disable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (devicePolicyManager.isAdminActive(componentName)) {
                devicePolicyManager.setStatusBarDisabled(componentName, disable)
            }
        }
    }

    // Disable or enable factory reset
//    fun setFactoryResetDisabled(disable: Boolean) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            if (devicePolicyManager.isAdminActive(componentName)) {
//                devicePolicyManager.setFactoryResetDisabled(componentName, disable)
//            }
//        }
//    }

    // Disable or enable apps (disallow users to use a particular app by package name)
    fun setApplicationHidden(
        packageName: String,
        hidden: Boolean,
    ) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setApplicationHidden(componentName, packageName, hidden)
        }
    }

    // Grant or revoke app permissions (for specific apps)
    @RequiresApi(Build.VERSION_CODES.M)
    fun setPermissionGrantState(
        packageName: String,
        permission: String,
        grantState: Int,
    ) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setPermissionGrantState(
                componentName,
                packageName,
                permission,
                grantState,
            )
        }
    }

    // Enable or disable Bluetooth
    @RequiresApi(Build.VERSION_CODES.M)
    fun setBluetoothDisabled(disable: Boolean) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setBluetoothContactSharingDisabled(componentName, disable)
        }
    }

//    // Enable or disable Wi-Fi
//    fun setWifiDisabled(disable: Boolean) {
//        if (devicePolicyManager.isAdminActive(componentName)) {
//            devicePolicyManager.setWifiDisabled(componentName, disable)
//        }
//    }

    // Disable adding new users (multi-user feature)
//    fun setUserAdditionDisabled(disable: Boolean) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (devicePolicyManager.isAdminActive(componentName)) {
//                devicePolicyManager.setUserAdditionDisabled(componentName, disable)
//            }
//        }
//    }

    // Set a global proxy for all network requests
//    fun setGlobalProxy(hostname: String, port: Int) {
//        if (devicePolicyManager.isAdminActive(componentName)) {
//            val proxy = DevicePolicyManager.ProxyInfo.buildDirectProxy(hostname, port)
//            devicePolicyManager.setRecommendedGlobalProxy(componentName, proxy)
//        }
//    }
//
//    // Enable or disable USB file transfer
//    fun setUsbDataSignalingEnabled(enable: Boolean) {
//        if (devicePolicyManager.isAdminActive(componentName)) {
//            devicePolicyManager.setUsbDataSignalingEnabled(componentName, enable)
//        }
//    }

    // Request device lock task mode
    fun startLockTaskMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (devicePolicyManager.isAdminActive(componentName)) {
                devicePolicyManager.setLockTaskPackages(
                    componentName,
                    arrayOf("com.example.deviceowner"),
                )
            }
        }
    }

    // Stop device lock task mode
    fun stopLockTaskMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (devicePolicyManager.isAdminActive(componentName)) {
                devicePolicyManager.clearDeviceOwnerApp("com.example.deviceowner")
            }
        }
    }
}
