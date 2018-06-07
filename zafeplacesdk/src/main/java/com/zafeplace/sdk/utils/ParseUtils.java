package com.zafeplace.sdk.utils;

import android.util.Log;

import com.google.gson.JsonObject;

public class ParseUtils {
    public static String transactionMessage(JsonObject jsonObject) {
        String result = "";
        String mess = jsonObject.getAsJsonPrimitive("message").getAsString();
        result += mess;
        return result;
    }
}
