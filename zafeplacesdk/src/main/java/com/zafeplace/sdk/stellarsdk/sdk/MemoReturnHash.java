package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo;
import com.zafeplace.sdk.stellarsdk.sdk.xdr.MemoType;

/**
 * Represents MEMO_RETURN.
 */
public class MemoReturnHash extends MemoHashAbstract {
  public MemoReturnHash(byte[] bytes) {
    super(bytes);
  }

  public MemoReturnHash(String hexString) {
    super(hexString);
  }

  @Override
  Memo toXdr() {
    com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo memo = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_RETURN);

    com.zafeplace.sdk.stellarsdk.sdk.xdr.Hash hash = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Hash();
    hash.setHash(bytes);

    memo.setHash(hash);
    return memo;
  }
}
