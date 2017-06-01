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
    private String typeName;
    private int type;
    private int icon;
    private int countPromo;
    private String distance;
    private int countCoupon;

    public Company() {
    }

    public Company(String id, String name, String info, String logo) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.logo = logo;
    }

    public Company(String id, String name, String info, String logo, String typeName, int type, int icon, int countPromo) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.logo = logo;
        this.typeName = typeName;
        this.type = type;
        this.icon = icon;
        this.countPromo = countPromo;
    }

    public Company(String id, String name, String info, String logo, String typeName, int type, int icon, int countPromo, String distance, int countCoupon) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.logo = logo;
        this.typeName = typeName;
        this.type = type;
        this.icon = icon;
        this.countPromo = countPromo;
        this.distance = distance;
        this.countCoupon = countCoupon;
    }

    protected Company(Parcel in) {
        id = in.readString();
        name = in.readString();
        info = in.readString();
        logo = in.readString();
        typeName = in.readString();
        type = in.readInt();
        icon = in.readInt();
        countPromo = in.readInt();
        distance = in.readString();
        countCoupon = in.readInt();
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getCountPromo() {
        return countPromo;
    }

    public void setCountPromo(int countPromo) {
        this.countPromo = countPromo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getCountCoupon() {
        return countCoupon;
    }

    public void setCountCoupon(int countCoupon) {
        this.countCoupon = countCoupon;
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
        dest.writeString(typeName);
        dest.writeInt(type);
        dest.writeInt(icon);
        dest.writeInt(countPromo);
        dest.writeString(distance);
        dest.writeInt(countCoupon);
    }
}
