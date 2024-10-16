package com.trex.rexandroidsecureclient.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(
    private val context: Context,
) {
    companion object {
        private const val REQUEST_CODE = 123
    }

    fun checkAndRequestPermission(
        activity: Activity,
        permission: String,
        rationaleMsg: String,
        callback: (Boolean) -> Unit,
    ) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission,
            ) == PackageManager.PERMISSION_GRANTED -> {
                callback(true)
            }

            else -> {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CODE)
            }
        }
    }
}
