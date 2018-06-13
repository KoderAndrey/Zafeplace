package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.MemoType;

/**
 * Represents MEMO_HASH.
 */
public class MemoHash extends MemoHashAbstract {
  public MemoHash(byte[] bytes) {
    super(bytes);
  }

  public MemoHash(String hexString) {
    super(hexString);
  }

  @Override
  com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo toXdr() {
    com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo memo = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_HASH);

    com.zafeplace.sdk.stellarsdk.sdk.xdr.Hash hash = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Hash();
    hash.setHash(bytes);

    memo.setHash(hash);
    return memo;
  }
}
