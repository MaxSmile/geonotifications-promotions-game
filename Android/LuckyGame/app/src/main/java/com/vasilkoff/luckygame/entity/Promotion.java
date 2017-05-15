package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Kusenko on 17.02.2017.
 */

public class Promotion implements Parcelable {

    private long dateStart;
    private long dateFinish;
    private String description;
    private String name;
    private boolean active;
    private List<String> listPlaces;
    private String imageUrl;
    private String contentUrl;
    private int colorBox;

    public Promotion() {

    }

    public Promotion(long dateStart, long dateFinish, String description, String name, boolean active, List<String> listPlaces, String imageUrl, String contentUrl, int colorBox) {
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.description = description;
        this.name = name;
        this.active = active;
        this.listPlaces = listPlaces;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
        this.colorBox = colorBox;
    }

    protected Promotion(Parcel in) {
        dateStart = in.readLong();
        dateFinish = in.readLong();
        description = in.readString();
        name = in.readString();
        active = in.readByte() != 0;
        listPlaces = in.createStringArrayList();
        imageUrl = in.readString();
        contentUrl = in.readString();
        colorBox = in.readInt();
    }

    public static final Creator<Promotion> CREATOR = new Creator<Promotion>() {
        @Override
        public Promotion createFromParcel(Parcel in) {
            return new Promotion(in);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getListPlaces() {
        return listPlaces;
    }

    public void setListPlaces(List<String> listPlaces) {
        this.listPlaces = listPlaces;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getColorBox() {
        return colorBox;
    }

    public void setColorBox(int colorBox) {
        this.colorBox = colorBox;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dateStart);
        dest.writeLong(dateFinish);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeStringList(listPlaces);
        dest.writeString(imageUrl);
        dest.writeString(contentUrl);
        dest.writeInt(colorBox);
    }
}
