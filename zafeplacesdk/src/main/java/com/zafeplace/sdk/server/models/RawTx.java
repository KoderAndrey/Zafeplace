package com.zafeplace.sdk.server.models;

import java.math.BigInteger;

public class RawTx {
    private String from;
    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private BigInteger value;
    private BigInteger chainId;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getChainId() {
        return chainId;
    }

    public void setChainId(BigInteger chainId) {
        this.chainId = chainId;
    }

    @Override
    public String toString() {
        return "RawTx{" +
                "from='" + from + '\'' +
                ", nonce='" + nonce + '\'' +
                ", gasPrice='" + gasPrice + '\'' +
                ", gasLimit='" + gasLimit + '\'' +
                ", to='" + to + '\'' +
                ", value='" + value + '\'' +
                ", chainId='" + chainId + '\'' +
                '}';
    }
}
