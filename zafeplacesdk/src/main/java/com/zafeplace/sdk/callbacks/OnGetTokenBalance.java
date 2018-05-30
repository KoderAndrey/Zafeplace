package com.zafeplace.sdk.callbacks;

public interface OnGetTokenBalance {
    void onTokenBalance(String balans);

    void onErrorTokenBalans(String error);
}
