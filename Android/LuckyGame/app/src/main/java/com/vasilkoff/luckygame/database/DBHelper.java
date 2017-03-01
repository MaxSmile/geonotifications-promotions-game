package com.vasilkoff.luckygame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vasilkoff.luckygame.entity.Coupon;
import com.vasilkoff.luckygame.entity.Place;
import com.vasilkoff.luckygame.entity.Promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_COUPONS = "coupons";
    private static final String TABLE_PLACES = "places";
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
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTableCoupons(sqLiteDatabase);
        createTablePlaces(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COUPONS);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_PLACES);
        onCreate(sqLiteDatabase);
    }

    private void createTableCoupons(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_COUPONS + "("
                + KEY_ID + " integer primary key,"
                + KEY_ACTIVE + " integer,"
                + KEY_NAME + " text,"
                + KEY_DESCRIPTION + " text,"
                + KEY_DATE_EXPIRE + " text,"
                + KEY_CODE + " text,"
                + KEY_COMPANY + " text"
                + ")");
    }

    private void createTablePlaces(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PLACES + "("
                + KEY_ID + " integer primary key,"
                + KEY_ADDRESS + " text,"
                + KEY_NAME + " text,"
                + KEY_NAME_COMPANY + " text,"
                + KEY_LAT + " real,"
                + KEY_LON + " real"
                + ")");
    }

    public void savePlaces(ArrayList<Place> places) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, null, null);

        for (Place place : places) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ADDRESS, place.getAddress());
            contentValues.put(KEY_NAME, place.getName());
            contentValues.put(KEY_NAME_COMPANY, place.getNameCompany());
            contentValues.put(KEY_LAT, place.getLat());
            contentValues.put(KEY_LON, place.getLon());
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

    public void saveCoupon(String code, String company, Promotion promotion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ACTIVE, 1);
        contentValues.put(KEY_CODE, code);
        contentValues.put(KEY_NAME, promotion.getName());
        contentValues.put(KEY_DESCRIPTION, promotion.getDescription());
        contentValues.put(KEY_DATE_EXPIRE, promotion.getDateFinish());
        contentValues.put(KEY_COMPANY, company);

        db.insert(TABLE_COUPONS, null, contentValues);
        db.close();
    }

    public void setInactive(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, 0);
        db.update(TABLE_COUPONS, values, KEY_CODE + " = ?", new String[] {code});

        db.close();
    }

    public List<Coupon> getCouponsList() {
        List<Coupon> couponsList = new ArrayList<Coupon>();
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

    private Coupon parseCoupon(Cursor cursor) {
        return new Coupon(
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
                cursor.getDouble(4),
                cursor.getDouble(5)
        );
    }
}
