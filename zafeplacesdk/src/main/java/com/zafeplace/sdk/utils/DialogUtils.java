package com.zafeplace.sdk.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;

import com.zafeplace.sdk.R;

public class DialogUtils {

    public static void showSimpleDialog(Activity context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                R.style.SimpleDialogTheme));
        builder.setMessage(message);

        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }
}
