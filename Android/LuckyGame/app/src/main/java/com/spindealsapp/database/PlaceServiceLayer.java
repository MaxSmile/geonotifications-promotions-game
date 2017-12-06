package com.spindealsapp.database;

import android.content.res.TypedArray;

import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.Spin;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.util.DateUtils;
import com.spindealsapp.util.LocationDistance;
import com.spindealsapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kvm on 27.06.2017.
 */

public class PlaceServiceLayer {

    private static ArrayList<Place> placesList;
    private static int day;
    private static Calendar calendar = new GregorianCalendar();
    private static boolean busy;

    public static ArrayList<Place> getPlaces() {
        if (placesList == null || day != calendar.get(Calendar.DAY_OF_MONTH)) {
            placesList = new ArrayList<>();
            calculateData();
        }
        calculateTimeLeft();
        return placesList;
    }

    private static void calculateTimeLeft() {
        for (int i = 0; i < placesList.size(); i++) {
            Spin spin = placesList.get(i).getSpin();
            spin.setTimeLeft(DateFormat.getDiff(spin.getTimeEnd()));
        }
    }

    public static void calculateData() {
        if (!busy) {
            busy = true;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    placesList = getPlacesWithSpin();
                    Collections.sort(placesList, new PlaceComparator());
                    for (int i = 0; i < placesList.size(); i++) {
                        Place place = placesList.get(i);
                        updatePlace(place);
                    }
                    busy = false;
                    EventBus.getDefault().post(new Events.UpdatePlaces());
                    EventBus.getDefault().post(new Events.FinishCalculateData());
                }
            }).start();
        }
    }

    private static ArrayList<Place> getPlacesWithSpin() {
        ArrayList<Place> placesList = DBHelper.getInstance(App.getInstance()).getOrderPlaces();
        Map<String, Spin> spins = SpinServiceLayer.getSpins();
        for (Place place : placesList) {
            if (spins.get(place.getId()) != null) {
                place.setSpin(spins.get(place.getId()));
            } else {
                place.setSpin(SpinServiceLayer.getSpinComing());
            }
        }

        return placesList;
    }

    public static Place getPlace(String id) {
        Place place = DBHelper.getInstance(App.getInstance()).getPlace(id);
        Map<String, Spin> spins = SpinServiceLayer.getSpins();
        if (spins.get(place.getId()) != null) {
            place.setSpin(spins.get(place.getId()));
        } else {
            place.setSpin(SpinServiceLayer.getSpinComing());
        }
        updatePlace(place);
        return place;
    }

    private static void updatePlace(Place place) {
        if (CurrentLocation.lat != 0) {
            if (place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                        place.getGeoLat(), place.getGeoLon()));
                place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                        place.getGeoLat(), place.getGeoLon()));
            }
        }
        if (place.getInfo() == null || place.getInfo().isEmpty()) {
            place.setInfoChecked(true);
        }
    }

    private static class PlaceComparator implements Comparator<Place> {

        @Override
        public int compare(Place o1, Place o2) {
            String name1 = o1.getName().toLowerCase();
            String name2 = o2.getName().toLowerCase();
            return name1.compareTo(name2);
        }
    }

    public static void updatePlaces(ArrayList<Place> places) {
        HashMap<String, Place> oldPlaces = DBHelper.getInstance(App.getInstance()).getPlaces();
        TypedArray iconArray = App.getInstance().getResources().obtainTypedArray(R.array.company_type_icons);
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            Place oldPlace = oldPlaces.get(place.getId());
            updatePlaceBeforeSave(place, oldPlace, iconArray);
        }
        iconArray.recycle();
        if (DBHelper.getInstance(App.getInstance()).savePlaces(places)) {
            EventBus.getDefault().post(new Events.UpdatePlaces());
        }
    }

    public static void insertPlace(Place place) {
        Place oldPlace = DBHelper.getInstance(App.getInstance()).getPlace(place.getId());
        TypedArray iconArray = App.getInstance().getResources().obtainTypedArray(R.array.company_type_icons);
        updatePlaceBeforeSave(place, oldPlace, iconArray);
        iconArray.recycle();
        DBHelper.getInstance(App.getInstance()).insertPlace(place);
        EventBus.getDefault().post(new Events.UpdatePlaces());
    }

    private static void updatePlaceBeforeSave(Place place, Place oldPlace, TypedArray iconArray) {
        place.setTypeName(Constants.COMPANY_TYPE_NAMES[place.getType()]);
        place.setTypeIcon(iconArray.getResourceId(place.getType(), 0));
        if (CurrentLocation.lat != 0 && place.getGeoLat() != 0 && place.getGeoLon() != 0) {
            place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                    place.getGeoLat(), place.getGeoLon()));
            place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                    place.getGeoLat(), place.getGeoLon()));
        }
        if (oldPlace != null) {
            place.setFavorites(oldPlace.isFavorites());
            if (place.getInfoTimestamp() != oldPlace.getInfoTimestamp()) {
                place.setInfoChecked(false);
            } else {
                place.setInfoChecked(oldPlace.isInfoChecked());
            }
        }
    }

    public static boolean isBusy() {
        return busy;
    }
}
