package com.vasilkoff.luckygame.entity;

/**
 * Created by Kvm on 01.06.2017.
 */

public class Gift {

    private String id;
    private long dateStart;
    private long dateFinish;
    private String companyKey;
    private String description;
    private long timeLock;

    public Gift() {
    }

    public Gift(String id, long dateStart, long dateFinish, String companyKey, String description, long timeLock) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.companyKey = companyKey;
        this.description = description;
        this.timeLock = timeLock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeLock() {
        return timeLock;
    }

    public void setTimeLock(long timeLock) {
        this.timeLock = timeLock;
    }
}
