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

import com.trex.rexandroidsecureclient.common.Util;
import com.trex.rexandroidsecureclient.myclient.ui.initdeviceregscreen.InitDeviceRegistrationActivity;
import com.trex.rexandroidsecureclient.provision.ProvisioningUtil;
import com.trex.rexnetwork.Constants;
import com.trex.rexnetwork.domain.firebasecore.fcm.ClientFCMTokenUpdater;
import com.trex.rexnetwork.domain.firebasecore.fcm.FCMTokenManager;
import com.trex.rexnetwork.utils.SharedPreferenceManager;

import java.util.function.Consumer;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class FinalizeActivity extends Activity {

    private static final String TAG = FinalizeActivity.class.getSimpleName();
    private FCMTokenManager fcmTokenManager;
    private SharedPreferenceManager sharedPreferenceManager;
    private DevicePolicyManagerGatewayImpl mDevicePolicyManagerGateway;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDevicePolicyManagerGateway = new DevicePolicyManagerGatewayImpl(this);


        if (savedInstanceState == null) {
            if (Util.isManagedProfileOwner(this)) {
                ProvisioningUtil.enableProfile(this);
            }
        }

        //saving shop id
        PersistableBundle extras = getIntent().getParcelableExtra(EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE);
        sharedPreferenceManager = new SharedPreferenceManager(this);
        String shopId = extras.getString(Constants.ADMIN_SHOP_ID);
        String deviceId = extras.getString(Constants.ADMIN_DEVICE_ID);
        sharedPreferenceManager.saveShopId(shopId);
        sharedPreferenceManager.saveDeviceId(deviceId);


        //initial setup
//        hideAppFromDrawer();
//        blockAppUninstallation();
//        setUserRestrictions(mDevicePolicyManagerGateway);


        //updating token of device on server
        fcmTokenManager = new FCMTokenManager(this, new ClientFCMTokenUpdater(this));
        fcmTokenManager.refreshToken(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {

                return null;
            }
        });
        InitDeviceRegistrationActivity.Companion.go(this);
        setResult(RESULT_OK);
        finish();

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
