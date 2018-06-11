package com.zafeplace.sdk.callbacks;

public interface OnExecuteMethodSmartContract {
    void onExecuteContract(String result);

    void onErrorExecuteConract(Throwable e);
}
