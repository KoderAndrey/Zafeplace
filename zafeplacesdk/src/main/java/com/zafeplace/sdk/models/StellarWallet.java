package com.zafeplace.sdk.models;

public class StellarWallet extends Wallet {
    private String secretSeed;
    private String accId;


    @Override
    public String getAddress() {
        return accId;
    }

    public void setAddress(String accId) {
        this.accId = accId;
    }

    public String getSecretSeed() {
        return secretSeed;
    }

    public void setSecretSeed(String secretSeed) {
        this.secretSeed = secretSeed;
    }
}
