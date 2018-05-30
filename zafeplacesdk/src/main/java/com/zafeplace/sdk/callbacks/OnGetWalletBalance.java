package com.zafeplace.sdk.callbacks;

public interface OnGetWalletBalance {
    void onWalletBalance(String balans);

    void onErrorWalletBalans(String error);
}
