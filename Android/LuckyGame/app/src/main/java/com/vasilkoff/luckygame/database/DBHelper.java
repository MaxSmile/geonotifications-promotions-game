package com.vasilkoff.luckygame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vasilkoff.luckygame.Constants;
import com.vasilkoff.luckygame.entity.Box;
import com.vasilkoff.luckygame.entity.Company;
import com.vasilkoff.luckygame.entity.CouponExtension;
import com.vasilkoff.luckygame.entity.Gift;
import com.vasilkoff.luckygame.entity.Place;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final int DATABASE_VERSION = 9;
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_COUPONS = "coupons";
    private static final String TABLE_PLACES = "places";
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String TABLE_COMPANIES = "companies";
    private static final String TABLE_GALLERY = "gallery";
    private static final String TABLE_BOX = "box";
    private static final String TABLE_GIFT = "gift";
    private static final String TABLE_KEYWORDS = "keywords";

    private static final String KEY_KEYWORDS_KEYWORD = "keyword";
    private static final String KEY_KEYWORDS = "keywords";

    private static final String KEY_GIFT_ID = "id";
    private static final String KEY_GIFT_DATE_START = "dateStart";
    private static final String KEY_GIFT_DATE_FINISH = "dateFinish";
    private static final String KEY_GIFT_DESCRIPTION = "description";
    private static final String KEY_GIFT_TIME_LOCK = "timeLock";
    private static final String KEY_GIFT_RULES = "rules";
    private static final String KEY_GIFT_LIMIT_GIFTS = "limitGifts";
    private static final String KEY_GIFT_COUNT_AVAILABLE = "countAvailable";
    private static final String KEY_GIFT_ACTIVE = "active";

    private static final String KEY_BOX_COLOR = "color";
    private static final String KEY_BOX_COUNT = "count";
    private static final String KEY_BOX_GIFT = "gift";

    private static final String KEY_ID = "_id";
    private static final String KEY_GALLERY_URL = "galleryUrl";

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
    private static final String KEY_PLACE_SPIN_START = "spinStart";
    private static final String KEY_PLACE_INFO_TIMESTAMP = "infoTimestamp";
    private static final String KEY_PLACE_INFO_CHECKED = "infoChecked";

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
    private static final String KEY_COUPON_COUPON_TYPE = "couponType";

    private static final String KEY_COMPANY_ID = "companyId";
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
        createTableGallery(sqLiteDatabase);
        createTableBox(sqLiteDatabase);
        createTableGift(sqLiteDatabase);
        createTableKeywords(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COUPONS);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_PLACES);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NOTIFICATION);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COMPANIES);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_GALLERY);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_BOX);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_GIFT);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_KEYWORDS);
        onCreate(sqLiteDatabase);
    }

    private void createTableGift(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_GIFT + "("
                + KEY_ID + " integer primary key,"
                + KEY_GIFT_ID  + " text,"
                + KEY_GIFT_DATE_START + " INTEGER,"
                + KEY_GIFT_DATE_FINISH + " INTEGER,"
                + KEY_COMPANY_ID + " text,"
                + KEY_GIFT_DESCRIPTION + " text,"
                + KEY_GIFT_TIME_LOCK + " INTEGER,"
                + KEY_GIFT_RULES + " text,"
                + KEY_GIFT_LIMIT_GIFTS + " INTEGER,"
                + KEY_GIFT_COUNT_AVAILABLE + " INTEGER,"
                + KEY_GIFT_ACTIVE + " INTEGER"
                + ")");
    }

    private void createTableBox(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_BOX + "("
                + KEY_ID + " integer primary key,"
                + KEY_PLACE_ID  + " text,"
                + KEY_BOX_COLOR + " INTEGER,"
                + KEY_BOX_COUNT + " INTEGER,"
                + KEY_BOX_GIFT+ " text"
                + ")");
    }

    private void createTableGallery(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_GALLERY + "("
                + KEY_ID + " integer primary key,"
                + KEY_PLACE_ID  + " text,"
                + KEY_GALLERY_URL + " text"
                + ")");
    }

    private void createTableKeywords(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_KEYWORDS + "("
                + KEY_ID + " integer primary key,"
                + KEY_KEYWORDS_KEYWORD + " text"
                + ")");
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
                + KEY_COUPON_COUPON_TYPE + " INTEGER,"
                + KEY_KEYWORDS + " text,"
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
                + KEY_PLACE_SPIN_START + " INTEGER,"
                + KEY_PLACE_INFO_TIMESTAMP + " INTEGER,"
                + KEY_PLACE_INFO_CHECKED + " INTEGER,"
                + KEY_KEYWORDS + " text,"
                + "UNIQUE ("
                + KEY_PLACE_ID
                + ") ON CONFLICT REPLACE"
                + ")");
    }

    public void saveKeywords(ArrayList<String> keywords) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted;
        db.beginTransaction();
        try {
            rowInserted = db.delete(TABLE_KEYWORDS, null, null);
            for (int i = 0; i < keywords.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_KEYWORDS_KEYWORD, keywords.get(i));
                rowInserted = db.insert(TABLE_KEYWORDS, null, contentValues);
            }

            if (rowInserted > -1) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }

        db.close();
    }

    public ArrayList<String> getKeywords() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> keywords = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KEYWORDS, null);
        if (cursor.moveToFirst()) {
            do {
                keywords.add(cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();
        return keywords;
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

    public boolean saveGifts(ArrayList<Gift> gifts) {
        long rowInserted = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_GIFT, null, null);

            for (Gift gift: gifts) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_GIFT_ID, gift.getId());
                contentValues.put(KEY_GIFT_DATE_START, gift.getDateStart());
                contentValues.put(KEY_GIFT_DATE_FINISH, gift.getDateFinish());
                contentValues.put(KEY_COMPANY_ID, gift.getCompanyKey());
                contentValues.put(KEY_GIFT_DESCRIPTION, gift.getDescription());
                contentValues.put(KEY_GIFT_TIME_LOCK, gift.getTimeLock());
                contentValues.put(KEY_GIFT_RULES, gift.getRules());
                contentValues.put(KEY_GIFT_LIMIT_GIFTS, gift.getLimitGifts());
                contentValues.put(KEY_GIFT_COUNT_AVAILABLE, gift.getCountAvailable());
                contentValues.put(KEY_GIFT_ACTIVE, gift.isActive() ? 1 : 0);

                rowInserted = db.insert(TABLE_GIFT, null, contentValues);
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

    public HashMap<String, Gift> getGifts(String companyId) {
        HashMap<String, Gift> gifts = new HashMap<String, Gift>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_GIFT
                        + " WHERE "
                        + KEY_COMPANY_ID
                        + " = ?"
                , new String[] {companyId});
        if (cursor.moveToFirst()) {
            do {
                gifts.put(cursor.getString(1), parseGift(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();
        return gifts;
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

    public Company getCompany(String id) {
        Company company = new Company();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_COMPANIES
                        + " WHERE "
                        + KEY_COMPANY_ID
                        + " = ?"
                , new String[] {id});
        if (cursor.moveToFirst()) {
            do {
                company = parseCompany(cursor);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return company;
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

    public boolean updatePlaces(ArrayList<Place> places) {
        long rowInserted = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Place place : places) {
                ContentValues values = new ContentValues();
                values.put(KEY_PLACE_DISTANCE, place.getDistance());
                values.put(KEY_PLACE_DISTANCE_STRING, place.getDistanceString());
                rowInserted = db.update(TABLE_PLACES, values, KEY_PLACE_ID + " = ?", new String[] {place.getId()});
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

    public boolean savePlaces(ArrayList<Place> places) {
        long rowInserted = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PLACES, null, null);
            db.delete(TABLE_GALLERY, null, null);
            db.delete(TABLE_BOX, null, null);

            for (Place place : places) {
                ContentValues contentValues = getPlaceValues(place);
                rowInserted = db.insert(TABLE_PLACES, null, contentValues);

                if (rowInserted > -1) {
                    List<String> gallery = place.getGallery();
                    for (int i = 0; i < gallery.size(); i++) {
                        ContentValues galleryValues = new ContentValues();
                        galleryValues.put(KEY_PLACE_ID, place.getId());
                        galleryValues.put(KEY_GALLERY_URL, gallery.get(i));
                        rowInserted = db.insert(TABLE_GALLERY, null, galleryValues);
                    }
                }

                if (rowInserted > -1) {
                    List<Box> box = place.getBox();
                    for (int i = 0; i < box.size(); i++) {
                        ContentValues boxValues = new ContentValues();
                        boxValues.put(KEY_PLACE_ID, place.getId());
                        boxValues.put(KEY_BOX_COLOR, box.get(i).getColor());
                        boxValues.put(KEY_BOX_COUNT, box.get(i).getCount());
                        boxValues.put(KEY_BOX_GIFT, box.get(i).getGift());
                        rowInserted = db.insert(TABLE_BOX, null, boxValues);
                    }
                }
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

    private ContentValues getPlaceValues(Place place) {
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
        contentValues.put(KEY_PLACE_SPIN_START, place.getSpinStart());
        contentValues.put(KEY_PLACE_INFO_TIMESTAMP, place.getInfoTimestamp());
        contentValues.put(KEY_PLACE_INFO_CHECKED, place.isInfoChecked() ? 1 : 0);
        contentValues.put(KEY_KEYWORDS, place.getKeywords());

        return contentValues;
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

    public HashMap<String, Place> getOtherPlacesCompany(String companyId, String placeId) {
        HashMap<String, Place> places = new HashMap<String, Place>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_PLACES
                        + " WHERE "
                        + KEY_PLACE_COMPANY_KEY
                        + " = ? AND "
                        + KEY_PLACE_ID
                        + " <> ?"
                , new String[] {companyId, placeId});

        if (cursor.moveToFirst()) {
            do {
                places.put(cursor.getString(3), parsePlace(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return places;
    }

    public Place getPlace(String placeId) {
        Place place = new Place();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_PLACES
                        + " WHERE "
                        + KEY_PLACE_ID
                        + " = ?"
                , new String[] {placeId});

        if (cursor.moveToFirst()) {
            do {
                place = parsePlace(cursor);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        if (place.getId() != null) {
            place.setGallery(getGallery(place.getId()));
            place.setBox(getBox(place.getId()));
        }

        cursor.close();
        db.close();

        return place;
    }

    private List<Box> getBox(String placeId) {
        List<Box> box = new ArrayList<Box>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_BOX
                        + " WHERE "
                        + KEY_PLACE_ID
                        + " = ?"
                , new String[] {placeId});

        if (cursor.moveToFirst()) {
            do {
                box.add(parseBox(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return box;
    }

    private List<String> getGallery(String placeId) {
        List<String> gallery = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_GALLERY
                        + " WHERE "
                        + KEY_PLACE_ID
                        + " = ?"
                , new String[] {placeId});

        if (cursor.moveToFirst()) {
            do {
                gallery.add(cursor.getString(2));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return gallery;
    }

    public void updatePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getPlaceValues(place);
        db.update(TABLE_PLACES, contentValues, KEY_PLACE_ID + " = ?", new String[] {place.getId()});
        db.close();
    }

    public void saveCoupon(CouponExtension coupon) {
        SQLiteDatabase db = this.getWritableDatabase();
        saveCouponExtension(coupon,db);
        db.close();
    }

    public void saveCoupons(List<CouponExtension> coupons, boolean offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            if (offer) {
                db.delete(TABLE_COUPONS, KEY_COUPON_COUPON_TYPE  + " = ?", new String[]{String.valueOf(Constants.COUPON_TYPE_OFFER)});
            }

            for (int i = 0; i < coupons.size(); i++) {
                saveCouponExtension(coupons.get(i),db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
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
        contentValues.put(KEY_COUPON_COUPON_TYPE, coupon.getCouponType());
        contentValues.put(KEY_KEYWORDS, coupon.getKeywords());

        db.insert(TABLE_COUPONS, null, contentValues);
    }

    public List<CouponExtension> getCouponsExtension() {
        List<CouponExtension>  coupons = new ArrayList<CouponExtension>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_COUPONS
                + " WHERE "
                + KEY_COUPON_COUPON_TYPE
                + " <> ? "
                + " ORDER BY "
                + KEY_COUPON_CREATION
                + " DESC", new String[]{String.valueOf(Constants.COUPON_TYPE_OFFER)});
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
                KEY_COUPON_CREATION + " DESC");
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

    public List<CouponExtension> getCouponsByPlaceGift(String giftKey, String placeKey) {
        List<CouponExtension>  coupons = new ArrayList<CouponExtension>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_COUPONS,
                null,
                KEY_COUPON_GIFT_KEY + " = ? AND "
                        + KEY_COUPON_PLACE_KEY
                        + " = ?",
                new String[]{giftKey, placeKey},
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
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_COUPONS
                + " WHERE "
                + KEY_COUPON_COUPON_TYPE
                + " <> ?", new String[]{String.valueOf(Constants.COUPON_TYPE_OFFER)});
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

    private Gift parseGift(Cursor cursor) {
        return new Gift(
                cursor.getString(1),
                cursor.getLong(2),
                cursor.getLong(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getLong(6),
                cursor.getString(7),
                cursor.getLong(8),
                cursor.getLong(9),
                cursor.getInt(10) > 0
        );
    }

    private Box parseBox(Cursor cursor) {
        return new Box(
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getString(4)
        );
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
                cursor.getString(21),
                cursor.getInt(22),
                cursor.getString(23)
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
                cursor.getLong(26),
                cursor.getLong(27),
                cursor.getLong(28),
                cursor.getInt(29) > 0,
                cursor.getString(30)
        );
    }
}
