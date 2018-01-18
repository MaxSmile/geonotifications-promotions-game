package com.spindealsapp.entity;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class Gallery {

    private String owner;
    private String path;

    public Gallery(String owner, String path) {
        this.owner = owner;
        this.path = path;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
