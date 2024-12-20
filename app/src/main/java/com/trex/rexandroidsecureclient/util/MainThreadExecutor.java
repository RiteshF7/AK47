/*
 * Copyright (C) 2018 The Android Open Source Project
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
 * limitations under the License
 */

package com.trex.rexandroidsecureclient.util;

import android.os.Looper;

/**
 * An executor service that executes its tasks on the main thread.
 *
 * <p>Shutting down this executor is not supported.
 */
public class MainThreadExecutor extends LooperExecutor {

  public MainThreadExecutor() {
    super(Looper.getMainLooper());
  }
}
