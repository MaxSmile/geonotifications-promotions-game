package com.spindealsapp.entity;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kvm on 31.05.2017.
 */

public class Spin implements Parcelable {
    private String id;
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
    private long extraCreateTime;
    private boolean extra;
    private List<Box> box = new ArrayList<Box>();

    public Spin() {
    }

    public Spin(String id, String companyKey, String placeKey, String rrule, long limit, long spent, boolean available, boolean extraAvailable, long extraCreateTime, boolean extra) {
        this.id = id;
        this.companyKey = companyKey;
        this.placeKey = placeKey;
        this.rrule = rrule;
        this.limit = limit;
        this.spent = spent;
        this.available = available;
        this.extraAvailable = extraAvailable;
        this.extraCreateTime = extraCreateTime;
        this.extra = extra;
    }

    protected Spin(Parcel in) {
        id = in.readString();
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
        extraCreateTime = in.readLong();
        extra = in.readByte() != 0;
        box = in.createTypedArrayList(Box.CREATOR);
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

    public long getExtraCreateTime() {
        return extraCreateTime;
    }

    public void setExtraCreateTime(long extraCreateTime) {
        this.extraCreateTime = extraCreateTime;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public List<Box> getBox() {
        return box;
    }

    public void setBox(List<Box> box) {
        this.box = box;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
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
        dest.writeLong(extraCreateTime);
        dest.writeByte((byte) (extra ? 1 : 0));
        dest.writeTypedList(box);
    }
}
