package com.zafeplace.sdk.server.models;

public class Input {
    public Boolean indexed;
    public String name;
    public String type;

    @Override
    public String toString() {
        return "Input{" +
                "indexed=" + indexed +
                ", name = " + name + " " +
                ", type = " + type + " " +
                "}";
    }
}
