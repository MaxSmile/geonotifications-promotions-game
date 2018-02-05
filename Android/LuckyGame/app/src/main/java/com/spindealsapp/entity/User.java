package com.spindealsapp.entity;

/**
 * Created by Kvm on 06.06.2017.
 */

public class User {

    private String id;
    private String token;
    private String name;
    private int type;

    public User() {
    }

    public User(String id, String token, String name, int type) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
