package com.vasilkoff.luckygame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.eventbus.Events;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final int DATABASE_VERSION = 7;
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_COUPONS = "coupons";
    private static final String TABLE_PLACES = "places";
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String TABLE_COMPANIES = "companies";


    private static final String KEY_ID = "_id";
    private static final String KEY_LAST_NOTIFICATION = "lastNotification";

    private static final String KEY_PLACE_ID = "id";
    private static final String KEY_PLACE_ADDRESS = "address";
    private static final String KEY_PLACE_NAME = "name";
    private static final String KEY_PLACE_COMPANY_KEY = "companyKey";
    private static final String KEY_PLACE_GEO_LAT = "geoLat";
    private static final String KEY_PLACE_GEO_LON = "geoLon";
    private static final String KEY_PLACE_GEO_RADIUS = "geoRadius";
    private static final String KEY_PLACE_GEO_MESSAGE = "geoMessage";
    private static final String KEY_PLACE_GEO_TIME_START = "geoTimeStart";
    private static final String KEY_PLACE_GEO_TIME_FINISH = "geoTimeFinish";
    private static final String KEY_PLACE_GEO_TIME_FREQUENCY = "geoTimeFrequency";
    private static final String KEY_PLACE_TYPE = "type";
    private static final String KEY_PLACE_TYPE_NAME = "typeName";
    private static final String KEY_PLACE_TYPE_ICON = "typeIcon";
    private static final String KEY_PLACE_INFO = "info";
    private static final String KEY_PLACE_URL = "url";
    private static final String KEY_PLACE_TEL = "tel";
    private static final String KEY_PLACE_ABOUT = "about";
    private static final String KEY_PLACE_ABOUT_MORE = "aboutMore";
    private static final String KEY_PLACE_DISTANCE = "distance";
    private static final String KEY_PLACE_DISTANCE_STRING = "distanceString";
    private static final String KEY_PLACE_CITY = "city";
    private static final String KEY_PLACE_SPIN_AVAILABLE = "spinAvailable";
    private static final String KEY_PLACE_EXTRA_SPIN_AVAILABLE = "extraSpinAvailable";
    private static final String KEY_PLACE_FAVORITES = "favorites";
    private static final String KEY_PLACE_SPIN_FINISH = "spinFinish";

    private static final String KEY_COUPON_STATUS = "status";
    private static final String KEY_COUPON_CODE = "code";
    private static final String KEY_COUPON_COMPANY_KEY = "companyKey";
    private static final String KEY_COUPON_GIFT_KEY = "giftKey";
    private static final String KEY_COUPON_PLACE_KEY = "placeKey";
    private static final String KEY_COUPON_DESCRIPTION = "description";
    private static final String KEY_COUPON_CREATOR = "creator";
    private static final String KEY_COUPON_CREATION = "creation";
    private static final String KEY_COUPON_EXPIRED = "expired";
    private static final String KEY_COUPON_LOCKS = "locks";
    private static final String KEY_COUPON_COMPANY_NAME = "companyName";
    private static final String KEY_COUPON_PLACE_NAME = "placeName";
    private static final String KEY_COUPON_LOGO = "logo";
    private static final String KEY_COUPON_TYPE = "type";
    private static final String KEY_COUPON_TYPE_STRING = "typeString";
    private static final String KEY_COUPON_GEO_LAT = "geoLat";
    private static final String KEY_COUPON_GEO_LON = "geoLon";
    private static final String KEY_COUPON_LOCKED = "locked";
    private static final String KEY_COUPON_REDEEMED = "redeemed";
    private static final String KEY_COUPON_CITY = "city";
    private static final String KEY_COUPON_RULES = "rules";

    private static final String KEY_COMPANY_ID = "id";
    private static final String KEY_COMPANY_NAME = "name";
    private static final String KEY_COMPANY_INFO = "info";
    private static final String KEY_COMPANY_LOGO = "logo";
    private static final String KEY_COMPANY_FACEBOOK_URL = "facebookUrl";
    private static final String KEY_COMPANY_TYPE = "type";



    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTableCoupons(sqLiteDatabase);
        createTablePlaces(sqLiteDatabase);
        createTableNotification(sqLiteDatabase);
        createTableCompanies(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COUPONS);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_PLACES);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NOTIFICATION);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COMPANIES);
        onCreate(sqLiteDatabase);
    }

    private void createTableCompanies(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_COMPANIES + "("
                + KEY_ID + " integer primary key,"
                + KEY_COMPANY_ID + " text,"
                + KEY_COMPANY_NAME + " text,"
                + KEY_COMPANY_INFO + " text,"
                + KEY_COMPANY_LOGO  + " text,"
                + KEY_COMPANY_FACEBOOK_URL + " text,"
                + KEY_COMPANY_TYPE + " INTEGER,"
                + "UNIQUE ("
                + KEY_COMPANY_ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }


    private void createTableCoupons(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_COUPONS + "("
                + KEY_ID + " integer primary key,"
                + KEY_COUPON_STATUS + " INTEGER,"
                + KEY_COUPON_CODE + " text,"
                + KEY_COUPON_COMPANY_KEY + " text,"
                + KEY_COUPON_GIFT_KEY + " text,"
                + KEY_COUPON_PLACE_KEY + " text,"
                + KEY_COUPON_DESCRIPTION + " text,"
                + KEY_COUPON_CREATOR + " text,"
                + KEY_COUPON_CREATION + " INTEGER,"
                + KEY_COUPON_EXPIRED + " INTEGER,"
                + KEY_COUPON_LOCKS + " INTEGER,"
                + KEY_COUPON_COMPANY_NAME + " text,"
                + KEY_COUPON_PLACE_NAME + " text,"
                + KEY_COUPON_LOGO + " text,"
                + KEY_COUPON_TYPE + " INTEGER,"
                + KEY_COUPON_TYPE_STRING + " text,"
                + KEY_COUPON_GEO_LAT + " REAL,"
                + KEY_COUPON_GEO_LON + " REAL,"
                + KEY_COUPON_LOCKED + " INTEGER,"
                + KEY_COUPON_REDEEMED + " INTEGER,"
                + KEY_COUPON_CITY + " text,"
                + KEY_COUPON_RULES + " text,"
                + "UNIQUE ("
                + KEY_COUPON_CODE
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTableNotification(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NOTIFICATION + "("
                + KEY_ID + " integer primary key,"
                + KEY_PLACE_ID  + " text,"
                + KEY_LAST_NOTIFICATION + " INTEGER,"
                + "UNIQUE ("
                + KEY_PLACE_ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTablePlaces(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PLACES + "("
                + KEY_ID + " INTEGER primary key,"
                + KEY_PLACE_ID + " text,"
                + KEY_PLACE_ADDRESS + " text,"
                + KEY_PLACE_NAME + " text,"
                + KEY_PLACE_COMPANY_KEY + " text,"
                + KEY_PLACE_GEO_LAT  + " real,"
                + KEY_PLACE_GEO_LON + " real,"
                + KEY_PLACE_GEO_RADIUS + " INTEGER,"
                + KEY_PLACE_GEO_MESSAGE + " text,"
                + KEY_PLACE_GEO_TIME_START + " INTEGER,"
                + KEY_PLACE_GEO_TIME_FINISH + " INTEGER,"
                + KEY_PLACE_GEO_TIME_FREQUENCY + " INTEGER,"
                + KEY_PLACE_TYPE + " INTEGER,"
                + KEY_PLACE_TYPE_NAME + " text,"
                + KEY_PLACE_TYPE_ICON + " INTEGER,"
                + KEY_PLACE_INFO + " text,"
                + KEY_PLACE_URL + " text,"
                + KEY_PLACE_TEL + " text,"
                + KEY_PLACE_ABOUT + " text,"
                + KEY_PLACE_ABOUT_MORE + " text,"
                + KEY_PLACE_DISTANCE + " real,"
                + KEY_PLACE_DISTANCE_STRING + " text,"
                + KEY_PLACE_CITY + " text,"
                + KEY_PLACE_SPIN_AVAILABLE + " INTEGER,"
                + KEY_PLACE_EXTRA_SPIN_AVAILABLE + " INTEGER,"
                + KEY_PLACE_FAVORITES + " INTEGER,"
                + KEY_PLACE_SPIN_FINISH + " INTEGER,"
                + "UNIQUE ("
                + KEY_PLACE_ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    public void saveTimeNotification(String placeI) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PLACE_ID, placeI);
        contentValues.put(KEY_LAST_NOTIFICATION, System.currentTimeMillis());

        db.insert(TABLE_NOTIFICATION, null, contentValues);
        db.close();
    }

    public long getTimeNotification(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_NOTIFICATION
                        + " WHERE "
                        + KEY_PLACE_ID
                        + " = ?"
                , new String[] {placeId});

        if (cursor.moveToFirst()) {
            do {
                return cursor.getLong(2);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return 0;
    }

    public boolean saveCompanies(ArrayList<Company> companies) {
        long rowInserted = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_COMPANIES, null, null);

            for (Company company: companies) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_COMPANY_ID, company.getId());
                contentValues.put(KEY_COMPANY_NAME, company.getName());
                contentValues.put(KEY_COMPANY_INFO, company.getInfo());
                contentValues.put(KEY_COMPANY_LOGO, company.getLogo());
                contentValues.put(KEY_COMPANY_FACEBOOK_URL, company.getFacebookUrl());
                contentValues.put(KEY_COMPANY_TYPE, company.getType());

                rowInserted = db.insert(TABLE_COMPANIES, null, contentValues);
            }

            if (rowInserted > -1) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        db.close();

        return rowInserted > -1;
    }


    public HashMap<String, Company> getCompanies() {
        HashMap<String, Company> companies = new HashMap<String, Company>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_COMPANIES, null);

        if (cursor.moveToFirst()) {
            do {
                companies.put(cursor.getString(1), parseCompany(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return companies;
    }

    public boolean savePlaces(ArrayList<Place> places) {
        long rowInserted = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PLACES, null, null);

            for (Place place : places) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_PLACE_ID, place.getId());
                contentValues.put(KEY_PLACE_ADDRESS, place.getAddress());
                contentValues.put(KEY_PLACE_NAME, place.getName());
                contentValues.put(KEY_PLACE_COMPANY_KEY, place.getCompanyKey());
                contentValues.put(KEY_PLACE_GEO_LAT, place.getGeoLat());
                contentValues.put(KEY_PLACE_GEO_LON, place.getGeoLon());
                contentValues.put(KEY_PLACE_GEO_RADIUS, place.getGeoRadius());
                contentValues.put(KEY_PLACE_GEO_MESSAGE, place.getGeoMessage());
                contentValues.put(KEY_PLACE_GEO_TIME_START, place.getGeoTimeStart());
                contentValues.put(KEY_PLACE_GEO_TIME_FINISH, place.getGeoTimeFinish());
                contentValues.put(KEY_PLACE_GEO_TIME_FREQUENCY, place.getGeoTimeFrequency());
                contentValues.put(KEY_PLACE_TYPE, place.getType());
                contentValues.put(KEY_PLACE_TYPE_NAME, place.getTypeName());
                contentValues.put(KEY_PLACE_TYPE_ICON, place.getTypeIcon());
                contentValues.put(KEY_PLACE_INFO, place.getInfo());
                contentValues.put(KEY_PLACE_URL, place.getUrl());
                contentValues.put(KEY_PLACE_TEL, place.getTel());
                contentValues.put(KEY_PLACE_ABOUT, place.getAbout());
                contentValues.put(KEY_PLACE_ABOUT_MORE, place.getAboutMore());
                contentValues.put(KEY_PLACE_DISTANCE, place.getDistance());
                contentValues.put(KEY_PLACE_DISTANCE_STRING, place.getDistanceString());
                contentValues.put(KEY_PLACE_CITY, place.getCity());
                contentValues.put(KEY_PLACE_SPIN_AVAILABLE, place.isSpinAvailable() ? 1 : 0);
                contentValues.put(KEY_PLACE_EXTRA_SPIN_AVAILABLE, place.isExtraSpinAvailable() ? 1 : 0);
                contentValues.put(KEY_PLACE_FAVORITES, place.isFavorites() ? 1 : 0);
                contentValues.put(KEY_PLACE_SPIN_FINISH, place.getSpinFinish());

                rowInserted = db.insert(TABLE_PLACES, null, contentValues);
            }

            if (rowInserted > -1) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        db.close();

        return rowInserted > -1;
    }

    public List<String> getCities() {
        List<String> cities = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT "
                + KEY_PLACE_CITY
                + " FROM "
                + TABLE_PLACES
                + " ORDER BY "
                + KEY_PLACE_CITY
                + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                cities.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return cities;
    }

    public ArrayList<Place> getOrderPlaces() {
        HashMap<String, Place> orderPlaces = new HashMap<String, Place>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_PLACES
                + " ORDER BY "
                + KEY_PLACE_DISTANCE
                + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                orderPlaces.put(cursor.getString(4), parsePlace(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return new ArrayList<Place>(orderPlaces.values());
    }


    public HashMap<String, Place> getPlaces() {
        HashMap<String, Place> places = new HashMap<String, Place>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_PLACES, null);

        if (cursor.moveToFirst()) {
            do {
                places.put(cursor.getString(1), parsePlace(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return places;
    }

    public Place getPlace(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_PLACES
                        + " WHERE "
                        + KEY_PLACE_ID
                        + " = ?"
                , new String[] {placeId});

        if (cursor.moveToFirst()) {
            do {
                return parsePlace(cursor);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return null;
    }

    public void saveFavorites(Place place) {

    }

    public void removeFavorites(Place place) {

    }

    public boolean checkFavorites(Place place) {
        return false;
    }

    public void updatePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_FAVORITES, place.isFavorites() ? 1 : 0);
        db.update(TABLE_PLACES, values, KEY_PLACE_ID + " = ?", new String[] {place.getId()});
        db.close();
    }



    public HashMap<String, String> getFavorites() {
       /* SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, String> favorites = new HashMap<String, String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITES, null);
        if (cursor.moveToFirst()) {
            do {
                favorites.put(cursor.getString(1), cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return favorites;*/
       return null;
    }

    public void saveCoupon(CouponExtension coupon) {
        SQLiteDatabase db = this.getWritableDatabase();
        saveCouponExtension(coupon,db);
        db.close();
    }

    public void saveCoupons(List<CouponExtension> coupons) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < coupons.size(); i++) {
            saveCouponExtension(coupons.get(i),db);
        }
        db.close();
    }

    private void saveCouponExtension(CouponExtension coupon,SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COUPON_STATUS, coupon.getStatus());
        contentValues.put(KEY_COUPON_CODE, coupon.getCode());
        contentValues.put(KEY_COUPON_COMPANY_KEY, coupon.getCompanyKey());
        contentValues.put(KEY_COUPON_GIFT_KEY, coupon.getGiftKey());
        contentValues.put(KEY_COUPON_PLACE_KEY , coupon.getPlaceKey());
        contentValues.put(KEY_COUPON_DESCRIPTION, coupon.getDescription());
        contentValues.put(KEY_COUPON_CREATOR, coupon.getCreator());
        contentValues.put(KEY_COUPON_CREATION, coupon.getCreation());
        contentValues.put(KEY_COUPON_EXPIRED, coupon.getExpired());
        contentValues.put(KEY_COUPON_LOCKS, coupon.getLocks());
        contentValues.put(KEY_COUPON_COMPANY_NAME, coupon.getCompanyName());
        contentValues.put(KEY_COUPON_PLACE_NAME, coupon.getPlaceName());
        contentValues.put(KEY_COUPON_LOGO, coupon.getLogo());
        contentValues.put(KEY_COUPON_TYPE, coupon.getType());
        contentValues.put(KEY_COUPON_TYPE_STRING, coupon.getTypeString());
        contentValues.put(KEY_COUPON_GEO_LAT, coupon.getGeoLat());
        contentValues.put(KEY_COUPON_GEO_LON, coupon.getGeoLon());
        contentValues.put(KEY_COUPON_LOCKED, coupon.getLocked());
        contentValues.put(KEY_COUPON_REDEEMED, coupon.getRedeemed());
        contentValues.put(KEY_COUPON_CITY, coupon.getCity());
        contentValues.put(KEY_COUPON_RULES, coupon.getRules());

        db.insert(TABLE_COUPONS, null, contentValues);
    }

    public List<CouponExtension> getCouponsExtension() {
        List<CouponExtension>  coupons = new ArrayList<CouponExtension>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COUPONS, null);
        if (cursor.moveToFirst()) {
            do {
                coupons.add(parseCouponExtension(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return coupons;
    }

    public List<CouponExtension> getCouponsByPlace(String placeKey) {
        List<CouponExtension>  coupons = new ArrayList<CouponExtension>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_COUPONS,
                null,
                KEY_COUPON_PLACE_KEY + "=?",
                new String[]{placeKey},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                coupons.add(parseCouponExtension(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return coupons;
    }

    public List<CouponExtension> getCouponsByCode(String couponKey) {
        List<CouponExtension>  coupons = new ArrayList<CouponExtension>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_COUPONS,
                null,
                KEY_COUPON_CODE + "=?",
                new String[]{couponKey},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                coupons.add(parseCouponExtension(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return coupons;
    }

    public List<String> getCoupons() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> coupons = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COUPONS, null);
        if (cursor.moveToFirst()) {
            do {
                coupons.add(cursor.getString(2));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return coupons;
    }

    private Company parseCompany(Cursor cursor) {
        return new Company(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6)
        );
    }

    private CouponExtension parseCouponExtension(Cursor cursor) {
        return new CouponExtension(
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getLong(8),
                cursor.getLong(9),
                cursor.getLong(10),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getString(13),
                cursor.getInt(14),
                cursor.getString(15),
                cursor.getDouble(16),
                cursor.getDouble(17),
                cursor.getInt(18),
                cursor.getLong(19),
                cursor.getString(20),
                cursor.getString(21)
        );
    }

    private Place parsePlace(Cursor cursor) {
        return new Place(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getInt(7),
                cursor.getString(8),
                cursor.getLong(9),
                cursor.getLong(10),
                cursor.getLong(11),
                cursor.getInt(12),
                cursor.getString(13),
                cursor.getInt(14),
                cursor.getString(15),
                cursor.getString(16),
                cursor.getString(17),
                cursor.getString(18),
                cursor.getString(19),
                cursor.getDouble(20),
                cursor.getString(21),
                cursor.getString(22),
                cursor.getInt(23) > 0,
                cursor.getInt(24) > 0,
                cursor.getInt(25) > 0,
                cursor.getLong(26)
        );
    }
}
