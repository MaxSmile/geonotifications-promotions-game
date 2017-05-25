package com.vasilkoff.luckygame.entity;

/**
 * Created by Kvm on 19.05.2017.
 */

public class Coupon {

    private int status;
    private String code;
    private String companyKey;
    private String promoKey;
    private String creator;
    private long creation;
    private long expired;
    private long locks;
    private long redeemed;

    public Coupon() {
    }

    public Coupon(int status, String code, String company, String promo, String creator, long creations, long expired, long locks) {
        this.status = status;
        this.code = code;
        this.companyKey = company;
        this.promoKey = promo;
        this.creator = creator;
        this.creation = creations;
        this.expired = expired;
        this.locks = locks;
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

    public String getPromoKey() {
        return promoKey;
    }

    public void setPromoKey(String promoKey) {
        this.promoKey = promoKey;
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
}
