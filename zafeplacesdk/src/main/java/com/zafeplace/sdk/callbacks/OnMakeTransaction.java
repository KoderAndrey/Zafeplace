package com.zafeplace.sdk.callbacks;

public interface OnMakeTransaction {

    void onStartTransaction();

    void onBreakTransaction();

    void onSuccessTransaction(String res);

    void onErrorTransaction(Throwable error);
}
