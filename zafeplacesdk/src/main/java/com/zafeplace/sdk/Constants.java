package com.zafeplace.sdk;

public interface Constants {
    String ZAFEPLACE_PASSWORD = "zafeplace_password";
    String ETH_SERVICE_URL = "https://mainnet.infura.io/";

    interface Extras {

    }

    interface TransactionType {
        int TOKEN_TRANSACTION = 11;
        int COIN_TRANSACTION = 12;
    }

    interface AuthType {
        int FINGERPRINT_AUTH = 0;
        int PIN_AUTH = 1;
    }

    interface WalletType {
        String ETH = "ethereum";
        String STELLAR = "stellar";
    }
}
