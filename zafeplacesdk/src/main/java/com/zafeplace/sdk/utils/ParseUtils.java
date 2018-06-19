package com.zafeplace.sdk.utils;

import com.google.gson.JsonObject;

public class ParseUtils {
    public static String transactionMessage(JsonObject jsonObject) {
        String result = "";
        String mess = "";
        if (jsonObject.has("message")) {
            mess = jsonObject.getAsJsonPrimitive("message").getAsString();
        }
        if (jsonObject.has("txHash")) {
            mess = jsonObject.getAsJsonPrimitive("txHash").getAsString();
        }
        result += mess;
        return result;
    }
}
