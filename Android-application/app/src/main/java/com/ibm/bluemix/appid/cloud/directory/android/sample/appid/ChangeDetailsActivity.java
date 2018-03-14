package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.ibm.bluemix.appid.android.api.AppID;
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequest;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequestFactory;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.AfterLoginActivity.CHANGE_DETAILS_CANCELED;
import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.AfterLoginActivity.CHANGE_DETAILS_SUCCESS;
import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.MainActivity.CLOUD_LAND_BACKEND_URL;

public class ChangeDetailsActivity extends AppCompatActivity {

    private AutoCompleteTextView mFirstNameView;
    private AutoCompleteTextView mLastNameView;
    private AutoCompleteTextView mPhoneView;
    private AutoCompleteTextView mEmailView;

    private ProgressManager progressManager;
    private AppIDRequestFactory appIDRequestFactory = new AppIDRequestFactory();
    private final static Logger logger = Logger.getLogger(SignUpActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Change details");

        mFirstNameView = findViewById(R.id.firstName);
        mLastNameView = findViewById(R.id.lastName);
        mPhoneView = findViewById(R.id.phoneNumber);
        mEmailView = findViewById(R.id.email);
        progressManager = new ProgressManager(this);

        mFirstNameView.setText(getIntent().getExtras().getString("firstName"));
        mLastNameView.setText(getIntent().getExtras().getString("lastName"));
        mPhoneView.setText(getIntent().getExtras().getString("phoneNumber"));
        mEmailView.setText(getIntent().getExtras().getString("email"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(CHANGE_DETAILS_CANCELED, intent);
        finish();
    }

    public void attemptSaveChanges(View v) {
        progressManager.showProgress();
        JSONObject profileObject = new JSONObject();
        try {
            profileObject.put("firstName", mFirstNameView.getText().toString());
            profileObject.put("lastName", mLastNameView.getText().toString());
            profileObject.put("phoneNumber", mPhoneView.getText().toString());
        } catch (JSONException e) {
            logger.error("Error while getting user sign up input");
            e.printStackTrace();
            progressManager.hideProgress();
            progressManager.showAlert("Failure", "Bad user input");
        }

        AppIDAuthorizationManager appIdAuthorizationManager = new AppIDAuthorizationManager(AppID.getInstance());
        AppIDRequest request = appIDRequestFactory.createRequest(CLOUD_LAND_BACKEND_URL + "/change_details/submit/mobile", AppIDRequest.POST);
        request.send(profileObject, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    logger.debug("change details request onSuccess");
                    progressManager.hideProgress();
                    String formattedName = response.getResponseJSON().getJSONObject("name").getString("formatted");
                    String email =  response.getResponseJSON().getJSONArray("emails").getJSONObject(0).getString("value");
                    String uuid = response.getResponseJSON().getString("id");
                    Intent intent = new Intent();
                    intent.putExtra("formattedName", formattedName);
                    intent.putExtra("email", email);
                    intent.putExtra("uuid", uuid);
                    setResult(CHANGE_DETAILS_SUCCESS, intent);
                    finish();
                } catch (JSONException e) {
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
                if (response.getStatus() >= 400 && response.getStatus() < 500) {
                    progressManager.showAlert("Failure", response.getResponseText());
                } else {
                    progressManager.showAlert("Failure", "Something went wrong, try again later");
                }
            }
        }, appIdAuthorizationManager.getAccessToken(), appIdAuthorizationManager.getIdentityToken());
    }
}
