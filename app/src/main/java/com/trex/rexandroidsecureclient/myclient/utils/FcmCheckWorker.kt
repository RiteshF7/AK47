package com.trex.rexandroidsecureclient.myclient.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager
import com.trex.rexnetwork.domain.firebasecore.fcm.MyFirebaseMessagingService
import java.util.concurrent.TimeUnit

// FCMCheckWorker.kt
class FCMCheckWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    private val tokenManager = FCMTokenManager(context, ClientFCMTokenUpdater(context))

    override fun doWork(): Result =
        try {
            // Check if FCM is working
            checkAndInitializeFCM()
            Result.success()
        } catch (e: Exception) {
            Log.e("FCMCheckWorker", "Error checking FCM", e)
            Result.retry()
        }

    private fun checkAndInitializeFCM() {
        tokenManager.checkAndRefreshToken({ newToken ->
            if (newToken.isEmpty()) {
                restartFCMService()
            }
        }, {
            restartFCMService()
        })
    }

    private fun restartFCMService() {
        // Force stop existing service
        context.stopService(Intent(context, MyFirebaseMessagingService::class.java))

        // Start new service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, MyFirebaseMessagingService::class.java))
        } else {
            context.startService(Intent(context, MyFirebaseMessagingService::class.java))
        }
    }

    companion object {
        fun enqueuePeriodicWork(context: Context) {
            val constraints =
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<FCMCheckWorker>(
                    3,
                    TimeUnit.HOURS, // Run every 3 hours
                    15,
                    TimeUnit.MINUTES, // Flex interval
                ).setConstraints(constraints)
                    .build()

            WorkManager
                .getInstance(context)
                .enqueueUniquePeriodicWork(
                    "FCMCheckWork",
                    ExistingPeriodicWorkPolicy.KEEP, // Keep existing if any
                    periodicWorkRequest,
                )
        }
    }
}
