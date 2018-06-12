package com.zafeplace.sdk.callbacks;

import com.zafeplace.sdk.server.models.ResultToken;

import java.util.List;

public interface OnGetTokenBalance {
    void onTokenBalance(List<ResultToken> tokenBalans);

    void onErrorTokenBalance(Throwable error);
}
