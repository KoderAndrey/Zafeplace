package com.zafeplace.sdk.stellarsdk.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

import com.zafeplace.sdk.stellarsdk.sdk.Asset;
import com.zafeplace.sdk.stellarsdk.sdk.AssetTypeNative;
import com.zafeplace.sdk.stellarsdk.sdk.KeyPair;

/**
 * Represents account_debited effect response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see com.zafeplace.sdk.stellarsdk.sdk.requests.EffectsRequestBuilder
 * @see com.zafeplace.sdk.stellarsdk.sdk.Server#effects()
 */
public class AccountDebitedEffectResponse extends EffectResponse {
  @SerializedName("amount")
  protected final String amount;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;

  AccountDebitedEffectResponse(String amount, String assetType, String assetCode, String assetIssuer) {
    this.amount = amount;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
  }

  public String getAmount() {
    return amount;
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
