package com.vasilkoff.luckygame.entity;

import java.util.List;

/**
 * Created by Kusenko on 17.02.2017.
 */

public class Promotion {

    private long dateStart;
    private long dateFinish;
    private String description;
    private String name;
    private boolean active;
    private List<String> listPlaces;
    private String imageUrl;
    private String contentUrl;

    public Promotion() {

    }

    public Promotion(long dateStart, long dateFinish, String description, String name, boolean active, List<String> listPlaces, String imageUrl, String contentUrl) {
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.description = description;
        this.name = name;
        this.active = active;
        this.listPlaces = listPlaces;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(long dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getListPlaces() {
        return listPlaces;
    }

    public void setListPlaces(List<String> listPlaces) {
        this.listPlaces = listPlaces;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
