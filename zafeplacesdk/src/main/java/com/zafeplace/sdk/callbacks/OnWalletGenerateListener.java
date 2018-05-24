package com.zafeplace.sdk.callbacks;

public interface OnWalletGenerateListener {
    void onSuccess(String address);
    void onError(String error);
}
