package com.zafeplace.sdk.callbacks;

public interface OnWalletGenerateListener {
    void onSuccess(String privateKey);
    void onError(String error);
}
