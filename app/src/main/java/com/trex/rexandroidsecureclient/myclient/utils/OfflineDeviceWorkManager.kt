package com.trex.rexandroidsecureclient.myclient.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Date
import java.util.concurrent.TimeUnit

class OfflineDeviceWorkManager(
    private val context: Context,
) {
    private val workManager = WorkManager.getInstance(context)
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val WORK_TAG = "offline_device_work"
        private const val PREFS_NAME = "offline_device_prefs"
        private const val LAST_ONLINE_TIME_KEY = "last_online_time"
        private const val OFFLINE_THRESHOLD_DAYS = 5L
    }

    // Worker class to handle the offline device task
    class OfflineDeviceWorker(
        context: Context,
        workerParams: WorkerParameters,
    ) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val lastOnlineTime = prefs.getLong(LAST_ONLINE_TIME_KEY, 0)
            val currentTime = System.currentTimeMillis()

            // Check if device has been offline for more than 5 days
            if (currentTime - lastOnlineTime >= TimeUnit.DAYS.toMillis(OFFLINE_THRESHOLD_DAYS)) {
                // Execute your offline device logic here
                handleOfflineDevice()
            }

            return Result.success()
        }

        private fun handleOfflineDevice() {
            // Implement your offline device handling logic here
            // For example: clear cache, sync data, send notifications, etc.
        }
    }

    // Updates the last online time when device is connected
    fun updateLastOnlineTime() {
        prefs.edit().apply {
            putLong(LAST_ONLINE_TIME_KEY, System.currentTimeMillis())
            apply()
        }
    }

    // Schedule the periodic work
    fun scheduleOfflineCheck() {
        // Create work constraints
        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

        // Create periodic work request
        val workRequest =
            PeriodicWorkRequestBuilder<OfflineDeviceWorker>(
                1,
                TimeUnit.DAYS, // Check daily
            ).setConstraints(constraints)
                .addTag(WORK_TAG)
                .build()

        // Enqueue the work with REPLACE policy
        workManager.enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest,
        )
    }

    // Cancel scheduled work
    fun cancelOfflineCheck() {
        workManager.cancelAllWorkByTag(WORK_TAG)
    }

    // Get the last online time
    fun getLastOnlineTime(): Date {
        val lastOnlineMillis = prefs.getLong(LAST_ONLINE_TIME_KEY, System.currentTimeMillis())
        return Date(lastOnlineMillis)
    }

    // Check if device is currently considered offline (more than threshold days)
    fun isDeviceOfflineLongTerm(): Boolean {
        val lastOnlineTime = prefs.getLong(LAST_ONLINE_TIME_KEY, System.currentTimeMillis())
        val currentTime = System.currentTimeMillis()
        return currentTime - lastOnlineTime >= TimeUnit.DAYS.toMillis(OFFLINE_THRESHOLD_DAYS)
    }
}
