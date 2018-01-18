package com.spindealsapp.database.table;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class PlaceTable {

    public static String TABLE_NAME = "places";

    public static class Fields {
        public static final String ID = "id";
        public static final String ADDRESS = "address";
        public static final String NAME = "name";
        public static final String COMPANY_KEY = "companyKey";
        public static final String GEO_LAT = "geoLat";
        public static final String GEO_LON = "geoLon";
        public static final String GEO_RADIUS = "geoRadius";
        public static final String GEO_MESSAGE = "geoMessage";
        public static final String GEO_TIME_START = "geoTimeStart";
        public static final String GEO_TIME_FINISH = "geoTimeFinish";
        public static final String GEO_TIME_FREQUENCY = "geoTimeFrequency";
        public static final String TYPE = "type";
        public static final String TYPE_NAME = "typeName";
        public static final String TYPE_ICON = "typeIcon";
        public static final String INFO = "info";
        public static final String URL = "url";
        public static final String TEL = "tel";
        public static final String ABOUT = "about";
        public static final String ABOUT_MORE = "aboutMore";
        public static final String DISTANCE = "distance";
        public static final String DISTANCE_STRING = "distanceString";
        public static final String CITY = "city";
        public static final String FAVORITES = "favorites";
        public static final String INFO_TIMESTAMP = "infoTimestamp";
        public static final String INFO_CHECKED = "infoChecked";
        public static final String KEYWORDS = "keywords";
    }
}
