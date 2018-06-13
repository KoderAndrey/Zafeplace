package com.zafeplace.sdk.stellarsdk.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

import com.zafeplace.sdk.stellarsdk.sdk.Asset;
import com.zafeplace.sdk.stellarsdk.sdk.AssetTypeNative;
import com.zafeplace.sdk.stellarsdk.sdk.KeyPair;

/**
 * Represents AllowTrust operation response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/operation.html" target="_blank">Operation documentation</a>
 * @see com.zafeplace.sdk.stellarsdk.sdk.requests.OperationsRequestBuilder
 * @see com.zafeplace.sdk.stellarsdk.sdk.Server#operations()
 */
public class AllowTrustOperationResponse extends OperationResponse {
  @SerializedName("trustor")
  protected final KeyPair trustor;
  @SerializedName("trustee")
  protected final KeyPair trustee;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;
  @SerializedName("authorize")
  protected final boolean authorize;

  AllowTrustOperationResponse(boolean authorize, String assetIssuer, String assetCode, String assetType, KeyPair trustee, KeyPair trustor) {
    this.authorize = authorize;
    this.assetIssuer = assetIssuer;
    this.assetCode = assetCode;
    this.assetType = assetType;
    this.trustee = trustee;
    this.trustor = trustor;
  }

  public KeyPair getTrustor() {
    return trustor;
  }

  public KeyPair getTrustee() {
    return trustee;
  }

  public boolean isAuthorize() {
    return authorize;
  }

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      KeyPair issuer = KeyPair.fromAccountId(assetIssuer);
      return Asset.createNonNativeAsset(assetCode, issuer);
    }
  }
}
