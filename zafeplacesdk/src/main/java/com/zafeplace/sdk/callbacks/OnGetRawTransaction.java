package com.zafeplace.sdk.callbacks;

import com.zafeplace.sdk.server.models.TransactionRaw;

public interface OnGetRawTransaction {
    void onGetRaw(TransactionRaw raw);

    void onErrorRaw(String error);
}
