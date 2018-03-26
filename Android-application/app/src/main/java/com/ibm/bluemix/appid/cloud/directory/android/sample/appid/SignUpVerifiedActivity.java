package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

public class SignUpVerifiedActivity extends AppCompatActivity {

    private final static Logger logger = Logger.getLogger(SignUpVerifiedActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verified);
        getSupportActionBar().hide();

        Uri encodeUri = this.getIntent().getData();
        String decodeUrl = Uri.decode(encodeUri.toString());

        Uri uri = Uri.parse(decodeUrl);

        logger.debug("uri:" + uri);

        String uuid = uri.getQueryParameter("uuid");
        String language = uri.getQueryParameter("language");
        String errorStatusCode = uri.getQueryParameter("errorStatusCode");
        String errorDescription = uri.getQueryParameter("errorDescription");
        if (null != errorDescription) {
            TextView textViewSignUpResult = findViewById(R.id.textViewSignUpResult);
            textViewSignUpResult.setText(errorDescription);
            findViewById(R.id.backTologin).setVisibility(View.GONE);
            findViewById(R.id.backTologin).setEnabled(false);
        }
    }

    public void backToLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
