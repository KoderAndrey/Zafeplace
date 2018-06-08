package com.zafeplace.sdk.callbacks;

public interface OnExecuteMethodSmartContract {
    void onExecuteContract();

    void onErrorExecuteConract(Throwable e);
}
