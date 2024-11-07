package com.trex.rexandroidsecureclient.myclient

import android.content.Context
import android.util.Log

class MyExceptionHandler(
    private val context: Context,
) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    private val sharedPreferences =
        context.getSharedPreferences("CrashLogPrefs", Context.MODE_PRIVATE)

    override fun uncaughtException(
        thread: Thread,
        throwable: Throwable,
    ) {
        // Save the error log in SharedPreferences
        saveErrorLogToPreferences(throwable)

        // Call the default handler to let the system handle the crash
        defaultHandler?.uncaughtException(thread, throwable)
    }

    private fun saveErrorLogToPreferences(throwable: Throwable) {
        val editor = sharedPreferences.edit()

        val stackTrace = Log.getStackTraceString(throwable)
        editor.putString(ERROR_LOG_KEY, stackTrace)
        editor.apply()
    }

    fun getErrorLogs() = sharedPreferences.getString(ERROR_LOG_KEY, "Not found!")

    companion object {
        private const val ERROR_LOG_KEY = "ERROR-LOG-KEY"
    }
}
