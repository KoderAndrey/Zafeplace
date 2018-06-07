package com.zafeplace.sdk.callbacks;

public interface OnGetTokenBalance {
    void onTokenBalance(String balans);

    void onErrorTokenBalance(Throwable error);
}
