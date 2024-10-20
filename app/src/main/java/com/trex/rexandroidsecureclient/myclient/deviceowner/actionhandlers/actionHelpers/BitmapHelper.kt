package com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers.actionHelpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log

class BitmapHelper(
    private val context: Context,
) {
    fun getBlackBitmap(): Bitmap {
        try {
        } catch (e: Exception) {
            Log.i(
                this.javaClass.simpleName,
                "getBlackBitmap: Error creating bitmap :: ${e.message}",
            )
        }
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val blackBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        blackBitmap.eraseColor(Color.BLACK)
        return blackBitmap
    }
}
