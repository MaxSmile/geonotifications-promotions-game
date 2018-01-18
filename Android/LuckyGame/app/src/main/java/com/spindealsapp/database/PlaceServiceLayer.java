package com.spindealsapp.database;

import android.content.res.TypedArray;

import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.CurrentLocation;
import com.spindealsapp.database.repository.PlaceSqlRepository;
import com.spindealsapp.database.repository.specification.GetCitySpecification;
import com.spindealsapp.database.repository.specification.OrderPlacesSpecification;
import com.spindealsapp.database.repository.specification.OtherPlacesCompanySqlSpecification;
import com.spindealsapp.database.repository.specification.PlaceByIdSqlSpecification;
import com.spindealsapp.database.repository.specification.PlacesSqlSpecification;
import com.spindealsapp.database.service.GalleryServiceLayer;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.Spin;
import com.spindealsapp.eventbus.Events;
import com.spindealsapp.util.DateFormat;
import com.spindealsapp.util.LocationDistance;
import com.spindealsapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kvm on 27.06.2017.
 */

public class PlaceServiceLayer {

    private static PlaceSqlRepository repository = new PlaceSqlRepository();
    private static Map<String, Place> placesList = new HashMap<>();
    private static int day;

    public static Map<String, Place> getPlaces() {
        if (placesList.size() == 0 || day != new GregorianCalendar().get(Calendar.DAY_OF_MONTH)) {
            calculateData();
        }
        calculateTimeEndDistance();
        return placesList;
    }

    private static void calculateTimeEndDistance() {
        for (Map.Entry<String, Place> item : placesList.entrySet()) {
            updateExtendInfo(item.getValue());
        }
    }

    public static void calculateData() {
        day = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
        new Thread(new Runnable() {
            @Override
            public void run() {
                calculate();
            }
        }).start();
    }

    private static synchronized void calculate() {
        Map<String, Place> placesNew = new HashMap<>();
        List<Place> places = getPlacesWithSpin();
        Collections.sort(places, new PlaceComparator());
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            placesNew.put(place.getId(), place);
        }
        placesList = placesNew;
        EventBus.getDefault().post(new Events.UpdatePlaces());
        EventBus.getDefault().post(new Events.FinishCalculateData());
    }

    private static List<Place> getPlacesWithSpin() {
        List<Place> placesList = repository.query(new OrderPlacesSpecification());
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
        Place place = placesList.get(id);
        if (place != null) {
            if (place.getGallery() == null) {
                place.setGallery(GalleryServiceLayer.getGallery(place.getId()));
            }
            updateExtendInfo(place);
        }
        return place;
    }

    private static void updateExtendInfo(Place place) {
        if (CurrentLocation.lat != 0) {
            if (place.getGeoLat() != 0 && place.getGeoLon() != 0) {
                double distance = LocationDistance.calculateDistance(CurrentLocation.lat, CurrentLocation.lon,
                        place.getGeoLat(), place.getGeoLon());
                place.setDistanceString(LocationDistance.getStringDistance(distance));
                place.setDistance(distance);
            }
        }
        if (place.getInfo() == null || place.getInfo().isEmpty()) {
            place.setInfoChecked(true);
        }

        Spin spin = place.getSpin();
        spin.setTimeLeft(DateFormat.getDiff(spin.getTimeEnd()));
    }



    public static void add(ArrayList<Place> places) {
        HashMap<String, Place> oldPlaces = new HashMap<>();
        List<Place> placeList = repository.query(new PlacesSqlSpecification());
        for (Place place: placeList) {
            oldPlaces.put(place.getId(), place);
        }
        TypedArray iconArray = App.getInstance().getResources().obtainTypedArray(R.array.company_type_icons);
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            Place oldPlace = oldPlaces.get(place.getId());
            updatePlaceBeforeSave(place, oldPlace, iconArray);
        }
        iconArray.recycle();
        repository.add(places);
        EventBus.getDefault().post(new Events.UpdatePlaces());
    }

    public static void add(Place place) {
        Place oldPlace = getSimplePlace(place.getId());

        TypedArray iconArray = App.getInstance().getResources().obtainTypedArray(R.array.company_type_icons);
        updatePlaceBeforeSave(place, oldPlace, iconArray);
        iconArray.recycle();
        repository.add(place);
        calculateData();
    }

    public static void update(Place place) {
        repository.update(place);
    }

    public static Place getSimplePlace(String placeId) {
        Place place = null;
        List<Place> placeList = repository.query(new PlaceByIdSqlSpecification(placeId));
        if (placeList.size() > 0) {
            place = placeList.get(0);
        }
        return place;
    }

    public static List<Place> getSimplePlaces() {
        return repository.query(new PlacesSqlSpecification());
    }

    public static Map<String, Place> getOtherPlacesCompany(String companyId, String placeId) {
        Map<String, Place> map = new HashMap<>();
        List<Place> placeList = repository.query(new OtherPlacesCompanySqlSpecification(companyId, placeId));
        for (Place place : placeList) {
            map.put(place.getName(), place);
        }
        return map;
    }

    public static List<String> getCities() {
        return repository.customQuery(new GetCitySpecification());
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

    private static class PlaceComparator implements Comparator<Place> {

        @Override
        public int compare(Place o1, Place o2) {
            String name1 = o1.getName().toLowerCase();
            String name2 = o2.getName().toLowerCase();
            return name1.compareTo(name2);
        }
    }
}
