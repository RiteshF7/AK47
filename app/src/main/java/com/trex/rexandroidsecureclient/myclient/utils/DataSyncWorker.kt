package com.trex.rexandroidsecureclient.myclient.utils

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar
import java.util.concurrent.TimeUnit

// WorkManager class for handling the sync
class DataSyncWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result =
        try {
            // Make your API call here
            val response = makeApiCall()
            Result.success()
        } catch (e: Exception) {
            Log.e("DataSyncWorker", "Error syncing data", e)
            Result.failure()
        }

    private suspend fun makeApiCall() {
        val tokenManager = FCMTokenManager(context, ClientFCMTokenUpdater(context))
        withContext(Dispatchers.IO) {
            tokenManager.refreshToken {
                Response.success(it)
            }
        }
    }

    companion object {
        fun scheduleDaily(context: Context) {
            // Create constraints
            val constraints =
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            // Create work request
            val dailyWorkRequest =
                PeriodicWorkRequestBuilder<DataSyncWorker>(
                    24,
                    TimeUnit.HOURS, // Repeat every 24 hours
                    15,
                    TimeUnit.MINUTES, // Flex period
                ).apply {
                    setConstraints(constraints)
                    // Set initial delay to next midnight
                    setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                }.build()

            // Schedule the work
            WorkManager
                .getInstance(context)
                .enqueueUniquePeriodicWork(
                    "dataSyncWork",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    dailyWorkRequest,
                )
        }

        private fun calculateInitialDelay(): Long {
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis

            // Set target time to next midnight
            calendar.apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            return calendar.timeInMillis - now
        }
    }
}
