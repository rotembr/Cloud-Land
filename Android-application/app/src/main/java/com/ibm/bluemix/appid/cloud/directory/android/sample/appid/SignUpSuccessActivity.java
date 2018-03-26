package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ibm.bluemix.appid.android.internal.network.AppIDRequest;
import com.ibm.bluemix.appid.android.internal.network.AppIDRequestFactory;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ibm.bluemix.appid.cloud.directory.android.sample.appid.MainActivity.CLOUD_LAND_BACKEND_URL;

public class SignUpSuccessActivity extends AppCompatActivity {

    private String email, uuid;
    private AppIDRequestFactory appIDRequestFactory = new AppIDRequestFactory();
    private final static Logger logger = Logger.getLogger(SignUpSuccessActivity.class.getName());
    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);
        getSupportActionBar().hide();

        String formattedName = getIntent().getExtras().getString("formattedName");
        email = getIntent().getExtras().getString("email");
        uuid = getIntent().getExtras().getString("uuid");
        TextView nameTextView = findViewById(R.id.formattedName);
        nameTextView.setText(formattedName);
        progressManager = new ProgressManager(this);
    }

    public void resendEmail(View v) {
        logger.debug("resending confirmation email: " + email);
        progressManager.showProgress();
        JSONObject body = new JSONObject();
        try {
            body.put("uuid", uuid);
        } catch (JSONException e) {
            logger.error("Error while getting user resend input");
            e.printStackTrace();
            progressManager.hideProgress();
            progressManager.showAlert("Failure", "Bad email address");
        }

        AppIDRequest request = appIDRequestFactory.createRequest(CLOUD_LAND_BACKEND_URL + "/resend/USER_VERIFICATION", AppIDRequest.POST);
        request.send(body, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                logger.debug("resend email request onSuccess");
                progressManager.hideProgress();
                progressManager.showAlert("Resend", response.getResponseText());
            }

            @Override
            public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
                logger.error("resend email request onFailure");
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
                progressManager.showAlert("Failure", "Something went wrong, try again later");
            }
        });
    }
}
