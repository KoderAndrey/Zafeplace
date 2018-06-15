package com.zafeplace.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.ResultToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zafeplace.sdk.Zafeplace.WalletTypes.STELLAR_WALLET;

public class StellarSampleActivity extends AppCompatActivity implements OnWalletGenerateListener, OnMakeTransaction {
    private Zafeplace mZafeplace;
    @BindView(R.id.nuber_coin_s)
    EditText numberCoin;
    @BindView(R.id.number_token_s)
    EditText numberTokens;
    private LoadingDialogFragment mLoadingDialogFragment;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellar_sample);
        mUnbinder = ButterKnife.bind(this);
        mZafeplace = Zafeplace.getInstance(this);
        mLoadingDialogFragment = LoadingDialogFragment.newInstance();
    }

    public void generateWalletStellar(View view) {
        mZafeplace.generateWallet(STELLAR_WALLET, this);
    }

    public void getWalletBalanceStellar(View view) {
        if (mZafeplace.isIdentityExist(STELLAR_WALLET)) {
            mZafeplace.getWalletBalance(STELLAR_WALLET, mZafeplace.getWallet(STELLAR_WALLET).getAddress(), new OnGetWalletBalance() {
                @Override
                public void onWalletBalance(BalanceModel balans) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(StellarSampleActivity.this, "Balans = " + balans.result, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorWalletBalance(Throwable error) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(StellarSampleActivity.this, "Balans error = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTransactionStellar(View view) {
        if (mZafeplace.isIdentityExist(STELLAR_WALLET) && !numberCoin.getText().toString().equals("")) {
            mZafeplace.createTransaction(STELLAR_WALLET, mZafeplace.getWallet(STELLAR_WALLET).getAddress(),
                    "GBHQ7TFBFEWJVLT4VTEZI2ISOVYSJFGM6TJJTHDSJZQBY65ARQRCYOP5",
                    Double.parseDouble(numberCoin.getText().toString()), this);
        } else if (numberCoin.getText().toString().equals("")) {
            Toast.makeText(this, "Input number", Toast.LENGTH_SHORT).show();
        } else if (!mZafeplace.isIdentityExist(STELLAR_WALLET)) {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTokenBalanceStellar(View view) {
        if (mZafeplace.isIdentityExist(STELLAR_WALLET)) {
            mZafeplace.getTokenBalance(STELLAR_WALLET, mZafeplace.getWallet(STELLAR_WALLET).getAddress(), new OnGetTokenBalance() {
                @Override
                public void onTokenBalance(List<ResultToken> tokenBalans) {
                    mLoadingDialogFragment.dismiss();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (tokenBalans.size() != 0) {
                        for (ResultToken resultToken : tokenBalans) {
                            stringBuilder.append(resultToken.toString()).append("\n");
                        }
                    } else {
                        stringBuilder.append("empty");
                    }
                    Toast.makeText(StellarSampleActivity.this, "Token balans " + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorTokenBalance(Throwable error) {
                    mLoadingDialogFragment.dismiss();
                    Toast.makeText(StellarSampleActivity.this, "Error Token Balan " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            mLoadingDialogFragment.show(getSupportFragmentManager(), LoadingDialogFragment.TAG);
        } else {
            Toast.makeText(this, "Please generete wallet at first", Toast.LENGTH_SHORT).show();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, StellarSampleActivity.class);
        context.startActivity(starter);
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
    public void onErrorGenerate(String errorMessage) {
        Toast.makeText(this, "Error generating wallet " + errorMessage, Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
