package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.OperationType;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html#inflation" target="_blank">Inflation</a> operation.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">List of Operations</a>
 */
public class InflationOperation extends Operation {
    @Override
    com.zafeplace.sdk.stellarsdk.sdk.xdr.Operation.OperationBody toOperationBody() {
        com.zafeplace.sdk.stellarsdk.sdk.xdr.Operation.OperationBody body = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Operation.OperationBody();
        body.setDiscriminant(OperationType.INFLATION);
        return body;
    }
}
