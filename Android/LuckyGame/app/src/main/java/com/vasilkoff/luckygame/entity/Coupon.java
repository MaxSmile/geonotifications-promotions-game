package com.vasilkoff.luckygame.entity;

/**
 * Created by Kusenko on 20.02.2017.
 */

public class Coupon {

    private boolean active;
    private String name;
    private String description;
    private String dateExpire;
    private String code;

    public Coupon(boolean active, String name, String description, String dateExpire, String code) {
        this.active = active;
        this.name = name;
        this.description = description;
        this.dateExpire = dateExpire;
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(String dateExpire) {
        this.dateExpire = dateExpire;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
