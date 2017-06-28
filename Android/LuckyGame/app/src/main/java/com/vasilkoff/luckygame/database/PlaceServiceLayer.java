package com.vasilkoff.luckygame.database;

import android.content.res.TypedArray;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vasilkoff.luckygame.App;
import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.CurrentLocation;
import com.vasilkoff.luckygame.CurrentUser;
import com.vasilkoff.luckygame.R;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.UsedSpin;
import com.vasilkoff.luckygame.util.DateFormat;
import com.vasilkoff.luckygame.util.LocationDistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kvm on 27.06.2017.
 */

public class PlaceServiceLayer {

    public  static ArrayList<Place> getPlaces() {
        ArrayList<Place> placesList = DBHelper.getInstance(App.getInstance()).getOrderPlaces();
        Collections.sort(placesList, new PlaceComparator());
        TypedArray spinIcon = App.getInstance().getResources().obtainTypedArray(R.array.spin_type_icon);
        String[] spinType = App.getInstance().getResources().getStringArray(R.array.spin_type);
        for (int i = 0; i < placesList.size(); i++) {
            Place place = placesList.get(i);
            if (CurrentLocation.lat != 0) {
                if (place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                    place.setDistanceString(LocationDistance.getDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                    place.setDistance(LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                            place.getGeoLat(), place.getGeoLon()));
                }
            }

            if (place.getSpinFinish() >= System.currentTimeMillis()) {
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
        spinIcon.recycle();

       return placesList;
    }



    private static class PlaceComparator implements Comparator<Place> {

        @Override
        public int compare(Place o1, Place o2) {
            String name1 = o1.getName().toLowerCase();
            String name2 = o2.getName().toLowerCase();
            return name1.compareTo(name2);
        }
    }
}
