package com.zafeplace.sdk.server.models;

public class ResultToken {
    public String balance;
    public String asset_type;

    @Override
    public String toString() {
        return
                "balance='" + balance + '\'' +
                        ", assetType='" + asset_type + "'";
    }
}
