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
    private double geoLat;
    private double geoLon;
    private int geoRadius;
    private String geoMessage;
    private long geoTimeStart;
    private long geoTimeFinish;
    private long geoTimeFrequency;
    private List<Box> box;
    private int type;
    private String typeName;
    private int typeIcon;
    private String info;
    private String url;
    private List<String> gallery;
    private String tel;
    private String about;
    private String aboutMore;
    private double distance;
    private String distanceString;

    public Place() {

    }

    public Place(String id, String address, String name, String companyKey, double geoLat, double geoLon, int geoRadius, String geoMessage, long geoTimeStart, long geoTimeFinish, long geoTimeFrequency) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.companyKey = companyKey;
        this.geoLat = geoLat;
        this.geoLon = geoLon;
        this.geoRadius = geoRadius;
        this.geoMessage = geoMessage;
        this.geoTimeStart = geoTimeStart;
        this.geoTimeFinish = geoTimeFinish;
        this.geoTimeFrequency = geoTimeFrequency;
    }

    protected Place(Parcel in) {
        id = in.readString();
        address = in.readString();
        name = in.readString();
        companyKey = in.readString();
        geoLat = in.readDouble();
        geoLon = in.readDouble();
        geoRadius = in.readInt();
        geoMessage = in.readString();
        geoTimeStart = in.readLong();
        geoTimeFinish = in.readLong();
        geoTimeFrequency = in.readLong();
        box = in.createTypedArrayList(Box.CREATOR);
        type = in.readInt();
        typeName = in.readString();
        typeIcon = in.readInt();
        info = in.readString();
        url = in.readString();
        gallery = in.createStringArrayList();
        tel = in.readString();
        about = in.readString();
        aboutMore = in.readString();
        distance = in.readDouble();
        distanceString = in.readString();
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

    public double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(double geoLat) {
        this.geoLat = geoLat;
    }

    public double getGeoLon() {
        return geoLon;
    }

    public void setGeoLon(double geoLon) {
        this.geoLon = geoLon;
    }

    public int getGeoRadius() {
        return geoRadius;
    }

    public void setGeoRadius(int geoRadius) {
        this.geoRadius = geoRadius;
    }

    public String getGeoMessage() {
        return geoMessage;
    }

    public void setGeoMessage(String geoMessage) {
        this.geoMessage = geoMessage;
    }

    public long getGeoTimeStart() {
        return geoTimeStart;
    }

    public void setGeoTimeStart(long geoTimeStart) {
        this.geoTimeStart = geoTimeStart;
    }

    public long getGeoTimeFinish() {
        return geoTimeFinish;
    }

    public void setGeoTimeFinish(long geoTimeFinish) {
        this.geoTimeFinish = geoTimeFinish;
    }

    public long getGeoTimeFrequency() {
        return geoTimeFrequency;
    }

    public void setGeoTimeFrequency(long geoTimeFrequency) {
        this.geoTimeFrequency = geoTimeFrequency;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAboutMore() {
        return aboutMore;
    }

    public void setAboutMore(String aboutMore) {
        this.aboutMore = aboutMore;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistanceString() {
        return distanceString;
    }

    public void setDistanceString(String distanceString) {
        this.distanceString = distanceString;
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
        dest.writeDouble(geoLat);
        dest.writeDouble(geoLon);
        dest.writeInt(geoRadius);
        dest.writeString(geoMessage);
        dest.writeLong(geoTimeStart);
        dest.writeLong(geoTimeFinish);
        dest.writeLong(geoTimeFrequency);
        dest.writeTypedList(box);
        dest.writeInt(type);
        dest.writeString(typeName);
        dest.writeInt(typeIcon);
        dest.writeString(info);
        dest.writeString(url);
        dest.writeStringList(gallery);
        dest.writeString(tel);
        dest.writeString(about);
        dest.writeString(aboutMore);
        dest.writeDouble(distance);
        dest.writeString(distanceString);
    }
}
