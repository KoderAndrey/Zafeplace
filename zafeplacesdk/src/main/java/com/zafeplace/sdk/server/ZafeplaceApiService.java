package com.zafeplace.sdk.server;

import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZafeplaceApiService {

    String ZAFEPLACE_BASE_URL = "http://35.233.100.41:3000/";

    @GET("/app/session/login")
    Call<LoginResponse> getAccessToken(@Query("packageName") String packageName,
                                       @Query("appSecret") String appSecret);

    @GET("/app/{network}/account/balance")
    Call<BalanceModel> getWalletBalance(@Path("network") String walletType,@Query("address") String address);
}
