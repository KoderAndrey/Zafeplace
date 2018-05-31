package com.zafeplace.sdk.server.models;

public class TransactionRaw {
    private String sender;
    private RawTx rawTx;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public RawTx getRawTx() {
        return rawTx;
    }

    public void setRawTx(RawTx rawTx) {
        this.rawTx = rawTx;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
