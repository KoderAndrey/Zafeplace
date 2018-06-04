package com.zafeplace.sdk.utils;

import android.util.Log;

import com.google.gson.JsonObject;

public class ParseUtils {
    public static String transactionMessage(JsonObject jsonObject) {
        String result = "";
        String mess = jsonObject.getAsJsonPrimitive("message").getAsString();
        Log.wtf("sho tut ", " a suka " + mess);
        result += mess;
        return result;
    }
}
