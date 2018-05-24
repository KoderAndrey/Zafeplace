package com.zafeplace.sdk.server;

import android.content.Context;

import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.LoginResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.zafeplace.sdk.server.ZafeplaceApiService.ZAFEPLACE_BASE_URL;

public class ZafeplaceApi {

    private Retrofit retrofit;
    private ZafeplaceApiService service;

    public static ZafeplaceApi getInstance(Context context) {
        return new ZafeplaceApi(context);
    }

    private ZafeplaceApi(Context context) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthRequestInterceptor(context))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ZAFEPLACE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        this.service = retrofit.create(ZafeplaceApiService.class);
    }

    public Retrofit getRetrofitConnection() {
        return retrofit;
    }

    public Call<LoginResponse> getAccessToken(String packageName, String appSecret) {
        return service.getAccessToken(packageName, appSecret);
    }

    public Call<BalanceModel> getWalletBalance(String walletType, String address) {
        return service.getWalletBalance(walletType, address);
    }
}

