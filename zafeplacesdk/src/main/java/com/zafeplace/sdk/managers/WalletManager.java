package com.zafeplace.sdk.managers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.zafeplace.sdk.R;
import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.callbacks.OnGetTokenBalance;
import com.zafeplace.sdk.callbacks.OnGetWalletBalance;
import com.zafeplace.sdk.callbacks.OnMakeTransaction;
import com.zafeplace.sdk.callbacks.OnSmartContractRawList;
import com.zafeplace.sdk.callbacks.OnWalletGenerateListener;
import com.zafeplace.sdk.managers.PreferencesManager;
import com.zafeplace.sdk.server.ZafeplaceApi;
import com.zafeplace.sdk.server.models.BalanceModel;
import com.zafeplace.sdk.server.models.ResultToken;
import com.zafeplace.sdk.server.models.TokenBalans;
import com.zafeplace.sdk.utils.FingerPrintLogin;
import com.zafeplace.sdk.utils.FingerprintHandler;
import com.zafeplace.sdk.utils.ParseUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zafeplace.sdk.Constants.AuthType.FINGERPRINT_AUTH;
import static com.zafeplace.sdk.Constants.AuthType.PIN_AUTH;
import static com.zafeplace.sdk.Constants.TransactionType.COIN_TRANSACTION;
import static com.zafeplace.sdk.Constants.TransactionType.TOKEN_TRANSACTION;
import static com.zafeplace.sdk.Constants.ZAFEPLACE_PASSWORD;
import static com.zafeplace.sdk.utils.EncryptionUtils.decryption;
import static com.zafeplace.sdk.utils.WalletUtils.getWalletName;

public abstract class WalletManager {

    private ExecutorService mExecutor;
    private PreferencesManager mPreferencesManager;
    private AlertDialog mAlert = null;
    private FingerPrintLogin mFingerPrintLogin;
    private boolean mIsCancelClicked;
    private EditText mInput;

    public WalletManager() {
        mPreferencesManager = new PreferencesManager();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    protected abstract void generateWallet(OnWalletGenerateListener onWalletGenerateListener, Activity activity, boolean isLoggedIn);

    public void getWalletBalance(String address, final OnGetWalletBalance onGetWalletBalance, Activity activity) {
        ZafeplaceApi.getInstance(activity).getWalletBalance(getWalletName(getWalletType()), address).enqueue(new Callback<BalanceModel>() {
            @Override
            public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                onGetWalletBalance.onWalletBalance(response.body());
            }

            @Override
            public void onFailure(Call<BalanceModel> call, Throwable t) {
                onGetWalletBalance.onErrorWalletBalance(t);
            }
        });
    }

    public void getTokenBalance(String address, final OnGetTokenBalance onGetTokenBalance, Activity activity) {
        ZafeplaceApi.getInstance(activity).getTokenBalance(getWalletName(getWalletType()), address).enqueue(new Callback<TokenBalans>() {
            @Override
            public void onResponse(Call<TokenBalans> call, Response<TokenBalans> response) {
                try {
                    TokenBalans tokenBalans = response.body();
                    List<ResultToken> resultTokens = tokenBalans.result;
                    onGetTokenBalance.onTokenBalance(resultTokens);
                } catch (Exception e) {
                    onGetTokenBalance.onErrorTokenBalance(e);
                }
            }

            @Override
            public void onFailure(Call<TokenBalans> call, Throwable t) {
                onGetTokenBalance.onErrorTokenBalance(t);
            }
        });
    }

