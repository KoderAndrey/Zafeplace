package com.zafeplace.sdk;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import com.zafeplace.sdk.callbacks.OnAccessTokenListener;
import com.zafeplace.sdk.callbacks.OnExecuteMethodSmartContract;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRawList;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.managers.EthereumManager;
import com.zafeplace.sdk.managers.PreferencesManager;
import com.zafeplace.sdk.managers.StellarManager;
import com.zafeplace.sdk.models.EthWallet;
import com.zafeplace.sdk.models.Wallet;
import com.zafeplace.sdk.server.ZafeplaceApi;
import com.zafeplace.sdk.server.models.LoginResponse;
import com.zafeplace.sdk.server.models.MethodParamsSmart;
import com.zafeplace.sdk.utils.FingerPrintLogin;
import com.zafeplace.sdk.utils.FingerprintHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zafeplace.sdk.Constants.AuthType.PIN_AUTH;
import static com.zafeplace.sdk.Constants.TransactionType.COIN_TRANSACTION;
import static com.zafeplace.sdk.Constants.TransactionType.TOKEN_TRANSACTION;
import static com.zafeplace.sdk.Constants.WalletType.ETH;
import static com.zafeplace.sdk.Constants.ZAFEPLACE_PASSWORD;
import static com.zafeplace.sdk.utils.AppUtils.isNull;
import static com.zafeplace.sdk.utils.EncryptionUtils.decryption;
import static com.zafeplace.sdk.utils.EncryptionUtils.encryption;

public class Zafeplace {

    private Activity mActivity;
    private EditText mInput;
    private PreferencesManager mManager;
    private AlertDialog mAlert = null;
    private FingerPrintLogin mFingerPrintLogin;
    private boolean mIsCancelClicked;
    private StellarManager mStellarManager;
    private EthereumManager mEthereumManager;

    public enum WalletTypes {
        ETH_WALLET,
        STELLAR_WALLET;
    }

    public enum AuthType {
        FINGERPRINT_AUTH,
        PIN_AUTH;
    }

    private static Zafeplace instance;

    public static Zafeplace getInstance(Activity activity) {
        if (instance == null) {
            instance = new Zafeplace(activity);
        } else {
            instance.mActivity = activity;
        }
        return instance;
    }

    private Zafeplace(Activity context) {
        this.mActivity = context;
        mManager = new PreferencesManager();
        mStellarManager = new StellarManager();
        mEthereumManager = new EthereumManager();
    }

