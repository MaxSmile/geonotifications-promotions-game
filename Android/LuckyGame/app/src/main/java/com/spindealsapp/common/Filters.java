package com.spindealsapp.common;

import android.util.SparseBooleanArray;

import java.util.Map;

/**
 * Created by Kvm on 20.06.2017.
 */

public class Filters {
    public static final int FILTER_CODE = 100;

    public static boolean nearMe;
    public static boolean byCity;
    public static boolean byKeywords;
    public static boolean byZA;

    public static Map<String, String> filteredCities;
    public static Map<String, String> filteredKeywords;
    public static SparseBooleanArray checkedCitiesArray;
    public static SparseBooleanArray checkedFiltersArray;
    public static SparseBooleanArray checkedKeywordsArray;

    public static int count;

    public static void clear() {
        count = 0;
        byCity = false;
        byKeywords = false;
        filteredCities = null;
        filteredKeywords = null;
        checkedCitiesArray = null;
        checkedKeywordsArray = null;
    }
}
