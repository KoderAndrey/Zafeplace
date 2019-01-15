package com.zafeplace.sdk.server.models;

import java.math.BigInteger;

public class Result_ {
    public String from;
    public BigInteger nonce;
    public String gasPrice;
    public String gasLimit;
    public String to;
    public BigInteger value;
    public BigInteger chainId;
    public String rawTx;
    public String data;

    @Override
    public String toString() {
        return "Result_{" +
                "from='" + from + '\'' +
                ", nonce=" + nonce +
                ", gasPrice='" + gasPrice + '\'' +
                ", gasLimit='" + gasLimit + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", chainId=" + chainId +
                ", rawTx='" + rawTx + '\'' +
                '}';
    }
}
