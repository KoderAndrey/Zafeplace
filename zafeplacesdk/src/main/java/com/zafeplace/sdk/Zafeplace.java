package com.zafeplace.sdk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.zafeplace.sdk.callbacks.OnAccessTokenListener;
import com.zafeplace.sdk.callbacks.OnGetRawTokenTransactionHex;
import com.zafeplace.sdk.callbacks.OnGetRawTransactionHex;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.managers.PreferencesManager;
import com.zafeplace.sdk.models.EthWallet;
import com.zafeplace.sdk.models.Wallet;
import com.zafeplace.sdk.server.ZafeplaceApi;
import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.ErrorTransaction;
import com.zafeplace.sdk.server.models.LoginResponse;
import com.zafeplace.sdk.server.models.TransactionRaw;
import com.zafeplace.sdk.utils.FingerPrintLogin;
import com.zafeplace.sdk.utils.FingerprintHandler;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.zafeplace.sdk.Constants.AuthType.PIN_AUTH;
import static com.zafeplace.sdk.Constants.ETH_SERVICE_URL;
import static com.zafeplace.sdk.Constants.WalletType.ETH;
import static com.zafeplace.sdk.Constants.ZAFEPLACE_PASSWORD;
import static com.zafeplace.sdk.managers.PreferencesManager.getEthWallet;
import static com.zafeplace.sdk.managers.PreferencesManager.isLoggedIn;
import static com.zafeplace.sdk.managers.PreferencesManager.setAuthToken;
import static com.zafeplace.sdk.managers.PreferencesManager.setAuthType;
import static com.zafeplace.sdk.managers.PreferencesManager.setEthWallet;
import static com.zafeplace.sdk.managers.PreferencesManager.setIsLoggedIn;
import static com.zafeplace.sdk.managers.PreferencesManager.setPinCode;
import static com.zafeplace.sdk.utils.AppUtils.isNull;
import static com.zafeplace.sdk.utils.EncryptionUtils.decryption;
import static com.zafeplace.sdk.utils.EncryptionUtils.encryption;
import static com.zafeplace.sdk.utils.StorageUtils.deleteFile;
import static com.zafeplace.sdk.utils.WalletUtils.getWalletName;

public class Zafeplace {

    private Context mContext;
    private PreferencesManager mManager;

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

    private Zafeplace() {
        mManager = new PreferencesManager();
    }

