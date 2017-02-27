package com.vasilkoff.luckygame.entity;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class Company {

    private String name;
    private String info;

    public Company(String name, String info) {
        this.name = name;
        this.info = info;
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
}
