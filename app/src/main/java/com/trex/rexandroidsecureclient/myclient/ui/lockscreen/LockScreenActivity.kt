package com.trex.rexandroidsecureclient.myclient.ui.lockscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions

class LockScreenActivity : ComponentActivity() {
    private val stopLockTaskReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                stopLockTask()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
    ) {
        super.onCreate(savedInstanceState, persistentState)
        val intentFilter = IntentFilter(STOP_LOCK_TASK)
        registerReceiver(stopLockTaskReceiver, intentFilter, RECEIVER_NOT_EXPORTED)

        this.startLockTask()

        setContent {
            MaterialTheme(colorScheme = LockScreenColors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    LockScreen(
                        deviceName = "Phone Locked!",
                        lockReason = "This device has been locked due to payment default. Please make the necessary payment to unlock the device.",
                        onGetUnlockCode = {
                            ActionExecuter(this).receiveActionsFromShop(
                                ActionMessageDTO(
                                    "",
                                    Actions.ACTION_UNLOCK_DEVICE,
                                ),
                            )
                        },
                    )
                }
            }
        }
    }

    companion object {
        const val STOP_LOCK_TASK = "STOP_LOCK_TASK"
    }
}
