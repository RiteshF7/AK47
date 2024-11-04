package com.trex.rexandroidsecureclient.myclient.ui.lockappscreen

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.trex.rexandroidsecureclient.R
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter
import com.trex.rexnetwork.data.ActionMessageDTO
import com.trex.rexnetwork.data.Actions

class LockAppActivity : Activity() {
    private val unlcokButton by lazy { findViewById<Button>(R.id.btnUnlock) }

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_app)
        val intentFilter = IntentFilter(STOP_LOCK_TASK)
        registerReceiver(stopLockTaskReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.startLockTask()

        unlcokButton.setOnClickListener {
            ActionExecuter(this).receiveActionsFromShop(
                ActionMessageDTO(
                    "",
                    Actions.ACTION_UNLOCK_DEVICE,
                ),
            )
        }
    }

    companion object {
        const val STOP_LOCK_TASK = "STOP_LOCK_TASK"
    }
}
