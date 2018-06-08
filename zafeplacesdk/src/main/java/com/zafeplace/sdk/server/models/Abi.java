package com.zafeplace.sdk.server.models;

import java.util.List;

public class Abi {
    public boolean anonymous;
    public List<Input> inputs = null;
    public String name;
    public String type;
    public boolean constant;
    public List<Output> outputs = null;
    public boolean payable;
    public String stateMutability;

    @Override
    public String toString() {
        return "Abi{" +
                "anonymous=" + anonymous +
                ", inputs=" + inputs +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", constant=" + constant +
                ", outputs=" + outputs +
                ", payable=" + payable +
                ", stateMutability='" + stateMutability + '\'' +
                '}';
    }
}
