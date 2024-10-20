package com.trex.rexandroidsecureclient.myclient.ui.lockappscreen

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.trex.rexandroidsecureclient.R

class LockAppActivity : Activity() {
    private val unlcokButton by lazy { findViewById<Button>(R.id.btnUnlock) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_app)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.startLockTask()

        unlcokButton.setOnClickListener {
            this.stopLockTask()
            finish()
        }
    }
}
