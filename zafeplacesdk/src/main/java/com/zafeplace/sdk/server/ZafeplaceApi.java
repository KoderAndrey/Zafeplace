package com.zafeplace.sdk.server;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.ContractModel;
import com.zafeplace.sdk.server.models.HexModel;
import com.zafeplace.sdk.server.models.LoginResponse;
import com.zafeplace.sdk.server.models.ResultModel;
import com.zafeplace.sdk.server.models.SmartContractTransactionRaw;
import com.zafeplace.sdk.server.models.TokenBalans;
import com.zafeplace.sdk.server.models.TransactionRaw;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.zafeplace.sdk.server.ZafeplaceApiService.ZAFEPLACE_BASE_URL;

public class ZafeplaceApi {

    private Retrofit retrofit;
    private ZafeplaceApiService service;
    private static ZafeplaceApi mZafeplaceApi;

    public static ZafeplaceApi getInstance(Context context) {
        if (mZafeplaceApi == null) {
            mZafeplaceApi = new ZafeplaceApi(context);
        }
        return mZafeplaceApi;
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

    public Call<LoginResponse> generateAccessToken(String appId, String appSecret) {
        Log.wtf("tag", "access token");
        return service.getAccessToken(appId, appSecret);
    }

    public Call<BalanceModel> getWalletBalance(String walletType, String address) {
        return service.getWalletBalance(walletType, address);
    }

    public Call<TokenBalans> getTokenBalance(String walletType, String address) {
        return service.getTokenBalance(walletType, address);
    }

    public Call<TransactionRaw> getRawTransaction(String walletType, String addressSender, String addressRecipient, double amount) {
        return service.getTransactionRaw(walletType, addressSender, addressRecipient, amount);
    }

    public Call<TransactionRaw> getTokenTransactionRaw(String walletType, String addressSender, String addressRecipient, int amount) {
        return service.getTokenTransactionRaw(walletType, addressSender, addressRecipient, amount);
    }

    public Call<SmartContractTransactionRaw> getSmartContractRaw(String walletType) {
        return service.getTransactionSmart(walletType);
    }

    public Call<ResultModel> executeContractInformationMethod(String walletType, ContractModel contractModel) {
        return service.executeContractInformationMethod(walletType, contractModel);
    }

    public Call<JsonObject> doTransaction(String signTx, String walletType) {
        return service.doTransaction(new HexModel(signTx), walletType);
    }
}

