package com.zafeplace.sdk.callbacks;

import com.zafeplace.sdk.server.models.Abi;

import java.util.List;

public interface OnSmartContractRawList {

    void onGetSmartContractAbiList(List<Abi> abis);

    void onErrorSmartRaw(Throwable error);
}
