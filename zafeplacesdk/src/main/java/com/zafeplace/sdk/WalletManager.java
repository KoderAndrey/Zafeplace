package com.zafeplace.sdk;

import android.app.Activity;
import android.util.Log;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRawList;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.managers.PreferencesManager;
import com.zafeplace.sdk.server.ZafeplaceApi;
import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.ResultToken;
import com.zafeplace.sdk.server.models.TokenBalans;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zafeplace.sdk.utils.WalletUtils.getWalletName;

public abstract class WalletManager {

    private ExecutorService mExecutor;
    private PreferencesManager mPreferencesManager;

    public WalletManager() {
        mPreferencesManager = new PreferencesManager();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public abstract void generateWallet(OnWalletGenerateListener onWalletGenerateListener, Activity activity, boolean isLoggedIn);

    public void getWalletBalance(String address, final OnGetWalletBalance onGetWalletBalance, Activity activity) {
        ZafeplaceApi.getInstance(activity).getWalletBalance(getWalletName(getWalletType()), address).enqueue(new Callback<BalanceModel>() {
            @Override
            public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                onGetWalletBalance.onWalletBalance(response.body());
            }

            @Override
            public void onFailure(Call<BalanceModel> call, Throwable t) {
                onGetWalletBalance.onErrorWalletBalance(t);
            }
        });
    }

    public void getTokenBalance(String address, final OnGetTokenBalance onGetTokenBalance, Activity activity) {
        ZafeplaceApi.getInstance(activity).getTokenBalance(getWalletName(getWalletType()), address).enqueue(new Callback<TokenBalans>() {
            @Override
            public void onResponse(Call<TokenBalans> call, Response<TokenBalans> response) {
                try {
                    TokenBalans tokenBalans = response.body();
                    Log.wtf("tag", "result stellar " + tokenBalans.toString());
                    List<ResultToken> resultTokens = tokenBalans.result;
                    onGetTokenBalance.onTokenBalance(resultTokens);
                } catch (Exception e) {
                    onGetTokenBalance.onErrorTokenBalance(e);
                }
            }

            @Override
            public void onFailure(Call<TokenBalans> call, Throwable t) {
                onGetTokenBalance.onErrorTokenBalance(t);
            }
        });
    }

    public void makeTransaction(Zafeplace.WalletTypes walletType, String addressSender, String addressRecipient, double amount,
                                final OnMakeTransaction onMakeTransaction) {
    }

    public void makeTransactionToken(Zafeplace.WalletTypes walletType, String addressSender, String addressRecipient, int amount,
                                     final OnMakeTransaction onMakeTransaction) {
    }

    public void getSmartContractTransactionRaw(final Zafeplace.WalletTypes walletType, final OnSmartContractRawList onSmartContractRaw) {
    }

    protected PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    protected ExecutorService getExecutor() {
        return mExecutor;
    }

    public abstract Zafeplace.WalletTypes getWalletType();
}