    public void generateAccessToken(String appId, String appSecret, final OnAccessTokenListener onAccessTokenListener) {
        ZafeplaceApi.getInstance(mActivity).generateAccessToken(appId, appSecret).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    onAccessTokenListener.onGetToken(response.body().accessToken);
                    String tok = response.body().accessToken;
                    mManager.setAuthToken(tok, mActivity);
                } else {
                    String err = response.message();
                    onAccessTokenListener.onErrorToken(new Throwable(err));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                onAccessTokenListener.onErrorToken(t);
            }
        });
    }

    public String getAccessToken() {
        return mManager.getAuthToken(mActivity);
    }


    public void getWalletBalance(WalletTypes walletType, String address, final OnGetWalletBalance onGetWalletBalance) {
        switch (walletType) {
            case STELLAR_WALLET:
                mStellarManager.getWalletBalance(address, onGetWalletBalance, mActivity);
                break;
            case ETH_WALLET:
                mEthereumManager.getWalletBalance(address, onGetWalletBalance, mActivity);
                break;
        }
    }

    public void getTokenBalance(WalletTypes walletType, String address, final OnGetTokenBalance onGetTokenBalance) {
        switch (walletType) {
            case ETH_WALLET:
                mEthereumManager.getTokenBalance(address, onGetTokenBalance, mActivity);
                break;
            case STELLAR_WALLET:
                mStellarManager.getTokenBalance(address, onGetTokenBalance, mActivity);
                break;
        }
    }

    public void getSmartContractTransactionRaw(final OnSmartContractRawList onSmartContractRaw) {
        mEthereumManager.getSmartContractTransactionRaw(mActivity, onSmartContractRaw);
    }

    public void createTransaction(WalletTypes walletType, String addressSender, String addressRecipient, int amount,
                                  final OnMakeTransaction onMakeTransaction) {
        switch (walletType) {
            case STELLAR_WALLET:
                mStellarManager.createTransaction(addressSender, addressRecipient, String.valueOf(amount), onMakeTransaction, mActivity, COIN_TRANSACTION);
                break;
            case ETH_WALLET:
                mEthereumManager.createTransaction(addressSender, addressRecipient, String.valueOf(amount), onMakeTransaction, mActivity, COIN_TRANSACTION);
                break;
        }
    }

    public void changeTrust(String addressRecipient, double amount,final OnMakeTransaction onMakeTransaction){
        mStellarManager.changeTrust(addressRecipient,amount,onMakeTransaction,mActivity);
    }

    public void createTransactionToken(WalletTypes walletType, String addressSender, String addressRecipient, double amount,
                                       final OnMakeTransaction onMakeTransaction) {
        switch (walletType) {
            case STELLAR_WALLET:
                mStellarManager.createTransaction(addressSender, addressRecipient, String.valueOf(amount), onMakeTransaction, mActivity, TOKEN_TRANSACTION);
                break;
            case ETH_WALLET:
                mEthereumManager.createTransaction(addressSender, addressRecipient, String.valueOf(amount), onMakeTransaction, mActivity, TOKEN_TRANSACTION);
                break;
        }
    }

    public void fingerprintLogin(FingerprintHandler.FingerprintAuthenticationCallback fingerprintAuthenticationCallback) {
        mFingerPrintLogin = new FingerPrintLogin(mActivity, fingerprintAuthenticationCallback);
        mFingerPrintLogin.initAuth();
    }

    private void cancelFingerprintLogin() {
        if (mFingerPrintLogin != null) {
            mFingerPrintLogin.stopAuth();
        }
    }


    public void pinCodeLogin(String code) {
        mManager.setIsLoggedIn(true, mActivity);
        mManager.setAuthType(PIN_AUTH, mActivity);
        mManager.setPinCode(encryption(ZAFEPLACE_PASSWORD, code), mActivity);
    }


    public void generateWallet(WalletTypes walletType, OnWalletGenerateListener onWalletGenerateListener) {
        switch (walletType) {
            case ETH_WALLET:
                mEthereumManager.checkLoginGenerateWallet(onWalletGenerateListener, mActivity);
                break;
            case STELLAR_WALLET:
                mStellarManager.checkLoginGenerateWallet(onWalletGenerateListener, mActivity);
                break;

        }
    }

    public void executeSmartContractMethod(String nameFunk, String sender,
                                           List<MethodParamsSmart> methodParamsSmarts,
                                           OnExecuteMethodSmartContract onExecuteMethodSmartContract) {
        mEthereumManager.executeSmartContractMethod(nameFunk, sender, methodParamsSmarts, onExecuteMethodSmartContract, mActivity);
    }

    public void saveUserData(String firstName, String secondName, String email, String
            additionalData) {
        mManager.setUserData(firstName, secondName, email, additionalData, mActivity);
    }

    public Wallet getWallet(WalletTypes walletType) {
        switch (walletType) {
            case ETH_WALLET:
                return mManager.getEthWallet(mActivity);
            case STELLAR_WALLET:
                return mManager.getStellarWallet(mActivity);
            default:
                return null;
        }
    }

    public boolean isIdentityExist(WalletTypes walletType) {
        switch (walletType) {
            case ETH_WALLET:
                return !isNull(mManager.getEthWallet(mActivity));
            case STELLAR_WALLET:
                return !isNull(mManager.getStellarWallet(mActivity));
            default:
                return false;
        }
    }

    public String getPinCode() {
        return mManager.isLoggedIn(mActivity) ? decryption(ZAFEPLACE_PASSWORD, mManager.getPinCode(mActivity)) :
                mActivity.getString(R.string.you_need_auth_to_get_pin_code);
    }

    public List<Wallet> getSemiPublicData() {
        List<Wallet> walletList = new ArrayList<>();
        EthWallet ethWallet = new EthWallet();
        ethWallet.setCurrencyName(ETH);
        ethWallet.setAddress(mManager.getEthWallet(mActivity).getAddress());
        walletList.add(ethWallet);
        return walletList;
    }

    public boolean isLoggedIn() {
        return mManager.isLoggedIn(mActivity);
    }

    public int getAuthType() {
        return mManager.getAuthType(mActivity);
    }

    public void logout() {
        mManager.setIsLoggedIn(false, mActivity);
    }

    private void closeDialog() {
        mAlert.dismiss();
    }
}
