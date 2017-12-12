package com.spindealsapp.database.table;

/**
 * Created by Volodymyr Kusenko on 11.12.2017.
 */

public class CouponTable {

    public static String TABLE_NAME = "coupons";

    public static class Fields {
        public static final String STATUS = "status";
        public static final String CODE = "code";
        public static final String COMPANY_KEY = "companyKey";
        public static final String GIFT_KEY = "giftKey";
        public static final String PLACE_KEY = "placeKey";
        public static final String DESCRIPTION = "description";
        public static final String CREATOR = "creator";
        public static final String CREATION = "creation";
        public static final String EXPIRED = "expired";
        public static final String LOCKS = "locks";
        public static final String COMPANY_NAME = "companyName";
        public static final String PLACE_NAME = "placeName";
        public static final String LOGO = "logo";
        public static final String TYPE = "type";
        public static final String TYPE_STRING = "typeString";
        public static final String GEO_LAT = "geoLat";
        public static final String GEO_LON = "geoLon";
        public static final String LOCKED = "locked";
        public static final String REDEEMED = "redeemed";
        public static final String CITY = "city";
        public static final String RULES = "rules";
        public static final String COUPON_TYPE = "couponType";
        public static final String RRULE = "rrule";
        public static final String KEYWORDS = "keywords";
    }
}
