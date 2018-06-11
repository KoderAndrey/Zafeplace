package com.zafeplace.sdk.server.models;

import java.util.List;

public class ContractModel {
    public String sender;
    public String methodName;
    public String methodParams;

    public ContractModel(String sender, String methodName, String methodParams) {
        this.sender = sender;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }


    @Override
    public String toString() {
        return "ContractModel{" +
                "sender='" + sender + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodParams='" + methodParams + '\'' +
                '}';
    }
}
