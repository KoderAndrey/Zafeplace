package com.zafeplace.sample;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.zafeplace.sdk.Zafeplace;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
