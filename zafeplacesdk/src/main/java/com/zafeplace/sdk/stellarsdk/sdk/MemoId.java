package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.MemoType;
import com.zafeplace.sdk.stellarsdk.sdk.xdr.Uint64;

/**
 * Represents MEMO_ID.
 */
public class MemoId extends Memo {
    private long id;

    public MemoId(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be a positive number");
        }
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo toXdr() {
        com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo memo = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo();
        memo.setDiscriminant(MemoType.MEMO_ID);
        Uint64 idXdr = new Uint64();
        idXdr.setUint64(id);
        memo.setId(idXdr);
        return memo;
    }
}
