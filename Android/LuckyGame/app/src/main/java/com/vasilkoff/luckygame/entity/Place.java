package com.vasilkoff.luckygame.entity;

/**
 * Created by Kusenko on 22.02.2017.
 */

public class Place {

    private String address;
    private String name;
    private String nameCompany;
    private double lat;
    private double lon;

    public Place() {

    }

    public Place(String address, String name, String nameCompany, double lat, double lon) {
        this.address = address;
        this.name = name;
        this.nameCompany = nameCompany;
        this.lat = lat;
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
