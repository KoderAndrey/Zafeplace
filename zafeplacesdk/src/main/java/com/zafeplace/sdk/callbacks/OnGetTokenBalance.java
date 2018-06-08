package com.zafeplace.sdk.callbacks;

public interface OnGetTokenBalance {
    void onTokenBalance(double balans);

    void onErrorTokenBalance(Throwable error);
}
