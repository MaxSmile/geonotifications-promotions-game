package com.spindealsapp.common;

import com.spindealsapp.entity.Place;

import java.util.Comparator;

/**
 * Created by Volodymyr Kusenko on 03.10.2017.
 */

public class PlaceDistanceComparator implements Comparator<Place> {

    @Override
    public int compare(Place o1, Place o2) {
        double distance1 = o1.getDistance();
        double distance2 = o2.getDistance();
        return Double.compare(distance1, distance2);
    }
}