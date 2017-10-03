package com.spindealsapp.entity;

/**
 * Created by Volodymyr Kusenko on 15.09.2017.
 */

public class Count {
    private long companies;
    private long gifts;
    private long offers;
    private long places;
    private long spins;

    public Count() {
    }

    public long getCompanies() {
        return companies;
    }

    public long getGifts() {
        return gifts;
    }

    public long getOffers() {
        return offers;
    }

    public long getPlaces() {
        return places;
    }

    public long getSpins() {
        return spins;
    }

    public void setCompanies(long companies) {
        this.companies = companies;
    }

    public void setGifts(long gifts) {
        this.gifts = gifts;
    }

    public void setOffers(long offers) {
        this.offers = offers;
    }

    public void setPlaces(long places) {
        this.places = places;
    }

    public void setSpins(long spins) {
        this.spins = spins;
    }
}
