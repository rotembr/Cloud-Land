/*
 * Copyright 2016, 2017 IBM Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.ibm.bluemix.appid.android.api.AuthorizationException;
import com.ibm.bluemix.appid.android.api.AuthorizationListener;
import com.ibm.bluemix.appid.android.api.tokens.AccessToken;
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken;
import com.ibm.bluemix.appid.android.api.tokens.RefreshToken;

/**
 * This listener provides the callback methods that are called at the end of App ID
 * authentication process when using the AppID APIs
 */
class AppIdSampleAuthorizationListener implements AuthorizationListener {
    private Activity activity;
    private ProgressManager progressManager;

    public AppIdSampleAuthorizationListener(Activity activity, ProgressManager progressManager) {
        this.activity = activity;
        this.progressManager = progressManager;
    }

    @Override
    public void onAuthorizationFailure(AuthorizationException exception) {
        Log.e(logTag("onAuthorizationFailure"),"Authorization failed", exception);
        progressManager.hideProgress();
        String errorMsg = exception.getMessage();
        if (errorMsg != null && errorMsg.contains("selfServiceEnabled is OFF") ||
                errorMsg != null && errorMsg.contains("resetPasswordEnabled is OFF")) {
            progressManager.showAlert("Oops...", "You can not perform this action");
        } else {
            progressManager.showAlert("Oops...", exception.getMessage());
        }
    }

    @Override
    public void onAuthorizationCanceled() {
        Log.w(logTag("onAuthorizationCanceled"),"Authorization canceled");
        progressManager.hideProgress();
    }

    @Override
    public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
        Log.i(logTag("onAuthorizationCanceled"),"Authorization succeeded");
        progressManager.hideProgress();
        if (accessToken != null && identityToken != null) {
            Intent intent = new Intent(activity, AfterLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }
    }

    private String logTag(String methodName){
        return this.getClass().getCanonicalName() + "." + methodName;
    }


}
