package com.zafeplace.sample;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnResponseListener;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.models.EthWallet;
import com.zafeplace.sdk.models.Wallet;
import com.zafeplace.sdk.server.ZafeplaceApi;
import com.zafeplace.sdk.utils.FingerprintHandler;

import java.util.List;

import static com.zafeplace.sdk.Zafeplace.WalletTypes.ETH_WALLET;
import static com.zafeplace.sdk.utils.WalletUtils.getWalletName;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Zafeplace.getInstance().generateWallet(ETH_WALLET, this, new OnWalletGenerateListener() {
            @Override
            public void onSuccess(String address) {
                Zafeplace.getInstance().getWalletBalance(ETH_WALLET,address,MainActivity.this);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}

//        "packageName": "com.zafeplace.sample",
//        "appSecret": "756496e0a7d900ade56913cc098749ee"
