package com.zafeplace.sdk.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.zafeplace.sdk.models.EthWallet;
import com.zafeplace.sdk.models.User;

import static com.zafeplace.sdk.Constants.ZAFEPLACE_PASSWORD;
import static com.zafeplace.sdk.utils.EncryptionUtils.decryption;
import static com.zafeplace.sdk.utils.EncryptionUtils.encryption;

public class PreferencesManager {

    private final static String ACCESS_TOKEN_PREF_KEY = "access_token_pref_key";
    private final static String ETH_WALLET_PREF_KEY = "eth_wallet_pref_key";
    private final static String USER_PREF_KEY = "user_pref_key";

    private final static String IS_LOGGED_IN_PREF_KEY = "is_logged_in_pref_key";
    private final static String AUTH_TYPE_PREF_KEY = "auth_type_pref_key";
    private final static String PIN_CODE_PREF_KEY = "pin_code_pref_key";

    public void setAuthToken(String token, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(ACCESS_TOKEN_PREF_KEY, token).apply();
    }

    public String getAuthToken(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(ACCESS_TOKEN_PREF_KEY, null);
    }

    public void setIsLoggedIn(boolean isLoggedIn, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(IS_LOGGED_IN_PREF_KEY, isLoggedIn).apply();
    }

    public boolean isLoggedIn(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(IS_LOGGED_IN_PREF_KEY, false);
    }

    public void setEthWallet(String privateKey, String address, Context context) {
        EthWallet ethWallet = new EthWallet();
        ethWallet.setPrivateKey(encryption(ZAFEPLACE_PASSWORD, privateKey));
        ethWallet.setAddress(encryption(ZAFEPLACE_PASSWORD, address));
        String userString = new Gson().toJson(ethWallet);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(ETH_WALLET_PREF_KEY, userString).apply();
    }

    public EthWallet getEthWallet(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String wallet = prefs.getString(ETH_WALLET_PREF_KEY, null);
        EthWallet ethWallet = new Gson().fromJson(wallet, EthWallet.class);
        if (ethWallet == null) return null;
        ethWallet.setPrivateKey(decryption(ZAFEPLACE_PASSWORD, ethWallet.getPrivateKey()));
        ethWallet.setAddress(decryption(ZAFEPLACE_PASSWORD, ethWallet.getAddress()));
        return ethWallet;
    }

    public void setAuthType(int authType, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(AUTH_TYPE_PREF_KEY, authType).apply();
    }

    public int getAuthType(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(AUTH_TYPE_PREF_KEY, -1);
    }

    public void setPinCode(String pinCode, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(PIN_CODE_PREF_KEY, pinCode).apply();
    }

    public String getPinCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PIN_CODE_PREF_KEY, null);
    }

    public void setUserData(String firstName, String secondName, String email, String additionalData, Context context) {
        User user = new User();
        user.setFirstName(encryption(ZAFEPLACE_PASSWORD, firstName));
        user.setSecondName(encryption(ZAFEPLACE_PASSWORD, secondName));
        user.setEmail(encryption(ZAFEPLACE_PASSWORD, email));
        user.setAdditionalData(encryption(ZAFEPLACE_PASSWORD, additionalData));
        user.setValidationTime(System.currentTimeMillis());
        String userString = new Gson().toJson(user);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(USER_PREF_KEY, userString).apply();
    }

    public User getUserData(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String wallet = prefs.getString(USER_PREF_KEY, null);
        User user = new Gson().fromJson(wallet, User.class);
        user.setFirstName(decryption(ZAFEPLACE_PASSWORD, user.getFirstName()));
        user.setSecondName(decryption(ZAFEPLACE_PASSWORD, user.getSecondName()));
        user.setEmail(decryption(ZAFEPLACE_PASSWORD, user.getEmail()));
        user.setAdditionalData(decryption(ZAFEPLACE_PASSWORD, user.getAdditionalData()));
        return user;
    }
}
