package com.trex.rexandroidsecureclient

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import com.google.firebase.FirebaseApp
import com.trex.rexandroidsecureclient.myclient.MyExceptionHandler
import com.trex.rexandroidsecureclient.myclient.PayloadReceiver
import com.trex.rexnetwork.Constants
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager

class MyApplication : Application() {
    companion object {
        private var instance: MyApplication? = null
        const val TAG = "APPLICATION CLASS"

        @JvmStatic
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
        Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler(this))
        instance = this
        initPayloadReceiver()
        FirebaseApp.initializeApp(this)
        createNotificationChannel(this)
    }


    private fun initPayloadReceiver() {
        val receiver = PayloadReceiver()
        val filter = IntentFilter("$packageName.${Constants.KEY_BROADCAST_PAYLOAD_ACTION}")
        this.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
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
