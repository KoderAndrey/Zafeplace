package com.zafeplace.sdk.server.models;

public class ErrorTransaction {   //todo create success transaction model or change current
    public String message;
    public Integer errorCode;

    public ErrorTransaction(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
