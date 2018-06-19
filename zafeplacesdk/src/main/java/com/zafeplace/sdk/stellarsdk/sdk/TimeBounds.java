package com.zafeplace.sdk.stellarsdk.sdk;

import com.zafeplace.sdk.stellarsdk.sdk.xdr.Uint64;

/**
 * <p>TimeBounds represents the time interval that a transaction is valid.</p>
 *
 * @see Transaction
 */
final public class TimeBounds {
    final private long mMinTime;
    final private long mMaxTime;

    /**
     * @param minTime 64bit Unix timestamp
     * @param maxTime 64bit Unix timestamp
     */
    public TimeBounds(long minTime, long maxTime) {
        if (minTime < 0 || maxTime < 0) {
            throw new IllegalArgumentException("minTime and maxTime cannot be negative");
        } else if (maxTime != 0 && minTime >= maxTime) {
            throw new IllegalArgumentException("minTime must be < maxTime");
        }
        mMinTime = minTime;
        mMaxTime = maxTime;
    }

    public long getMinTime() {
        return mMinTime;
    }

    public long getMaxTime() {
        return mMaxTime;
    }

    public com.zafeplace.sdk.stellarsdk.sdk.xdr.TimeBounds toXdr() {
        com.zafeplace.sdk.stellarsdk.sdk.xdr.TimeBounds timeBounds = new com.zafeplace.sdk.stellarsdk.sdk.xdr.TimeBounds();
        Uint64 minTime = new Uint64();
        Uint64 maxTime = new Uint64();
        minTime.setUint64(mMinTime);
        maxTime.setUint64(mMaxTime);
        timeBounds.setMinTime(minTime);
        timeBounds.setMaxTime(maxTime);
        return timeBounds;
    }

    public static TimeBounds fromXdr(com.zafeplace.sdk.stellarsdk.sdk.xdr.TimeBounds xdrTimeBounds) {

        return new TimeBounds(xdrTimeBounds.getMinTime().getUint64(),
                +xdrTimeBounds.getMaxTime().getUint64());
    }
}
