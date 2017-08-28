package com.spindealsapp.entity;

/**
 * Created by Volodymyr Kusenko on 28.08.2017.
 */

public class GiftLimit {

    private long date;
    private long value;

    public GiftLimit() {
    }

    public GiftLimit(long date, long value) {
        this.date = date;
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
