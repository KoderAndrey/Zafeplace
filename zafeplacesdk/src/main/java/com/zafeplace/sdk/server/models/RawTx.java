package com.zafeplace.sdk.server.models;

public class RawTx {
    public boolean constant;
    public Result_ result;

    @Override
    public String toString() {
        return "RawTx{" +
                "constant=" + constant +
                ", result=" + result +
                '}';
    }
}
