package com.zafeplace.sdk.callbacks;

public interface OnMakeTransaction {
    void OnSuccessTransaction(String res);

    void OnErrorTransaction(String error);
}