    public void getAccessToken(String packageName, String appSecret, final OnAccessTokenListener onAccessTokenListener) {

        ZafeplaceApi.getInstance(mContext).getAccessToken(packageName, appSecret).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    onAccessTokenListener.onGetToken(response.body().accessToken);
                    String tok = response.body().accessToken;
                    setAuthToken(tok, mContext);
                } else {
                    String err = response.message();
                    onAccessTokenListener.onErrorToken(err);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                onAccessTokenListener.onErrorToken(t.getMessage());
            }
        });
    }

    public void getWalletBalance(WalletTypes walletType, String address, final OnGetWalletBalance onGetWalletBalance) {
        ZafeplaceApi.getInstance(mContext).getWalletBalance(getWalletName(walletType), address).enqueue(new Callback<BalanceModel>() {
            @Override
            public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                onGetWalletBalance.onWalletBalance(response.body().balance);
            }

            @Override
            public void onFailure(Call<BalanceModel> call, Throwable t) {
                onGetWalletBalance.onErrorWalletBalans(t.getMessage());
            }
        });
    }

    public void getTokenBalance(WalletTypes walletType, String address, final OnGetTokenBalance onGetTokenBalance) {
        ZafeplaceApi.getInstance(mContext).getTokenBalance(getWalletName(walletType), address).enqueue(new Callback<BalanceModel>() {
            @Override
            public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                onGetTokenBalance.onTokenBalance(response.body().balance);
            }

            @Override
            public void onFailure(Call<BalanceModel> call, Throwable t) {
                onGetTokenBalance.onErrorTokenBalans(t.getMessage());
            }
        });
    }

    public void getRawTransaction(WalletTypes walletType, String addressSender, String addressRecipient, double amount,
                                  final OnGetRawTransactionHex onGetRawTransactionHex) {
        ZafeplaceApi.getInstance(mContext).getRawTransaction(getWalletName(walletType), addressSender, addressRecipient, amount).enqueue(new Callback<TransactionRaw>() {
            @Override
            public void onResponse(Call<TransactionRaw> call, Response<TransactionRaw> response) {
                Log.d(TAG, " call nat " + call.request().url());
                TransactionRaw raw = response.body();
                Credentials credentials = Credentials.create(PreferencesManager.getEthWallet(mContext).getPrivateKey());
                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(raw.getRawTx().getNonce(), raw.getRawTx().getGasPrice(),
                        raw.getRawTx().getGasLimit(), raw.getRawTx().getTo(), raw.getRawTx().getValue());
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                String hexValue = Numeric.toHexString(signedMessage);
                onGetRawTransactionHex.onGetRawHex(hexValue);
            }

            @Override
            public void onFailure(Call<TransactionRaw> call, Throwable t) {
                onGetRawTransactionHex.onErrorRawHex(t.getMessage());
            }
        });
    }

    public void getTokenTransactionRaw(WalletTypes walletType, String addressSender, String addressRecipient, int amount,
                                       final OnGetRawTokenTransactionHex onGetRawTokenTransactionHex) {
        ZafeplaceApi.getInstance(mContext).getTokenTransactionRaw(getWalletName(walletType), addressSender, addressRecipient, amount).enqueue(new Callback<TransactionRaw>() {
            @Override
            public void onResponse(Call<TransactionRaw> call, Response<TransactionRaw> response) {
                Log.d(TAG, " call " + call.request().url());
                TransactionRaw raw = response.body();
                Credentials credentials = Credentials.create(PreferencesManager.getEthWallet(mContext).getPrivateKey());
                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(raw.getRawTx().getNonce(), raw.getRawTx().getGasPrice(),
                        raw.getRawTx().getGasLimit(), raw.getRawTx().getTo(), raw.getRawTx().getValue());
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                String hexValue = Numeric.toHexString(signedMessage);
                onGetRawTokenTransactionHex.onGetTokenRawHex(hexValue);
            }

            @Override
            public void onFailure(Call<TransactionRaw> call, Throwable t) {
                onGetRawTokenTransactionHex.onErrorTokenRawHex(t.getMessage());
            }
        });
    }

    public void doTransaction(WalletTypes walletType, String signTx, final OnMakeTransaction onMakeTransaction) {
        ZafeplaceApi.getInstance(mContext).doTransaction(signTx, getWalletName(walletType)).enqueue(new Callback<ErrorTransaction>() {
            @Override
            public void onResponse(Call<ErrorTransaction> call, Response<ErrorTransaction> response) {
                try {
                    onMakeTransaction.OnSuccessTransaction(response.body().message);
                } catch (Exception e) {
                    e.printStackTrace();
                    onMakeTransaction.OnErrorTransaction(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ErrorTransaction> call, Throwable t) {
                onMakeTransaction.OnErrorTransaction(t.getMessage());
            }
        });
    }

    public void fingerprintLogin(FingerprintHandler.FingerprintAuthenticationCallback fingerprintAuthenticationCallback) {
        FingerPrintLogin fingerPrintLogin = new FingerPrintLogin(mContext, fingerprintAuthenticationCallback);
        fingerPrintLogin.initAuth();
    }

    public void pinCodeLogin(String code) {
        setIsLoggedIn(true, mContext);
        setAuthType(PIN_AUTH, mContext);
        setPinCode(encryption(ZAFEPLACE_PASSWORD, code), mContext);
    }

    public void generateWallet(WalletTypes walletType, OnWalletGenerateListener onWalletGenerateListener) {
        switch (walletType) {
            case ETH_WALLET:
                generateEthWallet(onWalletGenerateListener);
        }
    }

    private void generateEthWallet(OnWalletGenerateListener onWalletGenerateListener) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            onWalletGenerateListener.onErrorGenerate(mContext.getString(R.string.write_external_storage_permission_not_enabled));
        } else if (!isLoggedIn(mContext)) {
            onWalletGenerateListener.onErrorGenerate(mContext.getString(R.string.you_need_auth_to_generate_wallet));
        } else {
            try {
                Web3j web3 = Web3jFactory.build(new HttpService(ETH_SERVICE_URL));
                String wallet = WalletUtils.generateLightNewWalletFile(ZAFEPLACE_PASSWORD, Environment.getExternalStorageDirectory());
                Credentials credentials = WalletUtils.loadCredentials(ZAFEPLACE_PASSWORD, Environment.getExternalStorageDirectory() + "/" + wallet);
                String privateKey = String.format("%x", credentials.getEcKeyPair().getPrivateKey());
                String address = credentials.getAddress();
                setEthWallet(privateKey, address, mContext);
                deleteFile(Environment.getExternalStorageDirectory() + "/" + wallet);
                onWalletGenerateListener.onSuccessGenerate(address);
            } catch (Throwable e) {
                onWalletGenerateListener.onErrorGenerate(e.getMessage());
            }
        }
    }

    public void saveUserData(String firstName, String secondName, String email, String additionalData) {
        PreferencesManager.setUserData(firstName, secondName, email, additionalData, mContext);
    }

    public Wallet getWallet(WalletTypes walletType) {
        switch (walletType) {
            case ETH_WALLET:
                return PreferencesManager.getEthWallet(mContext);
            default:
                return null;
        }
    }

    public boolean isIdentityExist(WalletTypes walletType) {
        switch (walletType) {
            case ETH_WALLET:
                return !isNull(getEthWallet(mContext));
            default:
                return false;
        }
    }

    public String getPinCode() {
        return isLoggedIn(mContext) ? decryption(ZAFEPLACE_PASSWORD, PreferencesManager.getPinCode(mContext)) :
                mContext.getString(R.string.you_need_auth_to_get_pin_code);
    }

    public List<Wallet> getSemiPublicData() {
        List<Wallet> walletList = new ArrayList<>();
        EthWallet ethWallet = new EthWallet();
        ethWallet.setCurrencyName(ETH);
        ethWallet.setAddress(getEthWallet(mContext).getAddress());
        walletList.add(ethWallet);
        return walletList;
    }

    public void logout() {
        setIsLoggedIn(false, mContext);
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
