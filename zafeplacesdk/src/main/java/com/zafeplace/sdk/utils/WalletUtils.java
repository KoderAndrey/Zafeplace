package com.zafeplace.sdk.utils;

import com.zafeplace.sdk.Zafeplace;

import static com.zafeplace.sdk.Constants.WalletType.ETH;

public class WalletUtils {

    public static String getWalletName(Zafeplace.WalletTypes walletType){
        switch (walletType){
            case ETH_WALLET: return ETH;
            default: return  ETH;
        }
    }
}
