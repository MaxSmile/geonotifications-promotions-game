package com.vasilkoff.luckygame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vasilkoff.luckygame.entity.Coupon;
import com.vasilkoff.luckygame.entity.CouponDB;
import com.vasilkoff.luckygame.entity.Place;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_COUPONS = "coupons";
    private static final String TABLE_PLACES = "places";
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String TAG = "DBHelper";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COUPONS);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_PLACES);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NOTIFICATION);
        onCreate(sqLiteDatabase);
    }

    private void createTableCoupons(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_COUPONS + "("
                + KEY_ID + " integer primary key,"
                + KEY_CODE + " text"
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

    public void saveCoupon(Coupon coupon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CODE, coupon.getCode());

        db.insert(TABLE_COUPONS, null, contentValues);
        db.close();
    }

    public List<String> getCoupons() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> coupons = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COUPONS, null);
        if (cursor.moveToFirst()) {
            do {
                coupons.add(cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return coupons;
    }

    public void setInactive(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, 0);
        db.update(TABLE_COUPONS, values, KEY_CODE + " = ?", new String[] {code});

        db.close();
    }

    public List<CouponDB> getCouponsList() {
        List<CouponDB> couponsList = new ArrayList<CouponDB>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_COUPONS
                + " WHERE "
                + KEY_ACTIVE
                + " > 0"
                , null);

        if (cursor.moveToFirst()) {
            do {
                couponsList.add(parseCoupon(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return couponsList;
    }

    private CouponDB parseCoupon(Cursor cursor) {
        return new CouponDB(
                cursor.getInt(1) > 0,
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
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
