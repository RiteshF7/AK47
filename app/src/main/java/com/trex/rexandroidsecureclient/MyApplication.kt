package com.trex.rexandroidsecureclient

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    companion object {
        private var instance: MyApplication? = null
        const val TAG = "APPLICATION CLASS"

        fun getAppContext(): Context =
            instance?.applicationContext
                ?: throw IllegalStateException("Application not initialized")

        fun getDevicePolicyManager(): DevicePolicyManager {
            val context = getAppContext()
            return context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        }

        fun isDeviceOwner(): Boolean {
            // TODO
            return false
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
        createNotificationChannel(this)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel"
            val channelName = "Default Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(channelId, channelName, importance).apply {
                    description = "This is the default notification channel"
                }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
