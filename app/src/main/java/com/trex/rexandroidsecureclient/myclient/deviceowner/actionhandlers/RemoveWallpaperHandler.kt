package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.app.WallpaperManager
import android.content.Context
import android.util.Log
import com.trex.rexandroidsecureclient.myclient.deviceowner.actionhandlers.actionHelpers.BitmapHelper
import com.trex.rexnetwork.data.ActionMessageDTO

class RemoveWallpaperHandler(
    private val context: Context,
) : BaseActionHandler() {
    private val bitmapHelper = BitmapHelper(context)

    fun handle(messageDTO: ActionMessageDTO) {
        try {
            val blackBitmap = bitmapHelper.getBlackBitmap()
            val wallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.setBitmap(blackBitmap)
            buildAndSendResponseFromRequest(messageDTO, true, "Wallpaper removed successfully!")
        } catch (e: Exception) {
            buildAndSendResponseFromRequest(messageDTO, false, "Walpaper removing failed!")
            Log.i(this.javaClass.simpleName, "Error setting walpaper : ${e.message}")
        }
    }
}
