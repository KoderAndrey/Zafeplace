package com.zafeplace.sdk.models;

public class User {

    private long validationTime;
    private String firstName;
    private String secondName;
    private String email;
    private String additionalData;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public long getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(long validationTime) {
        this.validationTime = validationTime;
    }
}
