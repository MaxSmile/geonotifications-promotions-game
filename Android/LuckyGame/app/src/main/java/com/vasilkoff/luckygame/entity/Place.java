package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kusenko on 22.02.2017.
 */

public class Place implements Parcelable {

    private String address;
    private String name;
    private String nameCompany;
    private double lat;
    private double lon;

    public Place() {

    }

    public Place(String address, String name, String nameCompany, double lat, double lon) {
        this.address = address;
        this.name = name;
        this.nameCompany = nameCompany;
        this.lat = lat;
        this.lon = lon;
    }

    protected Place(Parcel in) {
        address = in.readString();
        name = in.readString();
        nameCompany = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(name);
        parcel.writeString(nameCompany);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }
}
