package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class Company implements Parcelable {

    private String id;
    private String name;
    private String info;
    private String logo;
    private String facebookUrl;
    private int type;
    private long limitGifts;

    public Company() {
    }

    protected Company(Parcel in) {
        id = in.readString();
        name = in.readString();
        info = in.readString();
        logo = in.readString();
        facebookUrl = in.readString();
        type = in.readInt();
        limitGifts = in.readLong();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getLimitGifts() {
        return limitGifts;
    }

    public void setLimitGifts(long limitGifts) {
        this.limitGifts = limitGifts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(info);
        dest.writeString(logo);
        dest.writeString(facebookUrl);
        dest.writeInt(type);
        dest.writeLong(limitGifts);
    }
}
