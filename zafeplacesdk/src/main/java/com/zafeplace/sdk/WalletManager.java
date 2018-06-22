package com.zafeplace.sdk;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;

import com.google.gson.JsonObject;
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
import com.zafeplace.sdk.utils.ParseUtils;

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

    private void doTransaction(Zafeplace.WalletTypes walletType, String signTx, final OnMakeTransaction onMakeTransaction, Activity activity) {
        ZafeplaceApi.getInstance(activity).doTransaction(signTx, getWalletName(walletType)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    onMakeTransaction.onSuccessTransaction(ParseUtils.transactionMessage(response.body()));
                } catch (Exception e) {
                    e.printStackTrace();
                    onMakeTransaction.onErrorTransaction(e);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onMakeTransaction.onErrorTransaction(t);
            }
        });
    }


    protected void showDialog(final String message, final OnMakeTransaction onMakeTransaction, Zafeplace.WalletTypes walletType, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                R.style.SimpleDialogTheme)).setTitle("Do you really want to do this transaction?").setNegativeButton("Cancel", (dialog, which) -> {
            onMakeTransaction.onBreakTransaction();
            dialog.dismiss();
        }).setPositiveButton("Accept", (dialog, which) -> doTransaction(walletType, message, onMakeTransaction, activity));
        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    protected void showErrorDialog(final String message, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                R.style.SimpleDialogTheme)).setTitle("Error transaction " + message).setNeutralButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.setMessage(message);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }


    public abstract void makeTransaction(String addressSender, String addressRecipient, double amount,
                                         final OnMakeTransaction onMakeTransaction, Activity activity);

    public abstract void makeTransactionToken(String addressSender, String addressRecipient, int amount,
                                              final OnMakeTransaction onMakeTransaction, Activity activity);

    protected PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    protected ExecutorService getExecutor() {
        return mExecutor;
    }

    public abstract Zafeplace.WalletTypes getWalletType();
}
