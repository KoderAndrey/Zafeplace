package com.zafeplace.sdk.server.models;

public class BalanceModel {

    public String network;
    public boolean constant;
    public double result;

    @Override
    public String toString() {
        return "BalanceModel{" +
                "network='" + network + '\'' +
                ", constant=" + constant +
                ", result=" + result +
                '}';
    }
}
