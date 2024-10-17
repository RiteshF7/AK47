package com.trex.rexandroidsecureclient.myclient

import android.content.Context
import android.util.Log

class MyExceptionHandler(
    private val context: Context,
) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

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
        val sharedPreferences = context.getSharedPreferences("CrashLogPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save stack trace as a string in SharedPreferences
        val stackTrace = Log.getStackTraceString(throwable)
        editor.putString("error_log", stackTrace)
        editor.apply()
    }
}
