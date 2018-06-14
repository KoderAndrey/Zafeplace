package com.zafeplace.sdk.utils;

import com.zafeplace.sdk.Zafeplace;

import static com.zafeplace.sdk.Constants.WalletType.ETH;
import static com.zafeplace.sdk.Constants.WalletType.STELLAR;

public class WalletUtils {

    public static String getWalletName(Zafeplace.WalletTypes walletType) {
        switch (walletType) {
            case ETH_WALLET:
                return ETH;
            case STELLAR_WALLET:
                return STELLAR;
            default:
                return null;
        }
    }
}
