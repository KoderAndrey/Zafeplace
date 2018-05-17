package com.zafeplace.sdk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.managers.PreferencesManager;
import com.zafeplace.sdk.models.EthWallet;
import com.zafeplace.sdk.models.Wallet;
import com.zafeplace.sdk.utils.FingerPrintLogin;
import com.zafeplace.sdk.utils.FingerprintHandler;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.List;

import static com.zafeplace.sdk.Constants.AuthType.PIN_AUTH;
import static com.zafeplace.sdk.Constants.ETH_SERVICE_URL;
import static com.zafeplace.sdk.Constants.WalletType.ETH;
import static com.zafeplace.sdk.Constants.ZAFEPLACE_PASSWORD;
import static com.zafeplace.sdk.managers.PreferencesManager.getEthWallet;
import static com.zafeplace.sdk.managers.PreferencesManager.isLoggedIn;
import static com.zafeplace.sdk.managers.PreferencesManager.setAuthType;
import static com.zafeplace.sdk.managers.PreferencesManager.setEthWallet;
import static com.zafeplace.sdk.managers.PreferencesManager.setIsLoggedIn;
import static com.zafeplace.sdk.managers.PreferencesManager.setPinCode;
import static com.zafeplace.sdk.utils.AppUtils.isNull;
import static com.zafeplace.sdk.utils.EncryptionUtils.decryption;
import static com.zafeplace.sdk.utils.EncryptionUtils.encryption;
import static com.zafeplace.sdk.utils.StorageUtils.deleteFile;

public class Zafeplace {

    public enum WalletTypes {
        ETH_WALLET;
    }

    private static Zafeplace instance;

    public static Zafeplace getInstance() {
        if (instance == null) {
            instance = new Zafeplace();
        }
        return instance;
    }

    public void fingerprintLogin(Activity context, FingerprintHandler.FingerprintAuthenticationCallback fingerprintAuthenticationCallback){
        FingerPrintLogin fingerPrintLogin = new FingerPrintLogin(context, fingerprintAuthenticationCallback);
        fingerPrintLogin.initAuth();
    }

    public void pinCodeLogin(Activity context, String code){
        setIsLoggedIn(true,context);
        setAuthType(PIN_AUTH, context);
        setPinCode(encryption(ZAFEPLACE_PASSWORD,code), context);
    }

    public void generateWallet(WalletTypes walletType, Activity context, OnWalletGenerateListener onWalletGenerateListener){
        switch (walletType){
            case ETH_WALLET: generateEthWallet(context,onWalletGenerateListener);
        }
    }

    private void generateEthWallet(Activity context, OnWalletGenerateListener onWalletGenerateListener){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            onWalletGenerateListener.onError(context.getString(R.string.write_external_storage_permission_not_enabled));
        }
        else if(!isLoggedIn(context)){
            onWalletGenerateListener.onError(context.getString(R.string.you_need_auth_to_generate_wallet));
        }
        else {
            try {
                Web3j web3 = Web3jFactory.build(new HttpService(ETH_SERVICE_URL));
                String wallet = WalletUtils.generateLightNewWalletFile(ZAFEPLACE_PASSWORD, Environment.getExternalStorageDirectory());
                Credentials credentials = WalletUtils.loadCredentials(ZAFEPLACE_PASSWORD,Environment.getExternalStorageDirectory() + "/" + wallet);
                String privateKey = String.format("%x", credentials.getEcKeyPair().getPrivateKey());
                String address = credentials.getAddress();
                setEthWallet(privateKey,address,context);

                deleteFile(Environment.getExternalStorageDirectory() + "/" + wallet);
                onWalletGenerateListener.onSuccess(privateKey);

            } catch (Throwable e) {
                onWalletGenerateListener.onError(e.getMessage());
            }
        }
    }

    public void saveUserData(String firstName,String secondName, String email, String additionalData, Activity context){
        PreferencesManager.setUserData(firstName,secondName,email,additionalData,context);
    }

    public Wallet getWallet(WalletTypes walletType, Activity context){
        switch (walletType){
            case ETH_WALLET: return PreferencesManager.getEthWallet(context);
            default:return null;
        }
    }

    public boolean isIdentityExist(WalletTypes walletType, Activity context){
        switch (walletType){
            case ETH_WALLET: return !isNull(getEthWallet(context));
            default: return false;
        }
    }

    public String getPinCode(Activity context){
        return isLoggedIn(context) ? decryption(ZAFEPLACE_PASSWORD,PreferencesManager.getPinCode(context)) :
                context.getString(R.string.you_need_auth_to_get_pin_code);
    }

    public List<Wallet> getSemiPublicData(Activity context){
        List<Wallet> walletList = new ArrayList<>();
        EthWallet ethWallet = new EthWallet();
        ethWallet.setCurrencyName(ETH);
        ethWallet.setAddress(getEthWallet(context).getAddress());
        walletList.add(ethWallet);
        return walletList;
    }

    public void logout(Activity context){
        setIsLoggedIn(false,context);
    }
}
