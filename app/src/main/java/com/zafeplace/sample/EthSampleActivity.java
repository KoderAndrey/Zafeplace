package com.zafeplace.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnExecuteMethodSmartContract;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRawList;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.server.models.Abi;
import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.MethodParamsSmart;
import com.zafeplace.sdk.server.models.ResultToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zafeplace.sdk.Zafeplace.WalletTypes.ETH_WALLET;
import static com.zafeplace.sdk.Zafeplace.WalletTypes.STELLAR_WALLET;

public class EthSampleActivity extends AppCompatActivity implements OnWalletGenerateListener,
        OnMakeTransaction, OnSmartContractRawList, OnExecuteMethodSmartContract {
    public static String TAG = "TAG";
    private Zafeplace mZafeplace;
    private LoadingDialogFragment mLoadingDialogFragment;
    EditText numCoin;
    EditText numberToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingDialogFragment = LoadingDialogFragment.newInstance();
        mZafeplace = Zafeplace.getInstance(this);
        numCoin = findViewById(R.id.nuber_coin);
        numberToken = findViewById(R.id.number_token);
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
    public void onErrorGenerate(Exception error) {
        Toast.makeText(this, "Error generating wallet " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onGetSmartContractAbiList(List<Abi> abis) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Abi abi : abis) {
            stringBuilder.append(abi.name + "\n");

            Log.d(TAG, "abi = " + abi);
        }
        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onErrorSmartRaw(Throwable error) {
        Toast.makeText(this, "Error geting list abi = " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExecuteContract(String result) {
        Toast.makeText(this, "result tokens balans  = " + result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorExecuteConract(Throwable e) {
        Toast.makeText(this, "error geting tokens balans  = " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void generateWallet(View view) {
        mZafeplace.generateWallet(ETH_WALLET, this);
    }

    public void getWalletBalance(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getWalletBalance(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(), new OnGetWalletBalance() {
                @Override
                public void onWalletBalance(BalanceModel balans) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(EthSampleActivity.this, "Balans = " + balans.result, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorWalletBalance(Throwable error) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(EthSampleActivity.this, "Balans error = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTokenBalance(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getTokenBalance(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(), new OnGetTokenBalance() {
                @Override
                public void onTokenBalance(List<ResultToken> tokenBalans) {
                    mLoadingDialogFragment.dismiss();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (ResultToken resultToken : tokenBalans) {
                        stringBuilder.append(resultToken.toString()).append("\n");
                    }
                    Toast.makeText(EthSampleActivity.this, "Token balans " + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorTokenBalance(Throwable error) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(EthSampleActivity.this, "Error Token Balan " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTransaction(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET) && !numCoin.getText().toString().equals("")) {
            mZafeplace.createTransaction(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(),
                    "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", Double.parseDouble(numCoin.getText().toString()), this);
        } else if (numCoin.getText().toString().equals("")) {
            Toast.makeText(this, "Input number", Toast.LENGTH_SHORT).show();
        } else if (!mZafeplace.isIdentityExist(ETH_WALLET)) {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTokenTransaction(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET) && !numberToken.getText().toString().equals("")) {
            mZafeplace.createTransactionToken(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(),
                    "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", Integer.parseInt(numberToken.getText().toString()), this);
        } else if (numberToken.getText().toString().equals("")) {
            Toast.makeText(this, "Input number", Toast.LENGTH_SHORT).show();
        } else if (!mZafeplace.isIdentityExist(ETH_WALLET)) {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void getListSmartContracts(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getSmartContractTransactionRaw(ETH_WALLET, this);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }

    }

    public void executeSmartMethodBalanceOf(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            List<MethodParamsSmart> list
                    = new ArrayList<>(Arrays
                    .asList(new MethodParamsSmart("tokenOwner",
                            mZafeplace.getWallet(ETH_WALLET).getAddress())));
            mZafeplace.executeSmartContractMethod(ETH_WALLET, true, "balanceOf",
                    mZafeplace.getWallet(ETH_WALLET).getAddress(), list, this);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, EthSampleActivity.class);
        context.startActivity(starter);
    }

}
//        "packageName": "com.zafeplace.sample",
//        "appSecret": "756496e0a7d900ade56913cc098749ee"