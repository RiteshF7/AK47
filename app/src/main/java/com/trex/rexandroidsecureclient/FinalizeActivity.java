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

import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trex.rexandroidsecureclient.common.Util;
import com.trex.rexandroidsecureclient.deviceowner.actionhandlers.ActionExecuter;
import com.trex.rexandroidsecureclient.provision.ProvisioningUtil;
import com.trex.rexnetwork.Constants;
import com.trex.rexnetwork.data.ActionMessageDTO;
import com.trex.rexnetwork.data.Actions;
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater;
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager;
import com.trex.rexnetwork.utils.SharedPreferenceManager;

import java.util.HashMap;
import java.util.UUID;

public class FinalizeActivity extends Activity {

    private static final String TAG = FinalizeActivity.class.getSimpleName();
    private Button finishSetupButton;
    private TextView titleText;
    private ImageView imageView;
    private EditText imeiEditText;
    private ProgressBar progressBar;
    private FCMTokenManager fcmTokenManager;
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.finalize_activity);

        fcmTokenManager = new FCMTokenManager(this, new ClientFCMTokenUpdater(this));
        sharedPreferenceManager = new SharedPreferenceManager(this);
        fcmTokenManager.refreshToken(fcmTokenManager.getFcmToken());

        PersistableBundle extras = getIntent().getParcelableExtra(EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE);
        //saving shop id
        String shopId = extras.getString(Constants.ADMIN_SHOP_ID);
        sharedPreferenceManager.saveShopId(shopId);


        if (savedInstanceState == null) {
            if (Util.isManagedProfileOwner(this)) {
                ProvisioningUtil.enableProfile(this);
            }
        }


        finishSetupButton = findViewById(R.id.btn_complete_setup);
        imageView = findViewById(R.id.setup_complete_status_iv);

        finishSetupButton.setOnClickListener(view -> {
            Boolean isRegComplete = sharedPreferenceManager.getRegCompleteStatus();
            if (isRegComplete) {
                setResult(RESULT_OK);
                finish();
            } else {
                initDeviceRegistration();
            }
        });


//        createDeviceButton.setOnClickListener(view -> {
//            hideKeyboard();
//            deviceBuilderUtils.saveImei(imeiEditText.getText().toString().trim());
//            processProgessUi();
//            deviceBuilderUtils.createDevice(aBoolean -> {
//                runOnUiThread(() -> {
//                    if (aBoolean) {
//                        processSuccessUI();
//                    } else {
//                        processFailureUi();
//                        Toast.makeText(this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                return null;
//            });
//        });


//        if (ProvisioningUtil.isAutoProvisioningDeviceOwnerMode()) {
//            Log.i(TAG, "Automatically provisioning device onwer");
//            onNavigateNext(null);
//            return;
//        }


        Util.isDeviceOwner(this);

    }

    void initDeviceRegistration() {
        ActionMessageDTO regAction = new ActionMessageDTO("", Actions.ACTION_REG_DEVICE, new HashMap<>(), false, UUID.randomUUID().toString());
        new ActionExecuter(this).sendActionToShop(regAction);

    }


}
