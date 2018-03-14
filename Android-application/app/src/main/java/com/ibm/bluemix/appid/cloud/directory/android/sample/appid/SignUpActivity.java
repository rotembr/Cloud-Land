package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.ibm.bluemix.appid.android.internal.network.AppIDRequest;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequestFactory;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.MainActivity.CLOUD_LAND_BACKEND_URL;
import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.MainActivity.SIGN_UP_SUCCESS;

public class SignUpActivity extends AppCompatActivity {

    private AutoCompleteTextView mFirstNameView;
    private AutoCompleteTextView mLastNameView;
    private AutoCompleteTextView mPhoneView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mRePasswordView;

    private ProgressManager progressManager;
    private AppIDRequestFactory appIDRequestFactory = new AppIDRequestFactory();
    private final static Logger logger = Logger.getLogger(SignUpActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Sign Up");
        // Set up the sign up form.
        mFirstNameView = findViewById(R.id.firstName);
        mLastNameView = findViewById(R.id.lastName);
        mPhoneView = findViewById(R.id.phoneNumber);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mRePasswordView = findViewById(R.id.rePassword);
        progressManager = new ProgressManager(this);
    }

    public void attemptSignUp(View v) {
        progressManager.showProgress();
        JSONObject profileObject = new JSONObject();
        try {
            profileObject.put("firstName", mFirstNameView.getText().toString());
            profileObject.put("lastName", mLastNameView.getText().toString());
            profileObject.put("phoneNumber", mPhoneView.getText().toString());
            profileObject.put("email", mEmailView.getText().toString());
            profileObject.put("password", mPasswordView.getText().toString());
            profileObject.put("confirmed_password", mRePasswordView.getText().toString());
        } catch (JSONException e) {
            logger.error("Error while getting user sign up input");
            e.printStackTrace();
            progressManager.hideProgress();
            progressManager.showAlert("Failure", "Bad user input");
        }

        AppIDRequest request = appIDRequestFactory.createRequest(CLOUD_LAND_BACKEND_URL + "/sign_up/submit/mobile", AppIDRequest.POST);
        request.send(profileObject, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    logger.debug("sign up request onSuccess");
                    progressManager.hideProgress();
                    String formattedName = response.getResponseJSON().getJSONObject("name").getString("formatted");
                    String email =  response.getResponseJSON().getJSONArray("emails").getJSONObject(0).getString("value");
                    String uuid = response.getResponseJSON().getString("id");
                    Intent intent = new Intent();
                    intent.putExtra("formattedName", formattedName);
                    intent.putExtra("email", email);
                    intent.putExtra("uuid", uuid);
                    setResult(SIGN_UP_SUCCESS, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                logger.error("sign up request onFailure");
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
