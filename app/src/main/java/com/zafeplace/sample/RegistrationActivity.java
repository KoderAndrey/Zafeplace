package com.zafeplace.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnAccessTokenListener;
import com.zafeplace.sdk.utils.DialogUtils;
import com.zafeplace.sdk.utils.FingerprintHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zafeplace.sample.Constants.FINGERPRINT;
import static com.zafeplace.sample.Constants.PINCODE;
import static com.zafeplace.sdk.Constants.AuthType.FINGERPRINT_AUTH;
import static com.zafeplace.sdk.Constants.AuthType.PIN_AUTH;

public class RegistrationActivity extends AppCompatActivity {

    private Zafeplace mZafeplace;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mUnbinder = ButterKnife.bind(this);
        mZafeplace = Zafeplace.getInstance(this);
        if (mZafeplace.isLoggedIn()) {
            LoginActivity.start(this, mZafeplace.getAuthType(), false);
        }
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    public void fingerprint(View view) {
        LoginActivity.start(this, FINGERPRINT_AUTH, true);
    }

    public void pincode(View view) {
        LoginActivity.start(this, PIN_AUTH, true);
    }
}
