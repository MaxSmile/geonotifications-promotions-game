package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kvm on 31.05.2017.
 */

public class Spin implements Parcelable {
    private String id;
    private long dateStart;
    private long dateFinish;
    private String companyKey;
    private String placeKey;
    private int status;
    private int statusIcon;
    private String timeLeft;

    public Spin() {
    }

    public Spin(String id, long dateStart, long dateFinish, String companyKey, String placeKey, int status, int statusIcon, String timeLeft) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.companyKey = companyKey;
        this.placeKey = placeKey;
        this.status = status;
        this.statusIcon = statusIcon;
        this.timeLeft = timeLeft;
    }

    protected Spin(Parcel in) {
        id = in.readString();
        dateStart = in.readLong();
        dateFinish = in.readLong();
        companyKey = in.readString();
        placeKey = in.readString();
        status = in.readInt();
        statusIcon = in.readInt();
        timeLeft = in.readString();
    }

    public static final Creator<Spin> CREATOR = new Creator<Spin>() {
        @Override
        public Spin createFromParcel(Parcel in) {
            return new Spin(in);
        }

        @Override
        public Spin[] newArray(int size) {
            return new Spin[size];
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

    public String getPlaceKey() {
        return placeKey;
    }

    public void setPlaceKey(String placeKey) {
        this.placeKey = placeKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatusIcon() {
        return statusIcon;
    }

    public void setStatusIcon(int statusIcon) {
        this.statusIcon = statusIcon;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
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
        dest.writeString(placeKey);
        dest.writeInt(status);
        dest.writeInt(statusIcon);
        dest.writeString(timeLeft);
    }
}