package com.vasilkoff.luckygame.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kvm on 19.05.2017.
 */

public class CouponExtension implements Parcelable {

    private int status;
    private String code;
    private String companyKey;
    private String giftKey;
    private String placeKey;
    private String description;
    private String creator;
    private long creation;
    private long expired;
    private long locks;
    private long redeemed;
    private String redeemUser;
    private String companyName;
    private String placeName;
    private String logo;
    private long type;
    private String lockDiff;
    private String expiredDiff;
    private int statusIcon;
    private String typeString;
    private String distance;


    public CouponExtension() {
    }

    public CouponExtension(int status, String code, String companyKey, String giftKey, String placeKey, String description, String creator, long creation, long expired, long locks, String companyName, String placeName, String logo, long type, String typeString) {
        this.status = status;
        this.code = code;
        this.companyKey = companyKey;
        this.giftKey = giftKey;
        this.placeKey = placeKey;
        this.description = description;
        this.creator = creator;
        this.creation = creation;
        this.expired = expired;
        this.locks = locks;
        this.companyName = companyName;
        this.placeName = placeName;
        this.logo = logo;
        this.type = type;
        this.typeString = typeString;
    }

    protected CouponExtension(Parcel in) {
        status = in.readInt();
        code = in.readString();
        companyKey = in.readString();
        giftKey = in.readString();
        placeKey = in.readString();
        description = in.readString();
        creator = in.readString();
        creation = in.readLong();
        expired = in.readLong();
        locks = in.readLong();
        redeemed = in.readLong();
        redeemUser = in.readString();
        companyName = in.readString();
        placeName = in.readString();
        logo = in.readString();
        type = in.readLong();
        lockDiff = in.readString();
        expiredDiff = in.readString();
        statusIcon = in.readInt();
        typeString = in.readString();
        distance = in.readString();
    }

    public static final Creator<CouponExtension> CREATOR = new Creator<CouponExtension>() {
        @Override
        public CouponExtension createFromParcel(Parcel in) {
            return new CouponExtension(in);
        }

        @Override
        public CouponExtension[] newArray(int size) {
            return new CouponExtension[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getGiftKey() {
        return giftKey;
    }

    public void setGiftKey(String giftKey) {
        this.giftKey = giftKey;
    }

    public String getPlaceKey() {
        return placeKey;
    }

    public void setPlaceKey(String placeKey) {
        this.placeKey = placeKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public long getLocks() {
        return locks;
    }

    public void setLocks(long locks) {
        this.locks = locks;
    }

    public long getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(long redeemed) {
        this.redeemed = redeemed;
    }

    public String getRedeemUser() {
        return redeemUser;
    }

    public void setRedeemUser(String redeemUser) {
        this.redeemUser = redeemUser;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getLockDiff() {
        return lockDiff;
    }

    public void setLockDiff(String lockDiff) {
        this.lockDiff = lockDiff;
    }

    public String getExpiredDiff() {
        return expiredDiff;
    }

    public void setExpiredDiff(String expiredDiff) {
        this.expiredDiff = expiredDiff;
    }

    public int getStatusIcon() {
        return statusIcon;
    }

    public void setStatusIcon(int statusIcon) {
        this.statusIcon = statusIcon;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(code);
        dest.writeString(companyKey);
        dest.writeString(giftKey);
        dest.writeString(placeKey);
        dest.writeString(description);
        dest.writeString(creator);
        dest.writeLong(creation);
        dest.writeLong(expired);
        dest.writeLong(locks);
        dest.writeLong(redeemed);
        dest.writeString(redeemUser);
        dest.writeString(companyName);
        dest.writeString(placeName);
        dest.writeString(logo);
        dest.writeLong(type);
        dest.writeString(lockDiff);
        dest.writeString(expiredDiff);
        dest.writeInt(statusIcon);
        dest.writeString(typeString);
        dest.writeString(distance);
    }
}
