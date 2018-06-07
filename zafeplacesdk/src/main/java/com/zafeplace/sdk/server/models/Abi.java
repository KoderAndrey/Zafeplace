package com.zafeplace.sdk.server.models;

import java.util.List;

public class Abi {
    public Boolean anonymous;
    public List<Input> inputs = null;
    public String name;
    public String type;
    public Boolean constant;
    public List<Output> outputs = null;
    public Boolean payable;
    public String stateMutability;

    @Override
    public String toString() {
        return "Abi{" +
                "anonymous=" + anonymous +
                ", name = " + name + " " +
                ", type = " + type + " " +
                ", constant = " + constant + " " +
                ", payable = " + payable + " " +
                ", stateMutability = " + stateMutability +
                "}";
    }
}
