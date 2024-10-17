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

import com.trex.rexandroidsecureclient.common.Util;
import com.trex.rexandroidsecureclient.provision.ProvisioningUtil;

public class FinalizeActivity extends Activity {

    private static final String TAG = FinalizeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (Util.isManagedProfileOwner(this)) {
                ProvisioningUtil.enableProfile(this);
            }
        }

        setContentView(R.layout.finalize_activity);

//        if (ProvisioningUtil.isAutoProvisioningDeviceOwnerMode()) {
//            Log.i(TAG, "Automatically provisioning device onwer");
//            onNavigateNext(null);
//            return;
//        }


        Util.isDeviceOwner(this);

    }

    public void onNavigateNext(View nextButton) {
        setResult(RESULT_OK);
        finish();
    }
}
