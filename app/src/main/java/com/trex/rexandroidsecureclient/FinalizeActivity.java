/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trex.rexandroidsecureclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.trex.rexandroidsecureclient.common.Util;
import com.trex.rexandroidsecureclient.myclient.DeviceBuilderUtils;
import com.trex.rexandroidsecureclient.provision.ProvisioningUtil;

public class FinalizeActivity extends Activity {

    private static final String TAG = FinalizeActivity.class.getSimpleName();
    private DeviceBuilderUtils deviceBuilderUtils;
    private Button createDeviceButton;
    private Button finishSetupButton;
    private TextView titleText;
    private ImageView imageView;
    private EditText imeiEditText;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (savedInstanceState == null) {
            if (Util.isManagedProfileOwner(this)) {
                ProvisioningUtil.enableProfile(this);
            }
        }
        setContentView(R.layout.finalize_activity);
        deviceBuilderUtils = new DeviceBuilderUtils(this);
        deviceBuilderUtils.saveShopId("+919910000163");
        deviceBuilderUtils.saveShopId("+919910000163");

        createDeviceButton = findViewById(R.id.btn_fin);
        finishSetupButton = findViewById(R.id.btn_complete_setup);
        titleText = findViewById(R.id.tv_fin);
        imageView = findViewById(R.id.setup_complete_status_iv);
        imeiEditText = findViewById(R.id.ed_fin);
        progressBar = findViewById(R.id.in_progress_fin);

        finishSetupButton.setOnClickListener(view -> {
            setResult(RESULT_OK);
            finish();
        });

        createDeviceButton.setOnClickListener(view -> {
            hideKeyboard();
            deviceBuilderUtils.saveImei(imeiEditText.getText().toString().trim());
            processProgessUi();
            deviceBuilderUtils.createDevice(aBoolean -> {
                runOnUiThread(() -> {
                    if (aBoolean) {
                        processSuccessUI();
                    } else {
                        processFailureUi();
                        Toast.makeText(this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

                return null;
            });
        });


//        if (ProvisioningUtil.isAutoProvisioningDeviceOwnerMode()) {
//            Log.i(TAG, "Automatically provisioning device onwer");
//            onNavigateNext(null);
//            return;
//        }


        Util.isDeviceOwner(this);

    }


    private void processProgessUi() {
        imeiEditText.setVisibility(View.GONE);
        createDeviceButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void processSuccessUI() {
        titleText.setText("Setup completed");
        imageView.setVisibility(View.VISIBLE);
        finishSetupButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void processFailureUi() {
        createDeviceButton.setVisibility(View.VISIBLE);
        createDeviceButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void onNavigateNext(View nextButton) {
        setResult(RESULT_OK);
        finish();
    }
}
