package com.zafeplace.sdk.callbacks;

public interface OnSmartContractRaw {
    void onGetSmartContractRaw(String response);

    void onErrorSmartRaw(Throwable error);
}
