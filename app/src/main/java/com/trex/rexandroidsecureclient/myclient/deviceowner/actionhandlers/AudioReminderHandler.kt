package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.media.MediaPlayer
import com.trex.rexandroidsecureclient.R

class AudioReminderHandler(
    private val context: Context,
) {
    private var mediaPlayer: MediaPlayer? = null

    fun playAudioReminder() {
        //TODO set volume to full
        // Release any existing media player instance
        mediaPlayer?.release()

        // Initialize and play audio
        mediaPlayer = MediaPlayer.create(context, R.raw.my_audio)
        mediaPlayer?.start()

        // Release the player after completion
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
    }

    // Optional: Call this when you need to stop or release media player manually
    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
