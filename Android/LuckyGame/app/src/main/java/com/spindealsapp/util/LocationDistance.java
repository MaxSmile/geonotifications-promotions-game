package com.spindealsapp.util;

/**
 * Created by Kvm on 16.06.2017.
 */

public class LocationDistance {

    public static String getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double calcDistance = calculateDistance(latitude1, longitude1, latitude2, longitude2);
        if (calcDistance >= 1000) {
            return String.format("%.2f", calcDistance/1000) + "km";
        } else {
            return (int)calcDistance + "m";
        }
    }

    public static String getStringDistance(double distance) {
        if (distance >= 1000) {
            return String.format("%.2f", distance/1000) + "km";
        } else {
            return (int)distance + "m";
        }
    }

    /**
     * Uses the Haversine formula to calculate the distnace between to lat-long coordinates
     *
     * @param latitude1  The first point's latitude
     * @param longitude1 The first point's longitude
     * @param latitude2  The second point's latitude
     * @param longitude2 The second point's longitude
     * @return The distance between the two points in meters
     */
    public static double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        /*
            Haversine formula:
            A = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
            C = 2.atan2(√a, √(1−a))
            D = R.c
            R = radius of earth, 6371 km.
            All angles are in radians
            */

        double deltaLatitude = Math.toRadians(Math.abs(latitude1 - latitude2));
        double deltaLongitude = Math.toRadians(Math.abs(longitude1 - longitude2));
        double latitude1Rad = Math.toRadians(latitude1);
        double latitude2Rad = Math.toRadians(latitude2);

        double a = Math.pow(Math.sin(deltaLatitude / 2), 2) +
                (Math.cos(latitude1Rad) * Math.cos(latitude2Rad) * Math.pow(Math.sin(deltaLongitude / 2), 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c * 1000; //Distance in meters

    }
}
