package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.trex.rexandroidsecureclient.MyApplication
import com.trex.rexandroidsecureclient.R
import com.trex.rexnetwork.data.ActionMessageDTO

class AudioReminderHandler(
    private val context: Context,
) : BaseActionHandler() {
    private var mediaPlayer: MediaPlayer? = null

    fun playAudioReminder(messageDTO: ActionMessageDTO) {
        // Release any existing media player instance
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer = MediaPlayer.create(MyApplication.getAppContext(), R.raw.my_audio)

        mediaPlayer?.setOnPreparedListener {
            // Increase volume to maximum
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0,
            )
            it.start()
        }

        mediaPlayer?.setOnCompletionListener {
            buildAndSendResponseFromRequest(messageDTO,true,"Audio reminder sent successfully!")
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
