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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ibm.bluemix.appid.android.api.AppID;
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager;
import com.ibm.bluemix.appid.android.api.tokens.AccessToken;
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequest;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequestFactory;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.MainActivity.CLOUD_LAND_BACKEND_URL;

/**
 * This Activity starts after pressing on "login" button.
 */
public class AfterLoginActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AppIDAuthorizationManager appIdAuthorizationManager;
    private ProgressManager progressManager;
    private final static Logger logger = Logger.getLogger(AfterLoginActivity.class.getName());

    public final static int CHANGE_DETAILS_SUCCESS = 5;
    public final static int CHANGE_PASSWORD_SUCCESS = 6;
    public final static int CHANGE_DETAILS_CANCELED = 7;
    public final static int CHANGE_PASSWORD_CANCELED = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        //Creating the attributes list view with adapter
        AppID appId = AppID.getInstance();
        appIdAuthorizationManager = new AppIDAuthorizationManager(appId);
        progressManager = new ProgressManager(this);
        getSupportActionBar().hide();
        setUserInfoToUI(appIdAuthorizationManager.getIdentityToken());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(logTag("AfterLoginActivity"),"onResume");
        final ListView listview = findViewById(R.id.listviewFood);
        final AppIdAttributesListAdapter adapter = new AppIdAttributesListAdapter(this, isFoodSelectionAllowed(), progressManager);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(adapter.getOnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (resultCode == CHANGE_DETAILS_SUCCESS) {
            String formattedName = data.getExtras().getString("formattedName");
            updateUserNameUI(formattedName);
            progressManager.hideProgress();
            progressManager.showAlert("Success", "Your profile details has been successfully changed");
        }
        if (resultCode == CHANGE_PASSWORD_SUCCESS) {
            progressManager.hideProgress();
            progressManager.showAlert("Success", "Your password has been successfully changed");
        }

    }

    private void updateUserNameUI(String userName) {
        String hiUser  = getString(R.string.greet) + " " + userName;
        ((TextView) findViewById(R.id.userName)).setText(hiUser);
    }
    private void setUserInfoToUI(IdentityToken idt) {
        //Getting information from identity token. This is information that is coming from the identity provider.
        String userName = idt.getEmail().split("@")[0];
        if(idt.getName() != null) {
            userName = idt.getName();
        }
        updateUserNameUI(userName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.user_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountDetails:
                progressManager.showProgress();
                Log.d(logTag("AfterLoginActivity "),"change details clicked");

                AppIDRequest request = new AppIDRequestFactory().createRequest(CLOUD_LAND_BACKEND_URL + "/ibm/cloud/appid/cloudLand/mobile/get_user_details", AppIDRequest.GET);
                request.send(new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        try {
                            logger.debug("change details request onSuccess");
                            Intent intent = new Intent(getApplicationContext(), ChangeDetailsActivity.class);
                            intent.putExtra("firstName", response.getResponseJSON().getString("firstName"));
                            intent.putExtra("lastName", response.getResponseJSON().getString("lastName"));
                            intent.putExtra("phoneNumber", response.getResponseJSON().getString("phoneNumber"));
                            intent.putExtra("email", response.getResponseJSON().getString("email"));
                            startActivityForResult(intent, CHANGE_DETAILS_SUCCESS);
                        } catch (JSONException e) {
                            progressManager.hideProgress();
                            progressManager.showAlert("Failure", "Could not get user details");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                        logger.error("change details request onFailure");
                        if (response != null) {
                            logger.error("response: " + response.toString());
                        } else if (extendedInfo != null) {
                            logger.error("extendedInfo: " + extendedInfo.toString());
                        } else if (t != null) {
                            logger.error("exception: " + t.getMessage());
                        } else {
                            logger.error("Request Failure");
                        }
                        progressManager.hideProgress();
                        progressManager.showAlert("Failure", "Could not get user details");
                    }
                }, appIdAuthorizationManager.getAccessToken(), appIdAuthorizationManager.getIdentityToken());

                return true;
            case R.id.changePassword:
                Log.d(logTag("AfterLoginActivity "),"change password clicked");
                startActivityForResult(new Intent(getApplicationContext(), ChangePasswordActivity.class), CHANGE_PASSWORD_SUCCESS);
                return true;
            case R.id.logOut:
                Log.d(logTag("AfterLoginActivity "),"log out clicked");
                logOutClicked();
                return true;
            default:
                progressManager.hideProgress();
                return false;
        }
    }

    private void logOutClicked() {
        appIdAuthorizationManager.logout(this, null);
        //returning to login activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        this.finish();
    }

    /**
     * Show textual representation of Access and Identity tokens
     * @param v
     */
    public void onTokenViewClick(View v) {
        Log.d(logTag("AfterLoginActivity"),"onTokenViewClick");
        Intent intent = new Intent(this, TokenActivity.class);
        IdentityToken idt = appIdAuthorizationManager.getIdentityToken();
        AccessToken at = appIdAuthorizationManager.getAccessToken();
        try {
            intent.putExtra("idToken", idt.getPayload().toString(2));
            intent.putExtra("accessToken", at.getPayload().toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    /**
     * return true if email verification is required and user email verified, or if email verification not required.
     * else return false.
     */
    private Boolean isFoodSelectionAllowed() {
        boolean isFoodSelectionAllowed = true;
        if (isEmailVerificationRequired()){
            isFoodSelectionAllowed = isUserEmailVerified();
        }
        Log.d(logTag("AfterLoginActivity"),"isFoodSelectionAllowed: " + isFoodSelectionAllowed);
        return isFoodSelectionAllowed;
    }

    /**
     * return true if idToken contains property name "email_verified" and it sets to true,
     * else return false.
     */
    private Boolean isUserEmailVerified() {
        try {
            JSONObject tokenPayload = appIdAuthorizationManager.getIdentityToken().getPayload();
            return tokenPayload.getBoolean("email_verified");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * isEmailVerificationRequired
     * @return true if idToken contains property name "email_verified", else return false.
     */
    private Boolean isEmailVerificationRequired() {
        JSONObject tokenPayload = appIdAuthorizationManager.getIdentityToken().getPayload();
        return tokenPayload.has("email_verified");
    }

    private String logTag(String methodName){
        return getClass().getCanonicalName() + "." + methodName;
    }
}
