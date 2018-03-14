package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ibm.bluemix.appid.android.api.AppID;
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequest;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequestFactory;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.AfterLoginActivity.CHANGE_PASSWORD_CANCELED;
import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.AfterLoginActivity.CHANGE_PASSWORD_SUCCESS;
import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.MainActivity.CLOUD_LAND_BACKEND_URL;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText mPasswordView, mRePasswordView, mCurrentPassword;
    private AppIDRequestFactory appIDRequestFactory = new AppIDRequestFactory();
    private final static Logger logger = Logger.getLogger(ResetPasswordFormActivity.class.getName());
    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Change password");
        mCurrentPassword = findViewById(R.id.currentPassword);
        mPasswordView = findViewById(R.id.newPassword);
        mRePasswordView = findViewById(R.id.reNewPassword);
        progressManager = new ProgressManager(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(CHANGE_PASSWORD_CANCELED, intent);
        finish();
    }

    public void attemptChangePassword(View v) {
        progressManager.showProgress();

        JSONObject bodyObject = new JSONObject();
        try {
            bodyObject.put("current_password", mCurrentPassword.getText().toString());
            bodyObject.put("new_password", mPasswordView.getText().toString());
            bodyObject.put("confirmed_new_password", mRePasswordView.getText().toString());
        } catch (JSONException e) {
            logger.error("Error while getting user change password input");
            e.printStackTrace();
            progressManager.hideProgress();
            progressManager.showAlert("Failure", "Bad user input");
        }

        AppIDAuthorizationManager appIdAuthorizationManager = new AppIDAuthorizationManager(AppID.getInstance());
        AppIDRequest request = appIDRequestFactory.createRequest(CLOUD_LAND_BACKEND_URL + "/change_password/submit/mobile", AppIDRequest.POST);
        request.send(bodyObject, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    logger.debug("change password request onSuccess");
                    progressManager.hideProgress();
                    String formattedName = response.getResponseJSON().getJSONObject("name").getString("formatted");
                    String email =  response.getResponseJSON().getJSONArray("emails").getJSONObject(0).getString("value");
                    Intent intent = new Intent();
                    intent.putExtra("formattedName", formattedName);
                    intent.putExtra("email", email);
                    setResult(CHANGE_PASSWORD_SUCCESS, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                logger.error("change password request onFailure");
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
                if (response.getStatus() >= 400 && response.getStatus() < 500) {
                    progressManager.showAlert("Failure", response.getResponseText());
                } else {
                    progressManager.showAlert("Failure", "Something went wrong, try again later");
                }
            }
        }, appIdAuthorizationManager.getAccessToken(), appIdAuthorizationManager.getIdentityToken());
    }
}
