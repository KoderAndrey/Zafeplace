package com.zafeplace.sdk.callbacks;

public interface OnGetRawTransactionHex {
    void onGetRawHex(String rawHex);

    void onErrorRawHex(String error);
}
