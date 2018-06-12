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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZafeplaceApiService {

    String ZAFEPLACE_BASE_URL = "http://35.233.100.41:3000/";

    @GET("/app/session/login")
    Call<LoginResponse> getAccessToken(@Query("appId") String appId,
                                       @Query("appSecret") String appSecret);

    @GET("/app/{network}/account/balance")
    Call<BalanceModel> getWalletBalance(@Path("network") String walletType,
                                        @Query("address") String address);

    @GET("/app/{network}/account/token-balance")
    Call<TokenBalans> getTokenBalance(@Path("network") String walletType,
                                      @Query("address") String address);

    @GET("/app/{network}/account/native-coin/rawtx")
    Call<TransactionRaw> getTransactionRaw(@Path("network") String walletType,
                                           @Query("sender") String addressSender,
                                           @Query("recipient") String addressRecipient,
                                           @Query("amount") double amount);

    @GET("/app/{network}/account/token-transfer/rawtx")
    Call<TransactionRaw> getTokenTransactionRaw(@Path("network") String walletType,
                                                @Query("sender") String addressSender,
                                                @Query("recipient") String addressRecipient,
                                                @Query("amount") int amount);

    @Headers("Content-Type: application/json")
    @POST("/app/{network}/account/send-tx")
    Call<JsonObject> doTransaction(@Body HexModel hexModel,
                                   @Path("network") String walletType);

    @GET("/app/{network}/contract/abi")
    Call<SmartContractTransactionRaw> getTransactionSmart(@Path("network") String walletType);

    @POST("/app/{network}/contract/execute-method")
    Call<ResultModel> executeContractInformationMethod(@Path("network") String network,
                                                        @Body ContractModel contractModel);
}
