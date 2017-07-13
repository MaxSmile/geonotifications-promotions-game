package com.spindealsapp.entity;

/**
 * Created by Kvm on 19.05.2017.
 */

public class Coupon{

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
    private int locked;

    public Coupon() {
    }

    public Coupon(int status, String code, String companyKey, String giftKey, String placeKey, String description, String creator, long creation, long expired, long locks, int locked) {
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
        this.locked = locked;
    }

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

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }
}
