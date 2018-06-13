package com.zafeplace.sdk.stellarsdk.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

public class AccountThresholdsUpdatedEffectResponse extends EffectResponse {
  @SerializedName("low_threshold")
  protected final Integer lowThreshold;
  @SerializedName("med_threshold")
  protected final Integer medThreshold;
  @SerializedName("high_threshold")
  protected final Integer highThreshold;

  AccountThresholdsUpdatedEffectResponse(Integer lowThreshold, Integer medThreshold, Integer highThreshold) {
    this.lowThreshold = lowThreshold;
    this.medThreshold = medThreshold;
    this.highThreshold = highThreshold;
  }

  public Integer getLowThreshold() {
    return lowThreshold;
  }

  public Integer getMedThreshold() {
    return medThreshold;
  }

  public Integer getHighThreshold() {
    return highThreshold;
  }
}
