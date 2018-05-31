package com.zafeplace.sdk.callbacks;

public interface OnGetRawTokenTransactionHex {
    void onGetTokenRawHex(String rawHex);

    void onErrorTokenRawHex(String error);
}
