package com.vasilkoff.luckygame.entity;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class Company {

    private String name;
    private String info;
    private String logo;

    public Company(String name, String info, String logo) {
        this.name = name;
        this.info = info;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
