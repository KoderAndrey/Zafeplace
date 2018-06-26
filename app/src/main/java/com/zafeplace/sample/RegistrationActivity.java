package com.zafeplace.sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zafeplace.sdk.Zafeplace;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zafeplace.sdk.Constants.AuthType.FINGERPRINT_AUTH;
import static com.zafeplace.sdk.Constants.AuthType.PIN_AUTH;

public class RegistrationActivity extends AppCompatActivity {

    private Zafeplace mZafeplace;
    private Unbinder mUnbinder;
    public static int REQUEST_WRITE_EXTERNAL_STORAGE = 111;
    public static int REQUEST_FINGER_PRINT_EXTERNAL = 112;

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
        checkPermissionWriteExternalStorageAndFingerPrint();
    }

    public void pincode(View view) {
        checkPermissionWriteExternalStorage();
    }

    private void checkPermissionWriteExternalStorage() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            LoginActivity.start(this, PIN_AUTH, true);
        }
    }


    private void checkPermissionWriteExternalStorageAndFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                Toast.makeText(this, "Device don't support fingerprint", Toast.LENGTH_SHORT).show();
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                Toast.makeText(this, "You don't have any fingerprints to authenticate", Toast.LENGTH_SHORT).show();
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.USE_FINGERPRINT},
                            REQUEST_FINGER_PRINT_EXTERNAL);
                } else {
                    LoginActivity.start(this, FINGERPRINT_AUTH, true);
                }
            }
        } else {
            Toast.makeText(this, "Device don't support fingerprint", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoginActivity.start(this, PIN_AUTH, true);
            } else {
                Toast.makeText(this, "We need this permission for app. Please access this permission", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_FINGER_PRINT_EXTERNAL) {
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                LoginActivity.start(this, FINGERPRINT_AUTH, true);
            } else {
                Toast.makeText(this, "We need this permission for app. Please access this permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
