package com.zafeplace.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zafeplace.sdk.R;

public class DialogUtils {

    public static void showSimpleDialog(Context context, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context,
//                R.style.SimpleDialogTheme));
//        builder.setMessage(message);
//
//        try {
//            builder.show();
//        } catch (WindowManager.BadTokenException e) {
//            e.printStackTrace();
//        }
//        mInput = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
      //  mInput.setLayoutParams(lp);
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                R.style.SimpleDialogTheme)).setTitle("Input Pin Code");
        //  builder.setView(mInput);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }
}
