package com.vasilkoff.luckygame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Place;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final int DATABASE_VERSION = 5;
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_COUPONS = "coupons";
    private static final String TABLE_PLACES = "places";
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String TABLE_FAVORITES = "favorites";


    private static final String KEY_ID = "_id";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE_EXPIRE = "dateExpire";
    private static final String KEY_CODE = "code";
    private static final String KEY_COMPANY = "company";

    private static final String KEY_ADDRESS = "address";
    private static final String KEY_NAME_COMPANY = "nameCompany";
    private static final String KEY_COMPANY_KEY = "companyKey";
    private static final String KEY_PLACE_KEY = "placeKey";
    private static final String KEY_GEO_LAT = "lat";
    private static final String KEY_GEO_LON = "lon";
    private static final String KEY_GEO_MESSAGE = "message";
    private static final String KEY_GEO_TIME_START = "start";
    private static final String KEY_GEO_TIME_FINISH = "finish";
    private static final String KEY_GEO_TIME_FREQUENCY = "frequency";
    private static final String KEY_GEO_RADIUS = "radius";
    private static final String KEY_LAST_NOTIFICATION = "lastNotification";

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
        createTableFavorites(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COUPONS);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_PLACES);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NOTIFICATION);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_FAVORITES);
        onCreate(sqLiteDatabase);
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

    private void createTableFavorites(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_FAVORITES + "("
                + KEY_ID + " integer primary key,"
                + KEY_PLACE_KEY  + " text,"
                + "UNIQUE ("
                + KEY_PLACE_KEY
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTableNotification(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NOTIFICATION + "("
                + KEY_ID + " integer primary key,"
                + KEY_PLACE_KEY  + " text,"
                + KEY_LAST_NOTIFICATION + " INTEGER,"
                + "UNIQUE ("
                + KEY_PLACE_KEY
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTablePlaces(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PLACES + "("
                + KEY_ID + " INTEGER primary key,"
                + KEY_PLACE_KEY + " text,"
                + KEY_ADDRESS + " text,"
                + KEY_NAME + " text,"
                + KEY_COMPANY_KEY + " text,"
                + KEY_GEO_LAT + " real,"
                + KEY_GEO_LON + " real,"
                + KEY_GEO_RADIUS + " INTEGER,"
                + KEY_GEO_MESSAGE + " text,"
                + KEY_GEO_TIME_START + " INTEGER,"
                + KEY_GEO_TIME_FINISH + " INTEGER,"
                + KEY_GEO_TIME_FREQUENCY + " INTEGER"
                + ")");
    }

    public void saveTimeNotification(String placeI) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PLACE_KEY, placeI);
        contentValues.put(KEY_LAST_NOTIFICATION, System.currentTimeMillis());

        db.insert(TABLE_NOTIFICATION, null, contentValues);
        db.close();
    }

    public long getTimeNotification(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_NOTIFICATION
                        + " WHERE "
                        + KEY_PLACE_KEY
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

    public void savePlaces(ArrayList<Place> places) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, null, null);

        for (Place place : places) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_PLACE_KEY, place.getId());
            contentValues.put(KEY_COMPANY_KEY, place.getCompanyKey());
            contentValues.put(KEY_ADDRESS, place.getAddress());
            contentValues.put(KEY_NAME, place.getName());
            contentValues.put(KEY_GEO_LAT, place.getGeoLat());
            contentValues.put(KEY_GEO_LON, place.getGeoLon());
            contentValues.put(KEY_GEO_RADIUS, place.getGeoRadius());
            contentValues.put(KEY_GEO_MESSAGE, place.getGeoMessage());
            contentValues.put(KEY_GEO_TIME_START, place.getGeoTimeStart());
            contentValues.put(KEY_GEO_TIME_FINISH, place.getGeoTimeFinish());
            contentValues.put(KEY_GEO_TIME_FREQUENCY, place.getGeoTimeFrequency());

            db.insert(TABLE_PLACES, null, contentValues);
        }

        db.close();
    }

    public ArrayList<Place> getPlacesList() {
        ArrayList<Place> placesList = new ArrayList<Place>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLACES, null);

        if (cursor.moveToFirst()) {
            do {
                placesList.add(parsePlace(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return placesList;
    }

    public Place getPlace(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_PLACES
                        + " WHERE "
                        + KEY_PLACE_KEY
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PLACE_KEY, place.getId());

        db.insert(TABLE_FAVORITES, null, contentValues);
        db.close();
    }

    public void removeFavorites(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_PLACE_KEY + " = ?", new String[] {place.getId()});
        db.close();
    }

    public boolean checkFavorites(Place place) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES,
                null,
                KEY_PLACE_KEY + "=?",
                new String[]{place.getId()},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            result = true;
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return result;
    }

    public HashMap<String, String> getFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
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

        return favorites;
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
                cursor.getLong(11)
        );
    }
}
