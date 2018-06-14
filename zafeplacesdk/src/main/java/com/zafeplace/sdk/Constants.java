package com.zafeplace.sdk;

public interface Constants {
    String ZAFEPLACE_PASSWORD = "zafeplace_password";
    String ETH_SERVICE_URL = "https://mainnet.infura.io/";

    interface Extras {

    }

    interface AuthType{
        int FINGERPRINT_AUTH = 0;
        int PIN_AUTH = 1;
    }

    interface WalletType{
        String ETH = "ethereum";
        String STELLAR = "stellar";
    }
}
