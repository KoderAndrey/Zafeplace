package com.zafeplace.sdk.server.models;

public class ContractModel {
    public String sender;
    public String methodName;
    public String methodParams;
    public int gasLimit;
    public int gasPrice;

    public ContractModel(String sender, String methodName, String methodParams) {
        this.sender = sender;
        this.methodName = methodName;
        this.methodParams = methodParams;
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
    }

    public ContractModel(String sender, String methodName, String methodParams, int gasLimit, int gasPrice) {
        this.sender = sender;
        this.methodName = methodName;
        this.methodParams = methodParams;
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
    }
}
