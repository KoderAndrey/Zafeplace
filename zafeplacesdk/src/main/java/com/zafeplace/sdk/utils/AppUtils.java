package com.zafeplace.sdk.utils;

public class AppUtils {

    public static boolean isNull(Object...objects) {
        int count = objects.length;
        for (int i = 0; i < count; i++) {
            if (objects[i] == null) return true;
        }
        return false;
    }
}
