package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kusenko on 20.02.2017.
 */

public class Coupon implements Parcelable {

    private boolean active;
    private String name;
    private String description;
    private String dateExpire;
    private String code;
    private String company;
    private String userId;

    public Coupon(boolean active, String name, String description, String dateExpire, String code, String company, String userId) {
        this.active = active;
        this.name = name;
        this.description = description;
        this.dateExpire = dateExpire;
        this.code = code;
        this.company = company;
        this.userId = userId;
    }

    protected Coupon(Parcel in) {
        active = in.readByte() != 0;
        name = in.readString();
        description = in.readString();
        dateExpire = in.readString();
        code = in.readString();
        company = in.readString();
        userId = in.readString();
    }

    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(String dateExpire) {
        this.dateExpire = dateExpire;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (active ? 1 : 0));
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(dateExpire);
        parcel.writeString(code);
        parcel.writeString(company);
        parcel.writeString(userId);
    }
}
