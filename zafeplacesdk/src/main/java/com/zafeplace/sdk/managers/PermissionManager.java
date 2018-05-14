package com.zafeplace.sdk.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static com.zafeplace.sdk.utils.AppUtils.isNull;

public class PermissionManager {
    public final static int PERMISSION_REQUEST_CODE = 777;

    public final static short PERMISSIONS_GRANTED = 1;
    public final static short PERMISSIONS_ERROR = -1;
    public final static short PERMISSIONS_SUCCESS = 0;

    private Context mContext;
    private List<String> mPermissions;

    public PermissionManager(Context context) {
        mContext = context;
    }

    public void removeCallback() {
        if (!isNull(mPermissions)) {
            mPermissions.clear();
            mPermissions = null;
        }
        mContext = null;
    }

    public PermissionManager cameraPermission() {
        if (!isNull(mContext)) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                addPermission(Manifest.permission.CAMERA);
            }
        }
        return this;
    }

    public PermissionManager writeInternalStoragePermission() {
        if (!isNull(mContext)) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        return this;
    }

    public PermissionManager readInternalStoragePermission() {
        if (!isNull(mContext)) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        return this;
    }

    public PermissionManager callPhonePermission() {
        if (!isNull(mContext)) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                addPermission(Manifest.permission.CALL_PHONE);
            }
        }
        return this;
    }

    public PermissionManager readContactsPermission() {
        if (!isNull(mContext)) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                addPermission(Manifest.permission.READ_CONTACTS);
            }
        }
        return this;
    }

    public PermissionManager recordAudioPermission() {
        if (!isNull(mContext)) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                addPermission(Manifest.permission.RECORD_AUDIO);
            }
        }
        return this;
    }

    public short requestFor(Activity activity) {
        if (isNull(mPermissions) || mPermissions.size() == 0) return PERMISSIONS_GRANTED;
        if (isNull(mContext)) return PERMISSIONS_ERROR;

        ActivityCompat.requestPermissions(activity, getPermissions(), PERMISSION_REQUEST_CODE);

        return PERMISSIONS_SUCCESS;
    }

    public short requestFor(Fragment fragment) {
        if (isNull(mPermissions) || mPermissions.size() == 0) return PERMISSIONS_GRANTED;
        if (isNull(mContext)) return PERMISSIONS_ERROR;

        fragment.requestPermissions(getPermissions(), PERMISSION_REQUEST_CODE);

        return PERMISSIONS_SUCCESS;
    }

    public boolean checkAllGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) return false;
        }

        return true;
    }

    public boolean checkUserSelectNeverAskAgain (Activity activity, String[] permissions) {
        for (String permission : permissions) {
            //if user checked "Never ask again"
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }

        return false;
    }

    public String[] getPermissions() {
        return mPermissions.toArray(new String[0]);
    }

    private void addPermission(String permission) {
        if (isNull(mPermissions)) mPermissions = new ArrayList<>();
        if (mPermissions.contains(permission)) return;
        mPermissions.add(permission);
    }
}