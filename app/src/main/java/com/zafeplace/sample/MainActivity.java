package com.zafeplace.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnAccessTokenListener;
import com.zafeplace.sdk.callbacks.OnGetRawTokenTransactionHex;
import com.zafeplace.sdk.callbacks.OnGetRawTransactionHex;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;

import static com.zafeplace.sdk.Zafeplace.WalletTypes.ETH_WALLET;

public class MainActivity extends AppCompatActivity implements OnWalletGenerateListener, OnGetWalletBalance,
        OnAccessTokenListener, OnGetTokenBalance, OnGetRawTransactionHex, OnMakeTransaction, OnGetRawTokenTransactionHex {
    public static String TAG = "TAG";
    Zafeplace mZafeplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mZafeplace = Zafeplace.getInstance();
        mZafeplace.getAccessToken("com.zafeplace.sample",
                "756496e0a7d900ade56913cc098749ee",
                this);
    }


    @Override
    public void onSuccessGenerate(String address) {
        Log.d(TAG, "onSuccess " + address);
        mZafeplace.getWalletBalance(ETH_WALLET, address, this);
        mZafeplace.getTokenBalance(ETH_WALLET, address, this);
        mZafeplace.getRawTransaction(ETH_WALLET, address, "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", 0.1, this);
        Log.d(TAG, "body ");
        mZafeplace.getTokenTransactionRaw(ETH_WALLET, address, "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", 100, this);
    }

    @Override
    public void onErrorGenerate(String error) {
        Log.d(TAG, "onError " + error);
    }

    @Override
    public void onWalletBalance(String response) {
        Log.d(TAG, "onWalletBalance " + response);
    }

    @Override
    public void onErrorWalletBalans(String error) {
        Log.d(TAG, "onErrorBalans " + error);
    }

    @Override
    public void onGetToken(String response) {
        Log.d(TAG, "getAccessToken succ " + response);
        mZafeplace.pinCodeLogin("abs");
        mZafeplace.generateWallet(ETH_WALLET, this);
    }

    @Override
    public void onErrorToken(String error) {
        Log.d(TAG, "getAccessToken err " + error);
    }

    @Override
    public void onTokenBalance(String balans) {
        Log.d(TAG, "onTokenBalance " + balans);
    }

    @Override
    public void onErrorTokenBalans(String error) {
        Log.d(TAG, "onErrorTokenBalans " + error);
    }

    @Override
    public void onGetRawHex(String rawHex) {
        Log.d(TAG, "onGetRawHex " + rawHex);
        mZafeplace.doTransaction(ETH_WALLET, rawHex, this);
    }

    @Override
    public void onErrorRawHex(String error) {
        Log.d(TAG, "onErrorRaw " + error);
    }

    @Override
    public void OnSuccessTransaction(String res) {
        Log.d(TAG, "OnSuccessTransaction " + res);
    }

    @Override
    public void OnErrorTransaction(String error) {
        Log.d(TAG, "OnSuccessTransaction " + error);
    }

    @Override
    public void onGetTokenRawHex(String rawHex) {
        Log.d(TAG, "onGetTokenRawHex " + rawHex);
        mZafeplace.doTransaction(ETH_WALLET, rawHex, this);
    }

    @Override
    public void onErrorTokenRawHex(String error) {
        Log.d(TAG, "onErrorTokenRawHex " + error);
    }
}
