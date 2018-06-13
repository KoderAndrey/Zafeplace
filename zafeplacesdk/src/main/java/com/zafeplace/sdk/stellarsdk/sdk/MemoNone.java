package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.MemoType;

/**
 * Represents MEMO_NONE.
 */
public class MemoNone extends Memo {
  @Override
  com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo toXdr() {
    com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo memo = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_NONE);
    return memo;
  }
}
