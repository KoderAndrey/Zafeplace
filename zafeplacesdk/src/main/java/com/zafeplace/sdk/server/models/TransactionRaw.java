package com.zafeplace.sdk.server.models;

public class TransactionRaw {
    public String network;
    public boolean constant;
    public Result result;

    @Override
    public String toString() {
        return "TransactionRaw{" +
                "network='" + network + '\'' +
                ", constant=" + constant +
                ", result=" + result +
                '}';
    }
}
