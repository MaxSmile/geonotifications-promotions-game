package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kvm on 01.06.2017.
 */

public class Gift implements Parcelable {

    private String id;
    private long dateStart;
    private long dateFinish;
    private String companyKey;
    private String description;
    private long timeLock;
    private String rules;
    private long limitGifts;
    private long countAvailable;
    private boolean active;

    public Gift() {
    }

    public Gift(String id, long dateStart, long dateFinish, String companyKey, String description, long timeLock, String rules, long limitGifts, long countAvailable, boolean active) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.companyKey = companyKey;
        this.description = description;
        this.timeLock = timeLock;
        this.rules = rules;
        this.limitGifts = limitGifts;
        this.countAvailable = countAvailable;
        this.active = active;
    }

    protected Gift(Parcel in) {
        id = in.readString();
        dateStart = in.readLong();
        dateFinish = in.readLong();
        companyKey = in.readString();
        description = in.readString();
        timeLock = in.readLong();
        rules = in.readString();
        limitGifts = in.readLong();
        countAvailable = in.readLong();
        active = in.readByte() != 0;
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

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(long dateFinish) {
        this.dateFinish = dateFinish;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(dateStart);
        dest.writeLong(dateFinish);
        dest.writeString(companyKey);
        dest.writeString(description);
        dest.writeLong(timeLock);
        dest.writeString(rules);
        dest.writeLong(limitGifts);
        dest.writeLong(countAvailable);
        dest.writeByte((byte) (active ? 1 : 0));
    }
}
