package com.zafeplace.sdk.callbacks;

public interface OnWalletGenerateListener {
    void onSuccessGenerate(String address);
    void onErrorGenerate(String error);
}
