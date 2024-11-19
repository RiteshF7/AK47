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
import android.os.UserManager;
import android.util.Log;
import android.view.Window;
import android.widget.Button;

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
import java.util.function.Consumer;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class FinalizeActivity extends Activity {

    private static final String TAG = FinalizeActivity.class.getSimpleName();
    private Button finishSetupButton;
    private FCMTokenManager fcmTokenManager;
    private SharedPreferenceManager sharedPreferenceManager;
    private DevicePolicyManagerGatewayImpl mDevicePolicyManagerGateway;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.finalize_activity);
        mDevicePolicyManagerGateway = new DevicePolicyManagerGatewayImpl(this);
        hideAppFromDrawer();
        blockAppUninstallation();
        setUserRestrictions(mDevicePolicyManagerGateway);
        fcmTokenManager = new FCMTokenManager(this, new ClientFCMTokenUpdater(this));
        sharedPreferenceManager = new SharedPreferenceManager(this);
        fcmTokenManager.refreshToken(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                return null;
            }
        });

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

    public void blockAppUninstallation() {
        // Success callback
        Consumer<Void> successCallback = (Void) -> {
            Log.d("UninstallBlock", "Uninstall blocking successful");
            // Additional success handling
        };

        // Error callback
        Consumer<Exception> errorCallback = (Exception e) -> {
            Log.e("UninstallBlock", "Uninstall blocking failed", e);
            // Error handling logic
        };

        // Call the method
        mDevicePolicyManagerGateway.setUninstallBlocked(
                this.getPackageName(),
                true,  // true to block uninstallation
                successCallback,
                errorCallback
        );
    }

    void hideAppFromDrawer() {
        // Success callback
        Consumer<Void> successCallback = (Void) -> {
            Log.d("Hidden block", "App hide successful");
            // Additional success handling
        };

        // Error callback
        Consumer<Exception> errorCallback = (Exception e) -> {
            Log.e("hidden error block", "App hide failed", e);
            // Error handling logic
        };
        mDevicePolicyManagerGateway.setApplicationHidden(this.getPackageName(), true, successCallback, errorCallback);


    }

    void setUserRestrictions(DevicePolicyManagerGatewayImpl mDevicePolicyManagerGateway) {
        mDevicePolicyManagerGateway.setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, true);
        mDevicePolicyManagerGateway.setUserRestriction(UserManager.DISALLOW_DEBUGGING_FEATURES, true);

    }

}
