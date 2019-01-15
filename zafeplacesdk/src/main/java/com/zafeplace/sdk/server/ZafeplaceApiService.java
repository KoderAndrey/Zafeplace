package com.zafeplace.sdk.server;

import com.google.gson.JsonObject;
import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.ContractModel;
import com.zafeplace.sdk.server.models.HexModel;
import com.zafeplace.sdk.server.models.LoginResponse;
import com.zafeplace.sdk.server.models.ResultModel;
import com.zafeplace.sdk.server.models.SmartContractTransactionRaw;
import com.zafeplace.sdk.server.models.TokenBalans;
import com.zafeplace.sdk.server.models.TransactionRaw;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZafeplaceApiService {

    String ZAFEPLACE_BASE_URL = "http://35.233.100.41:3000/api/v1/";

    @GET("sdk/session/login")
    Call<LoginResponse> getAccessToken(@Query("appId") String appId,
                                       @Query("appSecret") String appSecret);

    @GET("sdk/{network}/account/balance")
    Call<BalanceModel> getWalletBalance(@Path("network") String walletType,
                                        @Query("address") String address);

    @GET("sdk/{network}/account/token-balance")
    Call<TokenBalans> getTokenBalance(@Path("network") String walletType,
                                      @Query("addre" +
                                              "ss") String address);

    @GET("sdk/{network}/account/native-coin-raw-transaction")
    Call<TransactionRaw> getTransactionRaw(@Path("network") String walletType,
                                           @Query("sender") String addressSender,
                                           @Query("recipient") String addressRecipient,
                                           @Query("amount") double amount);

    @GET("sdk/{network}/account/token-raw-transaction")
    Call<TransactionRaw> getTokenTransactionRaw(@Path("network") String walletType,
                                                @Query("sender") String addressSender,
                                                @Query("recipient") String addressRecipient,
                                                @Query("amount") int amount);

    @Headers("Content-Type: application/json")
    @POST("sdk/{network}/account/send-transaction")
    Call<JsonObject> doTransaction(@Body HexModel hexModel,
                                   @Path("network") String walletType);

    @GET("sdk/{network}/contract/abi")
    Call<SmartContractTransactionRaw> getTransactionSmart(@Path("network") String walletType);

    @POST("sdk/{network}/contract/execute-method")
    Call<ResultModel> executeContractInformationMethod(@Path("network") String network,
                                                          @Body ContractModel contractModel);

    @GET("sdk/{network}/account/change-trust")
    Call<TransactionRaw> changeTrust(@Path("network") String network,
                             @Query("recipient") String recipient ,
                             @Query("limit") Double limit);
}