package com.zafeplace.sdk.managers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.zafeplace.sdk.R;
import com.zafeplace.sdk.WalletManager;
import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRawList;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import static com.zafeplace.sdk.Constants.ETH_SERVICE_URL;
import static com.zafeplace.sdk.Constants.ZAFEPLACE_PASSWORD;
import static com.zafeplace.sdk.utils.StorageUtils.deleteFile;

public class EthereumManager extends WalletManager {

    @Override
    public void generateWallet(OnWalletGenerateListener onWalletGenerateListener, Activity activity, boolean isLoggedIn) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            onWalletGenerateListener.onErrorGenerate(activity.getString(R.string.write_external_storage_permission_not_enabled));
        } else if (!isLoggedIn) {
            onWalletGenerateListener.onErrorGenerate(activity.getString(R.string.you_need_auth_to_generate_wallet));
        } else {
            getExecutor().execute(() -> {
                try {
                    Web3jFactory.build(new HttpService(ETH_SERVICE_URL));
                    String wallet = WalletUtils.generateLightNewWalletFile(ZAFEPLACE_PASSWORD, Environment.getExternalStorageDirectory());
                    Credentials credentials = WalletUtils.loadCredentials(ZAFEPLACE_PASSWORD, Environment.getExternalStorageDirectory() + "/" + wallet);
                    String privateKey = String.format("%x", credentials.getEcKeyPair().getPrivateKey());
                    final String address = credentials.getAddress();
                    getPreferencesManager().setEthWallet(privateKey, address, activity);
                    deleteFile(Environment.getExternalStorageDirectory() + "/" + wallet);
                    activity.runOnUiThread(() -> onWalletGenerateListener.onSuccessGenerate(address));
                } catch (final Exception e) {
                    activity.runOnUiThread(() -> onWalletGenerateListener.onErrorGenerate(e));
                }
            });
        }
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
        return Zafeplace.WalletTypes.ETH_WALLET;
    }
}
