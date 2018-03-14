package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

public class ResetPasswordActivity extends AppCompatActivity {

    private final static Logger logger = Logger.getLogger(ResetPasswordActivity.class.getName());
    public final static int RESET_PASSWORD_SUCCESS = 4;
    public final static int RESET_PASSWORD_CANCEL = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();

        Uri encodeUri = this.getIntent().getData();
        String decodeUrl = Uri.decode(encodeUri.toString());
        Uri uri = Uri.parse(decodeUrl);
        logger.debug("uri:" + uri);
        String uuid = uri.getQueryParameter("uuid");
        String language = uri.getQueryParameter("language");
        String code = uri.getQueryParameter("code");

        String errorStatusCode = uri.getQueryParameter("errorStatusCode");
        String errorDescription = uri.getQueryParameter("errorDescription");
        if (null != errorStatusCode) {
            TextView textViewSignUpResult = findViewById(R.id.textViewResetPasswordResult);
            textViewSignUpResult.setText(errorDescription);
            findViewById(R.id.backTologin).setVisibility(View.GONE);
            findViewById(R.id.backTologin).setEnabled(false);
        } else {
            logger.debug("start reset password form activity");
            Intent intent = new Intent(getApplicationContext(), ResetPasswordFormActivity.class);
            intent.putExtra("uuid", uuid);
            intent.putExtra("code", code);
            intent.putExtra("language", language);
            startActivityForResult(intent, RESET_PASSWORD_SUCCESS);
        }
    }

    public void backToLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESET_PASSWORD_CANCEL) {
            logger.debug("reset password canceled");
            finish();
            return;
        }
        if (resultCode == RESET_PASSWORD_SUCCESS) {
            logger.debug("reset password success");
            String successMsg = data.getExtras().getString("formattedName") + " your password changed successfully";
            TextView textViewSignUpResult = findViewById(R.id.textViewResetPasswordResult);
            textViewSignUpResult.setText(successMsg);
        }
    }
}
