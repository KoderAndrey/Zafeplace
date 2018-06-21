package com.zafeplace.sdk.managers;

import android.app.Activity;
import android.util.Log;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRawList;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.WalletManager;
import com.zafeplace.sdk.stellarsdk.sdk.KeyPair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class StellarManager extends WalletManager {

    private Zafeplace.WalletTypes mWalletType = Zafeplace.WalletTypes.STELLAR_WALLET;


    @Override
    public void generateWallet(OnWalletGenerateListener onWalletGenerateListener, Activity activity, boolean isLoggedIn) {
        KeyPair pair = KeyPair.random();
        String secretSeed = new String(pair.getSecretSeed());
        String accId = pair.getAccountId();
        Log.wtf("tag", "acc id " + accId);
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
    public void makeTransaction(Zafeplace.WalletTypes walletType, String addressSender, String addressRecipient, double amount, OnMakeTransaction onMakeTransaction) {
        super.makeTransaction(walletType, addressSender, addressRecipient, amount, onMakeTransaction);
    }

    @Override
    public void makeTransactionToken(Zafeplace.WalletTypes walletType, String addressSender, String addressRecipient, int amount, OnMakeTransaction onMakeTransaction) {
        super.makeTransactionToken(walletType, addressSender, addressRecipient, amount, onMakeTransaction);
    }

    @Override
    public void getSmartContractTransactionRaw(Zafeplace.WalletTypes walletType, OnSmartContractRawList onSmartContractRaw) {
        super.getSmartContractTransactionRaw(walletType, onSmartContractRaw);
    }

    @Override
    public Zafeplace.WalletTypes getWalletType() {
        return Zafeplace.WalletTypes.STELLAR_WALLET;
    }
}
