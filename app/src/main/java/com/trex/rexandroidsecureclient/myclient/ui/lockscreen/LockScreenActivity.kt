package com.trex.rexandroidsecureclient.myclient.ui.lockscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.trex.rexnetwork.domain.repositories.SendActionMessageRepository

class LockScreenActivity : ComponentActivity() {
    private val repo = SendActionMessageRepository()


    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
    ) {
        super.onCreate(savedInstanceState, persistentState)

        this.startLockTask()

        setContent {

        }
    }


}
