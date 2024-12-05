package com.trex.rexandroidsecureclient.myclient.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler
import java.io.File
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// GlobalExceptionHandler.kt
class GlobalExceptionHandler private constructor(
    private val applicationContext: Context,
    private val defaultHandler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            // Log the exception
            logException(thread, throwable)

            // Handle specific exceptions
            when (throwable) {
                is SocketTimeoutException -> {
                    Log.e(TAG, "Socket timeout occurred", throwable)
                    // Optionally show a user-friendly message
                    showToast("Network timeout. Please try again later.")
                    return // Don't crash for timeout
                }
                is OutOfMemoryError -> {
                    Log.e(TAG, "Out of memory error", throwable)
                    // Clear caches or take other recovery actions
                    return
                }
                else -> {
                    Log.e(TAG, "Uncaught exception", throwable)
                }
            }

            // If it's a critical exception that we can't handle, pass to default handler
            defaultHandler?.uncaughtException(thread, throwable)
        } catch (e: Exception) {
            // If our error handler fails, pass to default handler
            Log.e(TAG, "Error in error handler", e)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun logException(thread: Thread, throwable: Throwable) {
        val stackTrace = Log.getStackTraceString(throwable)
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())

        Log.e(TAG, """
            ⚠️ Uncaught Exception
            Time: $timestamp
            Thread: ${thread.name}
            Exception: ${throwable.javaClass.name}
            Message: ${throwable.message}
            Stack Trace: $stackTrace
        """.trimIndent())

        // Optionally save to file
        saveToFile(timestamp, thread, throwable)
    }

    private fun saveToFile(timestamp: String, thread: Thread, throwable: Throwable) {
        try {
            val file = File(applicationContext.getExternalFilesDir(null), "crash_logs.txt")
            val stackTrace = Log.getStackTraceString(throwable)

            file.appendText("""
                ===== Crash Report =====
                Time: $timestamp
                Thread: ${thread.name}
                Exception: ${throwable.javaClass.name}
                Message: ${throwable.message}
                Stack Trace: $stackTrace
                
            """.trimIndent())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save crash log to file", e)
        }
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "GlobalErrorHandler"

        fun setup(context: Context) {
            // Save the default handler
            val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

            // Set our custom handler
            Thread.setDefaultUncaughtExceptionHandler(
                GlobalExceptionHandler(context.applicationContext, defaultHandler)
            )

            // Set up Coroutine exception handler
            setupCoroutineExceptionHandler()
        }

        private fun setupCoroutineExceptionHandler() {
            CoroutineExceptionHandler { _, throwable ->
                Log.e(TAG, "Coroutine exception", throwable)
                when (throwable) {
                    is SocketTimeoutException -> {
                        Log.e(TAG, "Socket timeout in coroutine", throwable)
                    }
                    else -> {
                        Log.e(TAG, "Uncaught coroutine exception", throwable)
                    }
                }
            }
        }
    }
}
