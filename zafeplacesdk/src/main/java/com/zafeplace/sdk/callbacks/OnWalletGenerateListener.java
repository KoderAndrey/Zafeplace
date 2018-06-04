package com.zafeplace.sdk.callbacks;

public interface OnWalletGenerateListener {
    void onStartGenerate();

    void onSuccessGenerate(String address);

    void onErrorGenerate(String error);
}
