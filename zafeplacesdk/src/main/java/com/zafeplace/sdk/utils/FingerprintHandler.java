package com.zafeplace.sdk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.zafeplace.sdk.R;

import static com.zafeplace.sdk.Constants.AuthType.FINGERPRINT_AUTH;
import static com.zafeplace.sdk.managers.PreferencesManager.setAuthType;
import static com.zafeplace.sdk.managers.PreferencesManager.setIsLoggedIn;

public class FingerprintHandler extends FingerprintManagerCompat.AuthenticationCallback {

    private Context context;
    private FingerprintAuthenticationCallback fingerprintAuthenticationCallback;

    public interface FingerprintAuthenticationCallback{
        void onResponse(String message,boolean isSuccess);
    }
    public FingerprintHandler(Context context,FingerprintAuthenticationCallback fingerprintAuthenticationCallback) {
        this.context = context;
        this.fingerprintAuthenticationCallback = fingerprintAuthenticationCallback;
    }

    public void startAuth(FingerprintManagerCompat manager, FingerprintManagerCompat.CryptoObject cryptoObject) {
        android.support.v4.os.CancellationSignal cancellationSignal = new android.support.v4.os.CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, 0, cancellationSignal,this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void update(String e, Boolean success){
        if(success){
            setIsLoggedIn(true, context);
            setAuthType(FINGERPRINT_AUTH, context);
        }
        fingerprintAuthenticationCallback.onResponse(e,success);
    }
}
