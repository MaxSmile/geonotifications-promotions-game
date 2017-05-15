package com.vasilkoff.luckygame.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Kusenko on 27.02.2017.
 */

public class Company {

    private String id;
    private String name;
    private String info;
    private String logo;
    private String typeName;
    private int type;
    private int icon;

    public Company(String id, String name, String info, String logo) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.logo = logo;
    }

    public Company(String id, String name, String info, String logo, String typeName, int type, int icon) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.logo = logo;
        this.typeName = typeName;
        this.type = type;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }
}