    private void doTransaction(Zafeplace.WalletTypes walletType, String signTx, final OnMakeTransaction onMakeTransaction, Activity activity) {
        ZafeplaceApi.getInstance(activity).doTransaction(signTx, getWalletName(walletType)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.wtf("tag", response.message());
                try {
                    onMakeTransaction.onSuccessTransaction(ParseUtils.transactionMessage(response.body()));
                } catch (Exception e) {
                    e.printStackTrace();
                    onMakeTransaction.onErrorTransaction(e);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onMakeTransaction.onErrorTransaction(t);
            }
        });
    }


    protected void showDialog(final String message, final OnMakeTransaction onMakeTransaction, Zafeplace.WalletTypes walletType, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                R.style.SimpleDialogTheme)).setTitle("Do you really want to do this transaction?").setNegativeButton("Cancel", (dialog, which) -> {
            onMakeTransaction.onBreakTransaction();
            dialog.dismiss();
        }).setPositiveButton("Accept", (dialog, which) -> doTransaction(walletType, message, onMakeTransaction, activity));
        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    protected void showErrorDialog(final String message, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                R.style.SimpleDialogTheme)).setTitle("Error transaction " + message).setNeutralButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.setMessage(message);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }


    public abstract void makeTransaction(String addressSender, String addressRecipient, double amount,
                                         final OnMakeTransaction onMakeTransaction, Activity activity);

    public abstract void makeTransactionToken(String addressSender, String addressRecipient, int amount,
                                              final OnMakeTransaction onMakeTransaction, Activity activity);

    protected PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    protected ExecutorService getExecutor() {
        return mExecutor;
    }

    private void cancelFingerprintLogin(FingerPrintLogin fingerPrintLogin) {
        if (fingerPrintLogin != null) {
            fingerPrintLogin.stopAuth();
        }

    }

    public void checkLoginGenerateWallet(final OnWalletGenerateListener onWalletGenerateListener, Activity activity) {
        String title = "";
        String titleButton = "";
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                R.style.SimpleDialogTheme));
        builder.setCancelable(true);
        switch (getAuthType(activity)) {
            case FINGERPRINT_AUTH:
                title = "Please use fingerprint for authorization";
                titleButton = "Cancel";
                fingerprintLogin((message, isSuccess) -> {
                    if (isSuccess) {
                        closeDialog();
                        this.generateWallet(onWalletGenerateListener, activity, isLoggedIn(activity));
                        onWalletGenerateListener.onStartGenerate();
                    } else {
                        if (!mIsCancelClicked) {
                            Toast.makeText(activity, "Wrong Fingerprint!", Toast.LENGTH_SHORT).show();
                        } else {
                            mIsCancelClicked = false;
                        }
                    }
                }, activity);
                break;
            case PIN_AUTH:
                title = "Input Pin Code";
                titleButton = "OK";
                mInput = new EditText(activity);
                mInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                mInput.setLayoutParams(lp);
                builder.setView(mInput);
                break;
        }
        builder.setTitle(title).setNeutralButton(titleButton, (dialog, which) -> {
            switch (getAuthType(activity)) {
                case PIN_AUTH: {
                    if (mInput.getText().toString().equals(getPinCode(activity))) {
                        this.generateWallet(onWalletGenerateListener, activity, isLoggedIn(activity));
                        onWalletGenerateListener.onStartGenerate();
                    } else {
                        Toast.makeText(activity, "Wrong Pin!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case FINGERPRINT_AUTH:
                    mIsCancelClicked = true;
                    cancelFingerprintLogin();
                    break;
            }
        });

        mAlert = builder.show();
        mAlert.setOnDismissListener((dialog) -> {
            mIsCancelClicked = true;
        });
    }

    public void createTransaction(final String addressSender, final String addressRecipient, final String amount,
                                  final OnMakeTransaction onMakeTransaction,
                                  Activity activity, int transactionType) {
        String title = "";
        String titleButton = "";
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                R.style.SimpleDialogTheme));
        switch (getAuthType(activity)) {
            case FINGERPRINT_AUTH:
                title = "Please use fingerprint for authorization";
                titleButton = "Cancel";
                fingerprintLogin((message, isSuccess) -> {
                    if (isSuccess) {
                        closeDialog();
                        onMakeTransaction.onStartTransaction();
                        switch (transactionType) {
                            case TOKEN_TRANSACTION:
                                this.makeTransactionToken(addressSender, addressRecipient, Integer.parseInt(amount), onMakeTransaction, activity);
                                break;
                            case COIN_TRANSACTION:
                                this.makeTransaction(addressSender, addressRecipient, Double.parseDouble(amount), onMakeTransaction, activity);
                                break;
                        }
                    } else {
                        if (!mIsCancelClicked) {
                            Toast.makeText(activity, "Wrong Fingerprint!", Toast.LENGTH_SHORT).show();
                        } else {
                            mIsCancelClicked = false;
                        }
                    }
                }, activity);
                break;
            case PIN_AUTH:
                title = "Input Pin Code";
                titleButton = "OK";
                mInput = new EditText(activity);
                mInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                mInput.setLayoutParams(lp);
                builder.setView(mInput);
                break;
        }
        builder.setTitle(title).setNeutralButton(titleButton, (dialog, which) -> {
            switch (getAuthType(activity)) {
                case PIN_AUTH: {
                    if (mInput.getText().toString().equals(getPinCode(activity))) {
                        onMakeTransaction.onStartTransaction();
                        switch (transactionType) {
                            case TOKEN_TRANSACTION:
                                this.makeTransactionToken(addressSender, addressRecipient, Integer.parseInt(amount), onMakeTransaction, activity);
                                break;
                            case COIN_TRANSACTION:
                                this.makeTransaction(addressSender, addressRecipient, Double.parseDouble(amount), onMakeTransaction, activity);
                                break;
                        }
                    } else {
                        Toast.makeText(activity, "Wrong Pin!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case FINGERPRINT_AUTH:
                    mIsCancelClicked = true;
                    cancelFingerprintLogin();
                    break;
            }
        });
        builder.setView(mInput);
        mAlert = builder.show();
        mAlert.setOnDismissListener((dialog) -> {
            mIsCancelClicked = true;
        });
    }

    public abstract Zafeplace.WalletTypes getWalletType();

    private int getAuthType(Activity activity) {
        return getPreferencesManager().getAuthType(activity);
    }

    private void closeDialog() {
        mAlert.dismiss();
    }

    private void cancelFingerprintLogin() {
        if (mFingerPrintLogin != null) {
            mFingerPrintLogin.stopAuth();
        }
    }

    public void fingerprintLogin(FingerprintHandler.FingerprintAuthenticationCallback fingerprintAuthenticationCallback, Activity activity) {
        mFingerPrintLogin = new FingerPrintLogin(activity, fingerprintAuthenticationCallback);
        mFingerPrintLogin.initAuth();
    }

    public String getPinCode(Activity activity) {
        return getPreferencesManager().isLoggedIn(activity) ? decryption(ZAFEPLACE_PASSWORD, getPreferencesManager().getPinCode(activity)) :
                activity.getString(R.string.you_need_auth_to_get_pin_code);
    }

    public boolean isLoggedIn(Activity activity) {
        return getPreferencesManager().isLoggedIn(activity);
    }
}
