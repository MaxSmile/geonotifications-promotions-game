package com.spindealsapp.database;

import android.content.res.TypedArray;

import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.entity.Place;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.util.LocationDistance;
import com.spindealsapp.R;
import com.spindealsapp.util.Rrule;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Kvm on 27.06.2017.
 */

public class PlaceServiceLayer {

    public static ArrayList<Place> getPlaces() {
        ArrayList<Place> placesList = DBHelper.getInstance(App.getInstance()).getOrderPlaces();
        Collections.sort(placesList, new PlaceComparator());
        TypedArray spinIcon = App.getInstance().getResources().obtainTypedArray(R.array.spin_type_icon);
        String[] spinType = App.getInstance().getResources().getStringArray(R.array.spin_type);
        for (int i = 0; i < placesList.size(); i++) {
            Place place = placesList.get(i);
            updatePlace(place, spinIcon, spinType);
        }
        spinIcon.recycle();

       return placesList;
    }

    public static Place getPlace(String id) {
        Place place = DBHelper.getInstance(App.getInstance()).getPlace(id);
        TypedArray spinIcon = App.getInstance().getResources().obtainTypedArray(R.array.spin_type_icon);
        String[] spinType = App.getInstance().getResources().getStringArray(R.array.spin_type);

        updatePlace(place, spinIcon, spinType);

        spinIcon.recycle();
        return place;
    }

    private static void updatePlace(Place place, TypedArray spinIcon, String[] spinType) {
        if (CurrentLocation.lat != 0) {
            if (place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                        place.getGeoLat(), place.getGeoLon()));
                place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                        place.getGeoLat(), place.getGeoLon()));
            }
        }

        if (place.getRrule() != null && Rrule.isAvailable(place.getRrule())) {
            if (place.isSpinAvailable()) {
                place.setSpinStatus(Constants.SPIN_STATUS_ACTIVE);
            } else {
                place.setSpinStatus(Constants.SPIN_STATUS_EXTRA_AVAILABLE);
            }
        } else {
            place.setSpinStatus(Constants.SPIN_STATUS_COMING);
        }

        place.setSpinStatusIcon(spinIcon.getDrawable(place.getSpinStatus()));
        place.setSpinStatusString(spinType[place.getSpinStatus()]);
        place.setSpinTimeLeft(DateFormat.getDiff(place.getSpinFinish()));
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
            place.setTypeName(Constants.COMPANY_TYPE_NAMES[place.getType()]);
            place.setTypeIcon(iconArray.getResourceId(place.getType(), 0));
            if (CurrentLocation.lat != 0 && place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                        place.getGeoLat(), place.getGeoLon()));
                place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                        place.getGeoLat(), place.getGeoLon()));
            }

            Place oldPlace = oldPlaces.get(place.getId());
            if (oldPlace != null) {
                place.setFavorites(oldPlace.isFavorites());
                if (place.getInfoTimestamp() != oldPlace.getInfoTimestamp()) {
                    place.setInfoChecked(false);
                } else {
                    place.setInfoChecked(oldPlace.isInfoChecked());
                }
            }

        }
        iconArray.recycle();
        if (DBHelper.getInstance(App.getInstance()).savePlaces(places)) {
            EventBus.getDefault().post(new Events.UpdatePlaces());
        }
    }
}
