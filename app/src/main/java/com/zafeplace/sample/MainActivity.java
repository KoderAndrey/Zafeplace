package com.zafeplace.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRaw;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;

import static com.zafeplace.sdk.Zafeplace.WalletTypes.ETH_WALLET;

public class MainActivity extends AppCompatActivity implements OnWalletGenerateListener, OnGetWalletBalance,
        OnGetTokenBalance, OnMakeTransaction, OnSmartContractRaw {
    public static String TAG = "TAG";
    private Zafeplace mZafeplace;
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingDialogFragment = LoadingDialogFragment.newInstance();
        mZafeplace = Zafeplace.getInstance(this);
        mZafeplace.getSmartContractTransactionRaw(ETH_WALLET, this);
    }

    @Override
    public void onStartGenerate() {
        mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
    }

    @Override
    public void onSuccessGenerate(String address) {
        mLoadingDialogFragment.dismiss();
        Toast.makeText(this, "Wallet was generated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorGenerate(String error) {
        Toast.makeText(this, "Error generate wallet " + error, Toast.LENGTH_SHORT).show();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingDialogFragment.dismiss();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onWalletBalance(String response) {
        mLoadingDialogFragment.dismiss();
        Toast.makeText(this, "Wallet balance " + response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorWalletBalance(Throwable error) {
        mLoadingDialogFragment.dismiss();
        Toast.makeText(this, "Error Wallet Balans " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenBalance(String balans) {
        mLoadingDialogFragment.dismiss();
        Toast.makeText(this, "Token balans " + balans, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorTokenBalance(Throwable error) {
        mLoadingDialogFragment.dismiss();
        Toast.makeText(this, "Error Token Balan " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartTransaction() {
        mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
    }

    @Override
    public void onBreakTransaction() {
        mLoadingDialogFragment.dismiss();
    }

    @Override
    public void onSuccessTransaction(String res) {
        mLoadingDialogFragment.dismiss();
        Toast.makeText(this, "Result transaction " + res, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorTransaction(Throwable error) {
        mLoadingDialogFragment.dismiss();
        Toast.makeText(this, "Error transaction " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void generateWallet(View view) {
        mZafeplace.generateWallet(ETH_WALLET, this);
    }

    public void getWalletBalance(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getWalletBalance(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(), this);
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTokenBalance(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getTokenBalance(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(), this);
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTransaction(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.createTransaction(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(),
                    "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", 0.1, this);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTokenTransaction(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.createTransactionToken(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(),
                    "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", 10, this);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @Override
    public void onGetSmartContractRaw(String response) {
        Toast.makeText(this, "body smart contract " + response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorSmartRaw(Throwable error) {
        Toast.makeText(this, "body smart contract error " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
//        "packageName": "com.zafeplace.sample",
//        "appSecret": "756496e0a7d900ade56913cc098749ee"