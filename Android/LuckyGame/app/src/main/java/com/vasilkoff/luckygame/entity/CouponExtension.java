package com.vasilkoff.luckygame.entity;

/**
 * Created by Kvm on 19.05.2017.
 */

public class CouponExtension {

    private int status;
    private String code;
    private String companyKey;
    private String promoKey;
    private String creator;
    private long creation;
    private long expired;
    private long locks;
    private String companyName;
    private String promoName;
    private String logo;
    private long type;
    private String lockDiff;
    private String expiredDiff;
    private int statusIcon;

    public CouponExtension() {
    }

    public CouponExtension(int status, String code, String companyKey, String promoKey, String creator, long creation, long expired, long locks, String companyName, String promoName, String logo, long type) {
        this.status = status;
        this.code = code;
        this.companyKey = companyKey;
        this.promoKey = promoKey;
        this.creator = creator;
        this.creation = creation;
        this.expired = expired;
        this.locks = locks;
        this.companyName = companyName;
        this.promoName = promoName;
        this.logo = logo;
        this.type = type;
    }

    public CouponExtension(int status, String code, String companyKey, String promoKey, String creator, long creation, long expired, long locks, String companyName, String promoName, String logo, long type, String lockDiff, String expiredDiff, int statusIcon) {
        this.status = status;
        this.code = code;
        this.companyKey = companyKey;
        this.promoKey = promoKey;
        this.creator = creator;
        this.creation = creation;
        this.expired = expired;
        this.locks = locks;
        this.companyName = companyName;
        this.promoName = promoName;
        this.logo = logo;
        this.type = type;
        this.lockDiff = lockDiff;
        this.expiredDiff = expiredDiff;
        this.statusIcon = statusIcon;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
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
}
