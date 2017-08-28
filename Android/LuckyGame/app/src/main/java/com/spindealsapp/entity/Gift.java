package com.spindealsapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kvm on 01.06.2017.
 */

public class Gift implements Parcelable {

    private String id;
    private String companyKey;
    private String description;
    private long timeLock;
    private String rules;
    private long limitGifts;
    private long countAvailable;
    private String spinKey;
    private long expirationTime;
    private boolean active;

    public Gift() {
    }

    public Gift(String id, String companyKey, String description, long timeLock, String rules, long limitGifts, long countAvailable, String spinKey, long expirationTime) {
        this.id = id;
        this.companyKey = companyKey;
        this.description = description;
        this.timeLock = timeLock;
        this.rules = rules;
        this.limitGifts = limitGifts;
        this.countAvailable = countAvailable;
        this.spinKey = spinKey;
        this.expirationTime = expirationTime;
    }

    protected Gift(Parcel in) {
        id = in.readString();
        companyKey = in.readString();
        description = in.readString();
        timeLock = in.readLong();
        rules = in.readString();
        limitGifts = in.readLong();
        countAvailable = in.readLong();
        active = in.readByte() != 0;
        spinKey = in.readString();
        expirationTime = in.readLong();
    }

    public static final Creator<Gift> CREATOR = new Creator<Gift>() {
        @Override
        public Gift createFromParcel(Parcel in) {
            return new Gift(in);
        }

        @Override
        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeLock() {
        return timeLock;
    }

    public void setTimeLock(long timeLock) {
        this.timeLock = timeLock;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public long getLimitGifts() {
        return limitGifts;
    }

    public void setLimitGifts(long limitGifts) {
        this.limitGifts = limitGifts;
    }

    public long getCountAvailable() {
        return countAvailable;
    }

    public void setCountAvailable(long countAvailable) {
        this.countAvailable = countAvailable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSpinKey() {
        return spinKey;
    }

    public void setSpinKey(String spinKey) {
        this.spinKey = spinKey;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(companyKey);
        dest.writeString(description);
        dest.writeLong(timeLock);
        dest.writeString(rules);
        dest.writeLong(limitGifts);
        dest.writeLong(countAvailable);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(spinKey);
        dest.writeLong(expirationTime);
    }
}
