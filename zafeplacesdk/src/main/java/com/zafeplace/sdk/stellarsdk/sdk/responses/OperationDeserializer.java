package com.zafeplace.sdk.stellarsdk.sdk.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.zafeplace.sdk.stellarsdk.sdk.KeyPair;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.ManageDataOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.OperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.CreateAccountOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.PaymentOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.PathPaymentOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.ManageOfferOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.CreatePassiveOfferOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.SetOptionsOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.ChangeTrustOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.AllowTrustOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.AccountMergeOperationResponse;
import com.zafeplace.sdk.stellarsdk.sdk.responses.operations.InflationOperationResponse;

import java.lang.reflect.Type;

class OperationDeserializer implements JsonDeserializer<OperationResponse> {
  @Override
  public OperationResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Create new Gson object with adapters needed in Operation
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(KeyPair.class, new KeyPairTypeAdapter().nullSafe())
            .create();

    int type = json.getAsJsonObject().get("type_i").getAsInt();
    switch (type) {
      case 0:
        return gson.fromJson(json, CreateAccountOperationResponse.class);
      case 1:
        return gson.fromJson(json, PaymentOperationResponse.class);
      case 2:
        return gson.fromJson(json, PathPaymentOperationResponse.class);
      case 3:
        return gson.fromJson(json, ManageOfferOperationResponse.class);
      case 4:
        return gson.fromJson(json, CreatePassiveOfferOperationResponse.class);
      case 5:
        return gson.fromJson(json, SetOptionsOperationResponse.class);
      case 6:
        return gson.fromJson(json, ChangeTrustOperationResponse.class);
      case 7:
        return gson.fromJson(json, AllowTrustOperationResponse.class);
      case 8:
        return gson.fromJson(json, AccountMergeOperationResponse.class);
      case 9:
        return gson.fromJson(json, InflationOperationResponse.class);
      case 10:
        return gson.fromJson(json, ManageDataOperationResponse.class);
      default:
        throw new RuntimeException("Invalid operation type");
    }
  }
}
