package com.vasilkoff.luckygame.entity;

/**
 * Created by Kvm on 08.06.2017.
 */

public class UsedSpin {
    private long time;
    private int type;
    private int result;

    public UsedSpin() {
    }

    public UsedSpin(long time, int type, int result) {
        this.time = time;
        this.type = type;
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
