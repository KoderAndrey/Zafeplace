package com.zafeplace.sdk.stellarsdk.sdk.responses.effects;

import com.zafeplace.sdk.stellarsdk.sdk.KeyPair;

/**
 * Represents trustline_authorized effect response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see com.zafeplace.sdk.stellarsdk.sdk.requests.EffectsRequestBuilder
 * @see com.zafeplace.sdk.stellarsdk.sdk.Server#effects()
 */
public class TrustlineAuthorizedEffectResponse extends TrustlineAuthorizationResponse {
  TrustlineAuthorizedEffectResponse(KeyPair trustor, String assetType, String assetCode) {
    super(trustor, assetType, assetCode);
  }
}
