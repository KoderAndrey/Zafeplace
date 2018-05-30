package com.zafeplace.sample;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.zafeplace.sdk.Zafeplace;

public class App extends MultiDexApplication {

    Zafeplace mZafeplace;

    @Override
    public void onCreate() {
        mZafeplace = Zafeplace.getInstance();
        mZafeplace.setContext(getApplicationContext());
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
