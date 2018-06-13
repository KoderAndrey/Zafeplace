package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.MemoType;

import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents MEMO_TEXT.
 */
public class MemoText extends Memo {
    private String text;

    public MemoText(String text) {
        this.text = checkNotNull(text, "text cannot be null");

        int length = text.getBytes((Charset.forName("UTF-8"))).length;
        if (length > 28) {
            throw new MemoTooLongException("text must be <= 28 bytes. length=" + String.valueOf(length));
        }
    }

    public String getText() {
        return text;
    }

    @Override
    com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo toXdr() {
        com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo memo = new com.zafeplace.sdk.stellarsdk.sdk.xdr.Memo();
        memo.setDiscriminant(MemoType.MEMO_TEXT);
        memo.setText(text);
        return memo;
    }
}
