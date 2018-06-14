package com.zafeplace.sdk.server.models;

import java.util.List;

public class TokenBalans {
    public String network;
    public boolean constant;
    public List<ResultToken> result = null;

    @Override
    public String toString() {
        return "TokenBalans{" +
                "network='" + network + '\'' +
                ", constant=" + constant +
                ", result=" + result +
                '}';
    }
}
