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
import com.zafeplace.sdk.models.Wallet;
import com.zafeplace.sdk.utils.FingerprintHandler;

import java.util.List;

import static com.zafeplace.sdk.Zafeplace.WalletTypes.ETH_WALLET;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Zafeplace.getInstance().fingerprintLogin(this, new FingerprintHandler.FingerprintAuthenticationCallback() {
//            @Override
//            public void onResponse(String message, boolean isSuccess) {
//                if(isSuccess){
//                    Zafeplace.getInstance().generateWallet(ETH_WALLET, MainActivity.this, new OnWalletGenerateListener() {
//                        @Override
//                        public void onSuccess(String privateKey) {
//                            Log.wtf("aaa",privateKey);
//                        }
//
//                        @Override
//                        public void onError(String error) {
//                            Log.wtf("aaa",error);
//                        }
//                    });
//
//                }
//                else Log.wtf("aaa",message);
//            }
//        });


        List<Wallet> walletList = Zafeplace.getInstance().getSemiPublicData(MainActivity.this);
        Log.wtf("aaa",walletList.get(0).getAddress());
    }
}
