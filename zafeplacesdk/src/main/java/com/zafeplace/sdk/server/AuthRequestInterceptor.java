package com.zafeplace.sdk.server;

import android.content.Context;
import android.util.Log;

import com.zafeplace.sdk.Zafeplace;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.zafeplace.sdk.managers.PreferencesManager.getAuthToken;
import static com.zafeplace.sdk.utils.AppUtils.isNull;

public class AuthRequestInterceptor implements Interceptor {
    private static final String AUTHENTICATION = "Authorization";
    private static final String AUTH_PREPOSITION = "";

    private Context context;

    public AuthRequestInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String authToken = getAuthToken(context);
        String token = AUTH_PREPOSITION;
        if (!isNull(authToken)) token += authToken;
        Request.Builder signedRequest = chain.request().newBuilder();
        signedRequest.addHeader(AUTHENTICATION, token);
        return chain.proceed(signedRequest.build());
    }
}