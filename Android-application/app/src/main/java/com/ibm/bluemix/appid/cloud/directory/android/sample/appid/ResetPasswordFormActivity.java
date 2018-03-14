package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ibm.bluemix.appid.android.internal.network.AppIDRequest;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequestFactory;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.MainActivity.CLOUD_LAND_BACKEND_URL;
import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.ResetPasswordActivity.RESET_PASSWORD_CANCEL;
import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.ResetPasswordActivity.RESET_PASSWORD_SUCCESS;

public class ResetPasswordFormActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mRePasswordView;
    private String uuid, language, code;

    private AppIDRequestFactory appIDRequestFactory = new AppIDRequestFactory();
    private final static Logger logger = Logger.getLogger(ResetPasswordFormActivity.class.getName());
    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_form);
        getSupportActionBar().hide();
        mPasswordView = findViewById(R.id.newPassword);
        mRePasswordView = findViewById(R.id.reNewPassword);

        uuid = getIntent().getExtras().getString("uuid");
        language = getIntent().getExtras().getString("language");
        code = getIntent().getExtras().getString("code");
        progressManager = new ProgressManager(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESET_PASSWORD_CANCEL, intent);
        finish();
    }

    public void attemptSetPassword(View v) {
        progressManager.showProgress();
        JSONObject bodyObject = new JSONObject();
        try {
            bodyObject.put("new_password", mPasswordView.getText().toString());
            bodyObject.put("confirmed_new_password", mRePasswordView.getText().toString());
            bodyObject.put("uuid", uuid);
            bodyObject.put("language", language);
            bodyObject.put("code", code);

        } catch (JSONException e) {
            logger.error("Error while getting user reset password input");
            e.printStackTrace();
            progressManager.hideProgress();
            progressManager.showAlert("Failure", "Bad user input");
        }

        AppIDRequest request = appIDRequestFactory.createRequest(CLOUD_LAND_BACKEND_URL +"/reset_password/submit/mobile", AppIDRequest.POST);
        request.send(bodyObject, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    logger.debug("reset password request onSuccess");
                    progressManager.hideProgress();
                    String formattedName = response.getResponseJSON().getJSONObject("name").getString("formatted");
                    String email =  response.getResponseJSON().getJSONArray("emails").getJSONObject(0).getString("value");
                    Intent intent = new Intent();
                    intent.putExtra("formattedName", formattedName);
                    intent.putExtra("email", email);
                    setResult(RESET_PASSWORD_SUCCESS, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                logger.error("reset password request onFailure");
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
        });
    }
}
