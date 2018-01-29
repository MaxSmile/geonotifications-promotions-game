package com.spindealsapp.entity;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class PlaceNotification {

    private String placeId;
    private long lastNotification;

    public PlaceNotification(String placeId, long lastNotification) {
        this.placeId = placeId;
        this.lastNotification = lastNotification;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public long getLastNotification() {
        return lastNotification;
    }

    public void setLastNotification(long lastNotification) {
        this.lastNotification = lastNotification;
    }
}
