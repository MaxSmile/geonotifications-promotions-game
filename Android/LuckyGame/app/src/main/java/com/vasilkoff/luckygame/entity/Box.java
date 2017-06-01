package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kvm on 01.06.2017.
 */

public class Box implements Parcelable {

    private int color;
    private int count;
    private String gift;

    public Box() {
    }

    public Box(int color, int count, String gift) {
        this.color = color;
        this.count = count;
        this.gift = gift;
    }

    protected Box(Parcel in) {
        color = in.readInt();
        count = in.readInt();
        gift = in.readString();
    }

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(color);
        dest.writeInt(count);
        dest.writeString(gift);
    }
}
