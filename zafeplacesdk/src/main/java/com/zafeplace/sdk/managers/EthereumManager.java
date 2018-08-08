package com.zafeplace.sdk.managers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.zafeplace.sdk.R;
import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnExecuteMethodSmartContract;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRawList;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.exception.ZafeplaceException;
import com.zafeplace.sdk.server.ZafeplaceApi;
import com.zafeplace.sdk.server.models.Abi;
import com.zafeplace.sdk.server.models.ContractModel;
import com.zafeplace.sdk.server.models.MethodParamsSmart;
import com.zafeplace.sdk.server.models.ResultModel;
import com.zafeplace.sdk.server.models.SmartContractTransactionRaw;
import com.zafeplace.sdk.server.models.TransactionRaw;

//import org.web3j.protocol.Web3jFactory;
//import org.web3j.protocol.http.HttpService;


import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zafeplace.sdk.Constants.ETH_SERVICE_URL;
import static com.zafeplace.sdk.Constants.ZAFEPLACE_PASSWORD;
import static com.zafeplace.sdk.utils.StorageUtils.deleteFile;
import static com.zafeplace.sdk.utils.WalletUtils.getWalletName;

//import org.web3j.crypto.TransactionEncoder;

public class EthereumManager extends WalletManager {

    @Override
    public void generateWallet(OnWalletGenerateListener onWalletGenerateListener, Activity activity, boolean isLoggedIn) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            onWalletGenerateListener.onErrorGenerate(new ZafeplaceException(activity.getString(R.string.write_external_storage_permission_not_enabled)));
        } else if (!isLoggedIn) {
            onWalletGenerateListener.onErrorGenerate(new ZafeplaceException(activity.getString(R.string.you_need_auth_to_generate_wallet)));
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
    public void makeTransaction(String addressSender, String addressRecipient, double amount,
                                OnMakeTransaction onMakeTransaction, Activity activity) {
        ZafeplaceApi.getInstance(activity).getRawTransaction(getWalletName(getWalletType()), addressSender, addressRecipient, amount).enqueue(new Callback<TransactionRaw>() {
            @Override
            public void onResponse(@NonNull Call<TransactionRaw> call, @NonNull Response<TransactionRaw> response) {
                executeTransaction(response, activity, onMakeTransaction);
            }

            @Override
            public void onFailure(@NonNull Call<TransactionRaw> call, @NonNull Throwable t) {
                onMakeTransaction.onBreakTransaction();
                showErrorDialog(t.getMessage(), activity);
            }
        });
    }

    @Override
    public void makeTransactionToken(String addressSender, String addressRecipient, int amount, OnMakeTransaction onMakeTransaction, Activity activity) {
        ZafeplaceApi.getInstance(activity).getTokenTransactionRaw(getWalletName(getWalletType()), addressSender, addressRecipient, amount).enqueue(new Callback<TransactionRaw>() {
            @Override
            public void onResponse(@NonNull Call<TransactionRaw> call, @NonNull Response<TransactionRaw> response) {
                executeTransaction(response, activity, onMakeTransaction);
            }

            @Override
            public void onFailure(@NonNull Call<TransactionRaw> call, @NonNull Throwable t) {
                onMakeTransaction.onBreakTransaction();
                showErrorDialog(t.getMessage(), activity);
            }
        });
    }

    @Override
    public Zafeplace.WalletTypes getWalletType() {
        return Zafeplace.WalletTypes.ETH_WALLET;
    }

    public void getSmartContractTransactionRaw(Activity activity, OnSmartContractRawList onSmartContractRaw) {
        ZafeplaceApi.getInstance(activity).getSmartContractRaw(getWalletName(getWalletType())).enqueue(new Callback<SmartContractTransactionRaw>() {
            @Override
            public void onResponse(@NonNull Call<SmartContractTransactionRaw> call, @NonNull Response<SmartContractTransactionRaw> response) {
                try {
                    SmartContractTransactionRaw mes = response.body();
                    List<Abi> abis = mes.result.abi;
                    onSmartContractRaw.onGetSmartContractAbiList(abis);
                } catch (Exception e) {
                    onSmartContractRaw.onErrorSmartRaw(e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SmartContractTransactionRaw> call, @NonNull Throwable t) {
                onSmartContractRaw.onErrorSmartRaw(t);
            }
        });
    }

    public void executeSmartContractMethod(String nameFunk, String sender,
                                           List<MethodParamsSmart> methodParamsSmarts,
                                           OnExecuteMethodSmartContract onExecuteMethodSmartContract,
                                           Activity activity) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = methodParamsSmarts.size();
        for (int i = 0; i < size; i++) {
            if (size == 1) {
                stringBuilder.append("[").append(methodParamsSmarts.get(0).toString()).append("]");
            } else {

                if (i == 0) {
                    stringBuilder.append("[").append(methodParamsSmarts.get(0).toString()).append(",");
                } else if (i == size - 1) {
                    stringBuilder.append(methodParamsSmarts.get(i).toString()).append("]");
                } else {
                    stringBuilder.append(methodParamsSmarts.get(i).toString()).append(",");
                }
            }
        }
        String resultCon = stringBuilder.toString();
        ContractModel contractModel = new ContractModel(sender, nameFunk, resultCon);
        ZafeplaceApi.getInstance(activity).executeContractInformationMethod("ethereum", contractModel).enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(@NonNull Call<ResultModel> call, @NonNull Response<ResultModel> response) {
                try {
                    onExecuteMethodSmartContract.onExecuteContract(response.body().result);
                } catch (Exception e) {
                    onExecuteMethodSmartContract.onErrorExecuteConract(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultModel> call, @NonNull Throwable t) {
                onExecuteMethodSmartContract.onErrorExecuteConract(t);
            }
        });
    }

    private void executeTransaction(Response<TransactionRaw> response, Activity activity, OnMakeTransaction onMakeTransaction) {
//        TransactionRaw raw = response.body();
//        Credentials credentials = Credentials.create(getPreferencesManager().getEthWallet(activity).getPrivateKey());
//        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(raw.result.rawTx.result.nonce, new BigInteger(raw.result.rawTx.result.gasPrice),
//                new BigInteger(raw.result.rawTx.result.gasLimit), raw.result.rawTx.result.to, raw.result.rawTx.result.value);
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        String hexValue = Numeric.toHexString(signedMessage);
//        showDialog(hexValue, onMakeTransaction, getWalletType(), activity);
    }
}
