package com.spindealsapp.entity;

import android.graphics.drawable.Drawable;
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
    private Drawable statusIcon;
    private String timeLeft;
    private String statusString;
    private String rrule;
    private long limit;
    private long spent;
    private boolean available = true;
    private boolean extraAvailable = true;

    public Spin() {
    }

    public Spin(String id, String companyKey, String placeKey, String rrule, long limit, long spent, boolean available, boolean extraAvailable) {
        this.id = id;
        this.companyKey = companyKey;
        this.placeKey = placeKey;
        this.rrule = rrule;
        this.limit = limit;
        this.spent = spent;
        this.available = available;
        this.extraAvailable = extraAvailable;
    }

    protected Spin(Parcel in) {
        id = in.readString();
        dateStart = in.readLong();
        dateFinish = in.readLong();
        companyKey = in.readString();
        placeKey = in.readString();
        status = in.readInt();
        timeLeft = in.readString();
        statusString = in.readString();
        rrule = in.readString();
        limit = in.readLong();
        spent = in.readLong();
        available = in.readByte() != 0;
        extraAvailable = in.readByte() != 0;
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

    public Drawable getStatusIcon() {
        return statusIcon;
    }

    public void setStatusIcon(Drawable statusIcon) {
        this.statusIcon = statusIcon;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getSpent() {
        return spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isExtraAvailable() {
        return extraAvailable;
    }

    public void setExtraAvailable(boolean extraAvailable) {
        this.extraAvailable = extraAvailable;
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
        dest.writeString(timeLeft);
        dest.writeString(statusString);
        dest.writeString(rrule);
        dest.writeLong(limit);
        dest.writeLong(spent);
        dest.writeByte((byte) (available ? 1 : 0));
        dest.writeByte((byte) (extraAvailable ? 1 : 0));
    }
}
