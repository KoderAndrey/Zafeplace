package com.zafeplace.sdk.server;

import android.content.Context;

import com.zafeplace.sdk.managers.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.zafeplace.sdk.utils.AppUtils.isNull;

public class AuthRequestInterceptor implements Interceptor {
    private static final String AUTHENTICATION = "Authorization";
    private static final String AUTH_PREPOSITION = "";
    private PreferencesManager mManager;
    private Context context;

    public AuthRequestInterceptor(Context context) {
        this.context = context;
        mManager = new PreferencesManager();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String authToken = mManager.getAuthToken(context);
        String token = AUTH_PREPOSITION;
        if (!isNull(authToken)) token += authToken;
        Request.Builder signedRequest = chain.request().newBuilder();
        signedRequest.addHeader(AUTHENTICATION, token);
        return chain.proceed(signedRequest.build());
    }
}