package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.PlaceTable;
import com.spindealsapp.entity.Place;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class PlaceToContentValuesMapper implements Mapper<Place, ContentValues> {
    @Override
    public ContentValues map(Place place) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlaceTable.Fields.ID, place.getId());
        contentValues.put(PlaceTable.Fields.ADDRESS, place.getAddress());
        contentValues.put(PlaceTable.Fields.NAME, place.getName());
        contentValues.put(PlaceTable.Fields.COMPANY_KEY, place.getCompanyKey());
        contentValues.put(PlaceTable.Fields.GEO_LAT, place.getGeoLat());
        contentValues.put(PlaceTable.Fields.GEO_LON, place.getGeoLon());
        contentValues.put(PlaceTable.Fields.GEO_RADIUS, place.getGeoRadius());
        contentValues.put(PlaceTable.Fields.GEO_MESSAGE, place.getGeoMessage());
        contentValues.put(PlaceTable.Fields.GEO_TIME_START, place.getGeoTimeStart());
        contentValues.put(PlaceTable.Fields.GEO_TIME_FINISH, place.getGeoTimeFinish());
        contentValues.put(PlaceTable.Fields.GEO_TIME_FREQUENCY, place.getGeoTimeFrequency());
        contentValues.put(PlaceTable.Fields.TYPE, place.getType());
        contentValues.put(PlaceTable.Fields.TYPE_NAME, place.getTypeName());
        contentValues.put(PlaceTable.Fields.TYPE_ICON, place.getTypeIcon());
        contentValues.put(PlaceTable.Fields.INFO, place.getInfo());
        contentValues.put(PlaceTable.Fields.URL, place.getUrl());
        contentValues.put(PlaceTable.Fields.TEL, place.getTel());
        contentValues.put(PlaceTable.Fields.ABOUT, place.getAbout());
        contentValues.put(PlaceTable.Fields.ABOUT_MORE, place.getAboutMore());
        contentValues.put(PlaceTable.Fields.DISTANCE, place.getDistance());
        contentValues.put(PlaceTable.Fields.DISTANCE_STRING, place.getDistanceString());
        contentValues.put(PlaceTable.Fields.CITY, place.getCity());
        contentValues.put(PlaceTable.Fields.FAVORITES, place.isFavorites() ? 1 : 0);
        contentValues.put(PlaceTable.Fields.INFO_TIMESTAMP, place.getInfoTimestamp());
        contentValues.put(PlaceTable.Fields.INFO_CHECKED, place.isInfoChecked() ? 1 : 0);
        contentValues.put(PlaceTable.Fields.KEYWORDS, place.getKeywords());
        return contentValues;
    }
}
