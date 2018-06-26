package com.zafeplace.sdk.callbacks;

import com.zafeplace.sdk.Zafeplace;
import com.zafeplace.sdk.server.models.BalanceModel;

public interface OnGetWalletBalance {
    void onWalletBalance(BalanceModel balance);

    void onErrorWalletBalance(Throwable error);
}
