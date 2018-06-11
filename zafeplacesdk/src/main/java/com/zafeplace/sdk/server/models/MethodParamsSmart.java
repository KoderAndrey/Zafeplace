package com.zafeplace.sdk.server.models;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

public class MethodParamsSmart {
    String name;
    Object value;

    public MethodParamsSmart(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        if (value instanceof String) {
            return "{" +
                     "\"" + "name" + "\"" + ":" +  "\"" + name +  "\"" + "," +
                     "\"" + "value" +  "\"" + ":" +  "\"" + value + "\"" +
                    "}";
        } else {
            return "{" +
                     "\"" + "name" +  "\"" + ":" + name +  "\"" + "," +
                    "\"" + "value" +  "\"" + ":" + value +
                    "}";
        }
    }
}
