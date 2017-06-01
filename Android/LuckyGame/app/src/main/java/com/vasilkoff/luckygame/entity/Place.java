package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Kusenko on 22.02.2017.
 */

public class Place implements Parcelable {

    private String id;
    private String address;
    private String name;
    private String companyKey;
    private double lat;
    private double lon;
    private List<Box> box;
    private int type;
    private String typeName;
    private int typeIcon;
    private String info;

    public Place() {

    }

    public Place(String id, String address, String name, String companyKey, double lat, double lon, List<Box> box, int type, String typeName, int typeIcon, String info) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.companyKey = companyKey;
        this.lat = lat;
        this.lon = lon;
        this.box = box;
        this.type = type;
        this.typeName = typeName;
        this.typeIcon = typeIcon;
        this.info = info;
    }

    protected Place(Parcel in) {
        id = in.readString();
        address = in.readString();
        name = in.readString();
        companyKey = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        box = in.createTypedArrayList(Box.CREATOR);
        type = in.readInt();
        typeName = in.readString();
        typeIcon = in.readInt();
        info = in.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
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

    public List<Box> getBox() {
        return box;
    }

    public void setBox(List<Box> box) {
        this.box = box;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(int typeIcon) {
        this.typeIcon = typeIcon;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(companyKey);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeTypedList(box);
        dest.writeInt(type);
        dest.writeString(typeName);
        dest.writeInt(typeIcon);
        dest.writeString(info);
    }
}
