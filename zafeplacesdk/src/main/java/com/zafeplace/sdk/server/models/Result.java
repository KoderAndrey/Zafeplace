package com.zafeplace.sdk.server.models;

public class Result {
    public String sender;
    public String recipient;
    public RawTx rawTx;

    @Override
    public String toString() {
        return "Result{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", rawTx=" + rawTx +
                '}';
    }
}
