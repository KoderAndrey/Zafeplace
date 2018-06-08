package com.zafeplace.sdk.server.models;

public class SmartContractTransactionRaw {
    public String network;
    public boolean constant;
    public ResultSmart result;

    @Override
    public String toString() {
        return "SmartContractTransactionRaw{" +
                "network='" + network + '\'' +
                ", constant=" + constant +
                ", result=" + result +
                '}';
    }
}
