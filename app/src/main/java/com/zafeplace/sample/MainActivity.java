package com.zafeplace.sample;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.models.EthWallet;
import com.zafeplace.sdk.utils.FingerprintHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Zafeplace.getInstance().fingerprintLogin(this, new FingerprintHandler.FingerprintAuthenticationCallback() {
//            @Override
//            public void onResponse(String message, boolean isSuccess) {
//                if(isSuccess){
//                    Zafeplace.getInstance().generateWallet(Zafeplace.ETH_WALLET, MainActivity.this, new OnWalletGenerateListener() {
//                        @Override
//                        public void onSuccess(String privateKey) {
//                            Log.wtf("aaa",privateKey);
//                        }
//
//                        @Override
//                        public void onError(String error) {
//
//                        }
//                    });
//
//                }
//            }
//        });

        EthWallet ethWallet = (EthWallet) Zafeplace.getInstance().getWallet(Zafeplace.ETH_WALLET,MainActivity.this);
        Log.wtf("aaa",ethWallet.getPrivateKey());
    }
}
