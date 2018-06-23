package com.zafeplace.sdk.managers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.server.ZafeplaceApi;
import com.zafeplace.sdk.server.models.TransactionRaw;
import com.zafeplace.sdk.stellarsdk.sdk.KeyPair;
import com.zafeplace.sdk.stellarsdk.sdk.Network;
import com.zafeplace.sdk.stellarsdk.sdk.Server;
import com.zafeplace.sdk.stellarsdk.sdk.Transaction;
import com.zafeplace.sdk.stellarsdk.sdk.responses.SubmitTransactionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zafeplace.sdk.utils.WalletUtils.getWalletName;

public class StellarManager extends WalletManager {

    @Override
    public void generateWallet(OnWalletGenerateListener onWalletGenerateListener, Activity activity, boolean isLoggedIn) {
        KeyPair pair = KeyPair.random();
        String secretSeed = new String(pair.getSecretSeed());
        String accId = pair.getAccountId();
        final String friendbotUrl = String.format(
                "https://friendbot.stellar.org/?addr=%s",
                pair.getAccountId());
        Log.wtf("tag", "url " + friendbotUrl);
        getExecutor().execute(() -> {
            InputStream response = null;
            try {
                response = new URL(friendbotUrl).openStream();
                String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
                getPreferencesManager().setStellarWallet(secretSeed, accId, activity);
                activity.runOnUiThread(() -> onWalletGenerateListener.onSuccessGenerate(accId));
            } catch (IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> onWalletGenerateListener.onErrorGenerate(e));
                Log.wtf("tag", "error " + e.getMessage());
            } catch (Exception e) {
                onWalletGenerateListener.onErrorGenerate(e);
                activity.runOnUiThread(() -> onWalletGenerateListener.onErrorGenerate(e));
            }
        });
    }

    @Override
    public void getWalletBalance(String address, OnGetWalletBalance onGetWalletBalance, Activity activity) {
        super.getWalletBalance(address, onGetWalletBalance, activity);
    }

    @Override
    public void getTokenBalance(String address, OnGetTokenBalance onGetTokenBalance, Activity activity) {
        super.getTokenBalance(address, onGetTokenBalance, activity);
    }

    @Override
    public void makeTransaction(String addressSender, String addressRecipient, double amount, OnMakeTransaction onMakeTransaction, Activity activity) {
        ZafeplaceApi.getInstance(activity).getRawTransaction(getWalletName(getWalletType()), addressSender, addressRecipient, amount).enqueue(new Callback<TransactionRaw>() {
            @Override
            public void onResponse(Call<TransactionRaw> call, Response<TransactionRaw> response) {
                getExecutor().execute(() -> {
                            executeTransaction(response, activity, onMakeTransaction);
                        }
                );
            }

            @Override
            public void onFailure(Call<TransactionRaw> call, Throwable t) {
                onMakeTransaction.onBreakTransaction();
                showErrorDialog(t.getMessage(), activity);
            }
        });
    }

    @Override
    public void makeTransactionToken(String addressSender, String addressRecipient, int amount, OnMakeTransaction onMakeTransaction, Activity activity) {
        ZafeplaceApi.getInstance(activity).getTokenTransactionRaw(getWalletName(getWalletType()), addressSender, addressRecipient, amount).enqueue(new Callback<TransactionRaw>() {
            @Override
            public void onResponse(Call<TransactionRaw> call, Response<TransactionRaw> response) {
                getExecutor().execute(() -> {
                            executeTransaction(response, activity, onMakeTransaction);
                        }
                );
            }

            @Override
            public void onFailure(@NonNull Call<TransactionRaw> call, @NonNull Throwable t) {
                onMakeTransaction.onBreakTransaction();
                showErrorDialog(t.getMessage(), activity);
            }
        });
    }

    @Override
    public Zafeplace.WalletTypes getWalletType() {
        return Zafeplace.WalletTypes.STELLAR_WALLET;
    }

    private void executeTransaction(Response<TransactionRaw> response, Activity activity, OnMakeTransaction onMakeTransaction) {
        try {
            TransactionRaw raw1 = response.body();
            Server server = new Server("https://horizon-testnet.stellar.org");
            Network.useTestNetwork();
            KeyPair source = KeyPair.fromSecretSeed(getPreferencesManager().getStellarWallet(activity).getSecretSeed());
            Transaction tx = Transaction.fromEnvelope(Transaction.decodeXdrEnvelope(raw1.result.rawTx.result.rawTx));
            tx.sign(source);
            SubmitTransactionResponse submitTransactionResponse = server.submitTransaction(tx);
            Log.wtf("tag", "Success creating stellar transaction!");
            Log.wtf("tag", submitTransactionResponse.getEnvelopeXdr() + " ---  "
                    + submitTransactionResponse.getHash() + " --- "
                    + submitTransactionResponse.getResultXdr());
            activity.runOnUiThread(() -> showDialog(submitTransactionResponse.getEnvelopeXdr(),
                    onMakeTransaction, getWalletType(), activity));
        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                onMakeTransaction.onErrorTransaction(e);
            });
        }
    }
}
