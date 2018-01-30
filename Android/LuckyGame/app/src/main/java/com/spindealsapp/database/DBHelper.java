package com.spindealsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spindealsapp.App;
import com.spindealsapp.database.table.BoxTable;
import com.spindealsapp.database.table.CompanyTable;
import com.spindealsapp.database.table.CouponTable;
import com.spindealsapp.database.table.GalleryTable;
import com.spindealsapp.database.table.GiftTable;
import com.spindealsapp.database.table.KeywordTable;
import com.spindealsapp.database.table.NotificationTable;
import com.spindealsapp.database.table.PlaceTable;
import com.spindealsapp.database.table.SpinTable;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final int DATABASE_VERSION = 14;

    private static final String DATABASE_NAME = "data.db";
    private static final String KEY_ID = "_id";

    public static synchronized DBHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DBHelper(App.getInstance());
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
        createTableGallery(sqLiteDatabase);
        createTableBox(sqLiteDatabase);
        createTableGift(sqLiteDatabase);
        createTableKeywords(sqLiteDatabase);
        createTableSpin(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + CouponTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + PlaceTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + NotificationTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + CompanyTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + GalleryTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + BoxTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + GiftTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + KeywordTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + SpinTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void createTableSpin(SQLiteDatabase db) {
        db.execSQL("create table " + SpinTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + SpinTable.Fields.ID  + " text,"
                + SpinTable.Fields.COMPANY_KEY + " text,"
                + SpinTable.Fields.PLACE_KEY + " text,"
                + SpinTable.Fields.RRULE + " text,"
                + SpinTable.Fields.LIMIT + " INTEGER,"
                + SpinTable.Fields.SPENT + " INTEGER,"
                + SpinTable.Fields.AVAILABLE + " INTEGER,"
                + SpinTable.Fields.EXTRA_AVAILABLE + " INTEGER,"
                + SpinTable.Fields.EXTRA_CREATE_TIME + " INTEGER,"
                + SpinTable.Fields.EXTRA + " INTEGER,"
                + "UNIQUE ("
                + SpinTable.Fields.ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTableGift(SQLiteDatabase db) {
        db.execSQL("create table " + GiftTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + GiftTable.Fields.ID  + " text,"
                + GiftTable.Fields.COMPANY_ID + " text,"
                + GiftTable.Fields.DESCRIPTION + " text,"
                + GiftTable.Fields.TIME_LOCK + " INTEGER,"
                + GiftTable.Fields.RULES + " text,"
                + GiftTable.Fields.LIMIT_GIFTS + " INTEGER,"
                + GiftTable.Fields.COUNT_AVAILABLE + " INTEGER,"
                + GiftTable.Fields.SPIN_KEY + " text,"
                + GiftTable.Fields.EXPIRATION_TIME + " INTEGER,"
                + "UNIQUE ("
                + GiftTable.Fields.ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTableBox(SQLiteDatabase db) {
        db.execSQL("create table " + BoxTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + BoxTable.Fields.OWNER  + " text,"
                + BoxTable.Fields.COLOR + " INTEGER,"
                + BoxTable.Fields.COUNT + " INTEGER,"
                + BoxTable.Fields.GIFT + " text"
                + ")");
    }

    private void createTableGallery(SQLiteDatabase db) {
        db.execSQL("create table " + GalleryTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + GalleryTable.Fields.OWNER  + " text,"
                + GalleryTable.Fields.PATH + " text"
                + ")");
    }

    private void createTableKeywords(SQLiteDatabase db) {
        db.execSQL("create table " + KeywordTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + KeywordTable.Fields.KEYWORD + " text"
                + ")");
    }

    private void createTableCompanies(SQLiteDatabase db) {
        db.execSQL("create table " + CompanyTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + CompanyTable.Fields.ID + " text,"
                + CompanyTable.Fields.NAME + " text,"
                + CompanyTable.Fields.INFO + " text,"
                + CompanyTable.Fields.LOGO  + " text,"
                + CompanyTable.Fields.FACEBOOK_URL + " text,"
                + CompanyTable.Fields.TYPE + " INTEGER,"
                + "UNIQUE ("
                + CompanyTable.Fields.ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }


    private void createTableCoupons(SQLiteDatabase db) {
        db.execSQL("create table " + CouponTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + CouponTable.Fields.STATUS + " INTEGER,"
                + CouponTable.Fields.CODE + " text,"
                + CouponTable.Fields.COMPANY_KEY + " text,"
                + CouponTable.Fields.GIFT_KEY + " text,"
                + CouponTable.Fields.PLACE_KEY + " text,"
                + CouponTable.Fields.DESCRIPTION + " text,"
                + CouponTable.Fields.CREATOR + " text,"
                + CouponTable.Fields.CREATION + " INTEGER,"
                + CouponTable.Fields.EXPIRED + " INTEGER,"
                + CouponTable.Fields.LOCKS + " INTEGER,"
                + CouponTable.Fields.COMPANY_NAME + " text,"
                + CouponTable.Fields.PLACE_NAME + " text,"
                + CouponTable.Fields.LOGO + " text,"
                + CouponTable.Fields.TYPE + " INTEGER,"
                + CouponTable.Fields.TYPE_STRING + " text,"
                + CouponTable.Fields.GEO_LAT + " REAL,"
                + CouponTable.Fields.GEO_LON + " REAL,"
                + CouponTable.Fields.LOCKED + " INTEGER,"
                + CouponTable.Fields.REDEEMED + " INTEGER,"
                + CouponTable.Fields.CITY + " text,"
                + CouponTable.Fields.RULES + " text,"
                + CouponTable.Fields.COUPON_TYPE + " INTEGER,"
                + CouponTable.Fields.KEYWORDS + " text,"
                + CouponTable.Fields.RRULE + " text,"
                + "UNIQUE ("
                + CouponTable.Fields.CODE
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTableNotification(SQLiteDatabase db) {
        db.execSQL("create table " + NotificationTable.TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + NotificationTable.Fields.PLACE_ID  + " text,"
                + NotificationTable.Fields.LAST_NOTIFICATION + " INTEGER,"
                + "UNIQUE ("
                + NotificationTable.Fields.PLACE_ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    private void createTablePlaces(SQLiteDatabase db) {
        db.execSQL("create table " + PlaceTable.TABLE_NAME + "("
                + KEY_ID + " INTEGER primary key,"
                + PlaceTable.Fields.ID + " text,"
                + PlaceTable.Fields.ADDRESS + " text,"
                + PlaceTable.Fields.NAME + " text,"
                + PlaceTable.Fields.COMPANY_KEY + " text,"
                + PlaceTable.Fields.GEO_LAT  + " real,"
                + PlaceTable.Fields.GEO_LON + " real,"
                + PlaceTable.Fields.GEO_RADIUS + " INTEGER,"
                + PlaceTable.Fields.GEO_MESSAGE + " text,"
                + PlaceTable.Fields.GEO_TIME_START + " INTEGER,"
                + PlaceTable.Fields.GEO_TIME_FINISH + " INTEGER,"
                + PlaceTable.Fields.GEO_TIME_FREQUENCY + " INTEGER,"
                + PlaceTable.Fields.TYPE + " INTEGER,"
                + PlaceTable.Fields.TYPE_NAME + " text,"
                + PlaceTable.Fields.TYPE_ICON + " INTEGER,"
                + PlaceTable.Fields.INFO + " text,"
                + PlaceTable.Fields.URL + " text,"
                + PlaceTable.Fields.TEL + " text,"
                + PlaceTable.Fields.ABOUT + " text,"
                + PlaceTable.Fields.ABOUT_MORE + " text,"
                + PlaceTable.Fields.DISTANCE + " real,"
                + PlaceTable.Fields.DISTANCE_STRING + " text,"
                + PlaceTable.Fields.CITY + " text,"
                + PlaceTable.Fields.FAVORITES + " INTEGER,"
                + PlaceTable.Fields.INFO_TIMESTAMP + " INTEGER,"
                + PlaceTable.Fields.INFO_CHECKED + " INTEGER,"
                + PlaceTable.Fields.KEYWORDS + " text,"
                + "UNIQUE ("
                + PlaceTable.Fields.ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }
}
