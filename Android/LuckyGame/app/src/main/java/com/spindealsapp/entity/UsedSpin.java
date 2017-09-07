package com.spindealsapp.entity;

/**
 * Created by Kvm on 08.06.2017.
 */

public class UsedSpin {
    private long time;
    private int type;
    private int result;
    private String companyKey;
    private String placeKey;
    private String spinKey;
    private String userKey;

    public UsedSpin() {
    }

    public UsedSpin(long time, int type, int result) {
        this.time = time;
        this.type = type;
        this.result = result;
    }

    public UsedSpin(long time, int type, int result, String companyKey, String placeKey, String spinKey, String userKey) {
        this.time = time;
        this.type = type;
        this.result = result;
        this.companyKey = companyKey;
        this.placeKey = placeKey;
        this.spinKey = spinKey;
        this.userKey = userKey;
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

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getPlaceKey() {
        return placeKey;
    }

    public void setPlaceKey(String placeKey) {
        this.placeKey = placeKey;
    }

    public String getSpinKey() {
        return spinKey;
    }

    public void setSpinKey(String spinKey) {
        this.spinKey = spinKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
