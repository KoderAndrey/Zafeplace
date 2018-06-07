package com.zafeplace.sdk.callbacks;

public interface OnAccessTokenListener {
    void onGetToken(String response);

    void onErrorToken(Throwable error);
}
