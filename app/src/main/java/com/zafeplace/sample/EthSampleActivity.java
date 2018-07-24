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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zafeplace.sdk.Zafeplace.WalletTypes.ETH_WALLET;

public class EthSampleActivity extends AppCompatActivity implements OnWalletGenerateListener,
        OnMakeTransaction, OnSmartContractRawList, OnExecuteMethodSmartContract {
    public static String TAG = "TAG";
    private Zafeplace mZafeplace;
    private LoadingDialogFragment mLoadingDialogFragment;
    @BindView(R.id.nuber_coin)
    EditText numCoin;
    @BindView(R.id.number_token)
    EditText numberToken;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_sample);
        mUnbinder = ButterKnife.bind(this);
        mLoadingDialogFragment = LoadingDialogFragment.newInstance();
        mZafeplace = Zafeplace.getInstance(this);
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
        Toast.makeText(this, "Error generate wallet " + error, Toast.LENGTH_SHORT).show();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    runOnUiThread(() -> mLoadingDialogFragment.dismiss());
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
            stringBuilder.append(abi.name).append("\n");
            Log.d(TAG, "abi = " + abi);
        }
        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onErrorSmartRaw(Throwable error) {
        Toast.makeText(this, "Error getting list abi = " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExecuteContract(String result) {
        Toast.makeText(this, "result tokens balance  = " + result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorExecuteConract(Throwable e) {
        Toast.makeText(this, "error getting tokens balance  = " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void generateWallet(View view) {
        mZafeplace.generateWallet(ETH_WALLET, this);
    }

    public void getWalletBalance(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getWalletBalance(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(), new OnGetWalletBalance() {
                @Override
                public void onWalletBalance(BalanceModel balance) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(EthSampleActivity.this, "balance = " + balance.result, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorWalletBalance(Throwable error) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(EthSampleActivity.this, "balance error = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generate wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTokenBalance(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getTokenBalance(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(), new OnGetTokenBalance() {
                @Override
                public void onTokenBalance(List<ResultToken> tokenBalance) {
                    mLoadingDialogFragment.dismiss();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (ResultToken resultToken : tokenBalance) {
                        stringBuilder.append(resultToken.toString()).append("\n");
                    }
                    Toast.makeText(EthSampleActivity.this, "Token balance " + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorTokenBalance(Throwable error) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(EthSampleActivity.this, "Error Token Balan " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generate wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTransaction(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET) && !numCoin.getText().toString().equals("")) {
            mZafeplace.createTransaction(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(),
                    "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", Integer.parseInt(numCoin.getText().toString()), this);
        } else if (numCoin.getText().toString().equals("")) {
            Toast.makeText(this, "Input number", Toast.LENGTH_SHORT).show();
        } else if (!mZafeplace.isIdentityExist(ETH_WALLET)) {
            Toast.makeText(this, "Please generate wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTokenTransaction(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET) && !numberToken.getText().toString().equals("")) {
            mZafeplace.createTransactionToken(ETH_WALLET, mZafeplace.getWallet(ETH_WALLET).getAddress(),
                    "0x41B964C9E439d5d5e06c30BA24DC3F9A53844C9A", Double.parseDouble(numberToken.getText().toString()), this);
        } else if (numberToken.getText().toString().equals("")) {
            Toast.makeText(this, "Input number", Toast.LENGTH_SHORT).show();
        } else if (!mZafeplace.isIdentityExist(ETH_WALLET)) {
            Toast.makeText(this, "Please generate wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void getListSmartContracts(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            mZafeplace.getSmartContractTransactionRaw(this);
        } else {
            Toast.makeText(this, "Please generate wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void executeSmartMethodBalanceOf(View view) {
        if (mZafeplace.isIdentityExist(ETH_WALLET)) {
            List<MethodParamsSmart> list
                    = new ArrayList<>(Arrays
                    .asList(new MethodParamsSmart("tokenOwner",
                            mZafeplace.getWallet(ETH_WALLET).getAddress())));
            mZafeplace.executeSmartContractMethod("balanceOf",
                    mZafeplace.getWallet(ETH_WALLET).getAddress(), list, this);
        } else {
            Toast.makeText(this, "Please generate wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, EthSampleActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
