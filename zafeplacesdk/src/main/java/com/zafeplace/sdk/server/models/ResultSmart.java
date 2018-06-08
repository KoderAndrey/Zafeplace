package com.zafeplace.sdk.server.models;

import java.util.List;

public class ResultSmart {
    public List<Abi> abi = null;
    public String address;

    @Override
    public String toString() {
        return "ResultSmart{" +
                "abi=" + abi +
                ", address='" + address + '\'' +
                '}';
    }
}
