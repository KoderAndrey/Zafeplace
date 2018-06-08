package com.zafeplace.sdk.callbacks;

public interface OnGetWalletBalance {
    void onWalletBalance(double balans);

    void onErrorWalletBalance(Throwable error);
}
