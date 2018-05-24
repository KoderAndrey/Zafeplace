package com.zafeplace.sdk.callbacks;

public interface OnResponseListener {
    void onSuccess();
    void onError(String error);
}
