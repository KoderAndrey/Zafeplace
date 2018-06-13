package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.AssetType;

/**
 * Represents Stellar native asset - <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">lumens (XLM)</a>
 *
 * @see <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">Assets</a>
 */
public final class AssetTypeNative extends Asset {

    public AssetTypeNative() {
    }

    @Override
    public String getType() {
        return "native";
    }

    @Override
    public boolean equals(Object object) {
        return this.getClass().equals(object.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public   com.zafeplace.sdk.stellarsdk.sdk.xdr.Asset toXdr() {
        com.zafeplace.sdk.stellarsdk.sdk.xdr.Asset xdr = new   com.zafeplace.sdk.stellarsdk.sdk.xdr.Asset();
        xdr.setDiscriminant(AssetType.ASSET_TYPE_NATIVE);
        return xdr;
    }
}
