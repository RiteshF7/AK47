package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers.actionHelpers.BitmapHelper

class SetWallpaperHandler(
    private val context: Context,
) {
    private val bitmapHelper = BitmapHelper(context)

    @RequiresApi(Build.VERSION_CODES.N)
    fun handle() {
        try {
            val blackBitmap = bitmapHelper.getBlackBitmap()
            val wallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.setBitmap(blackBitmap)
        } catch (e: Exception) {
            Log.i(this.javaClass.simpleName, "Error setting walpaper : ${e.message}")
        }
    }
}
