package com.spindealsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.spindealsapp.App;
import com.spindealsapp.Constants;
import com.spindealsapp.database.mapper.CouponToContentValuesMapper;
import com.spindealsapp.database.table.CompanyTable;
import com.spindealsapp.database.table.CouponTable;
import com.spindealsapp.database.table.GalleryTable;
import com.spindealsapp.database.table.GiftTable;
import com.spindealsapp.database.table.KeywordTable;
import com.spindealsapp.database.table.NotificationTable;
import com.spindealsapp.database.table.PlaceTable;
import com.spindealsapp.entity.Box;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.CouponExtension;
import com.spindealsapp.entity.Gift;
import com.spindealsapp.entity.Place;
import com.spindealsapp.entity.Spin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final int DATABASE_VERSION = 14;
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_BOX = "box";
    private static final String TABLE_SPIN = "spin";
    private static final String KEY_ID = "_id";

    private static final String KEY_BOX_SPIN_ID = "spinId";
    private static final String KEY_BOX_COLOR = "color";
    private static final String KEY_BOX_COUNT = "count";
    private static final String KEY_BOX_GIFT = "gift";

    private static final String KEY_SPIN_ID = "id";
    private static final String KEY_SPIN_COMPANY_KEY = "companyKey";
    private static final String KEY_SPIN_PLACE_KEY = "placeKey";
    private static final String KEY_SPIN_LIMIT = "spinLimit";
    private static final String KEY_SPIN_RRULE = "rrule";
    private static final String KEY_SPIN_SPENT = "spent";
    private static final String KEY_SPIN_AVAILABLE = "available";
    private static final String KEY_SPIN_EXTRA_AVAILABLE = "extraAvailable";
    private static final String KEY_SPIN_EXTRA = "extra";
    private static final String KEY_SPIN_EXTRA_CREATE_TIME = "extraCreateTime";

    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

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
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_BOX);
        sqLiteDatabase.execSQL("drop table if exists " + GiftTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + KeywordTable.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_SPIN);
        onCreate(sqLiteDatabase);
    }

    private void createTableSpin(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_SPIN + "("
                + KEY_ID + " integer primary key,"
                + KEY_SPIN_ID  + " text,"
                + KEY_SPIN_COMPANY_KEY + " text,"
                + KEY_SPIN_PLACE_KEY + " text,"
                + KEY_SPIN_RRULE + " text,"
                + KEY_SPIN_LIMIT + " INTEGER,"
                + KEY_SPIN_SPENT + " INTEGER,"
                + KEY_SPIN_AVAILABLE + " INTEGER,"
                + KEY_SPIN_EXTRA_AVAILABLE + " INTEGER,"
                + KEY_SPIN_EXTRA_CREATE_TIME + " INTEGER,"
                + KEY_SPIN_EXTRA + " INTEGER,"
                + "UNIQUE ("
                + KEY_SPIN_ID
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
        db.execSQL("create table " + TABLE_BOX + "("
                + KEY_ID + " integer primary key,"
                + KEY_BOX_SPIN_ID  + " text,"
                + KEY_BOX_COLOR + " INTEGER,"
                + KEY_BOX_COUNT + " INTEGER,"
                + KEY_BOX_GIFT + " text"
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

    public void insertSpin(Spin spin) {
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.insert(TABLE_SPIN, null, getSpinValues(spin));
        db.delete(TABLE_BOX, KEY_BOX_SPIN_ID + " = ?", new String[] {spin.getId()});
        saveBoxes(db, spin);
        //db.close();
        DatabaseManager.getInstance().closeDatabase();
    }

    public void updateSpin(Spin spin) {
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.update(TABLE_SPIN, getSpinValues(spin), KEY_SPIN_ID + " = ?", new String[] {spin.getId()});
        //db.close();
        DatabaseManager.getInstance().closeDatabase();
    }

    public Spin getSpin(String Id) {
        Spin spin = new Spin();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_SPIN
                        + " WHERE "
                        + KEY_SPIN_ID
                        + " = ?"
                , new String[] {Id});
        if (cursor.moveToFirst()) {
            do {
                spin = parseSpin(cursor);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return spin;
    }

    private ContentValues getSpinValues(Spin spin) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_SPIN_ID, spin.getId());
        contentValues.put(KEY_SPIN_COMPANY_KEY, spin.getCompanyKey());
        contentValues.put(KEY_SPIN_PLACE_KEY, spin.getPlaceKey());
        contentValues.put(KEY_SPIN_LIMIT, spin.getLimit());
        contentValues.put(KEY_SPIN_RRULE, spin.getRrule());
        contentValues.put(KEY_SPIN_SPENT, spin.getSpent());
        contentValues.put(KEY_SPIN_AVAILABLE, spin.isAvailable() ? 1 : 0);
        contentValues.put(KEY_SPIN_EXTRA_AVAILABLE, spin.isExtraAvailable() ? 1 : 0);
        contentValues.put(KEY_SPIN_EXTRA_CREATE_TIME, spin.getExtraCreateTime());
        contentValues.put(KEY_SPIN_EXTRA, spin.isExtra() ? 1 : 0);

        return contentValues;
    }


    public boolean saveSpins(ArrayList<Spin> spins) {
        long rowInserted = -1;
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_SPIN, null, null);
            db.delete(TABLE_BOX, null, null);

            for (Spin spin: spins) {
                rowInserted = db.insert(TABLE_SPIN, null, getSpinValues(spin));

                if (rowInserted > -1) {
                    rowInserted = saveBoxes(db, spin);
                }
            }

            if (rowInserted > -1) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        //db.close();
        DatabaseManager.getInstance().closeDatabase();

        return rowInserted > -1;
    }

    private long saveBoxes(SQLiteDatabase db, Spin spin) {
        long rowInserted = -1;
        List<Box> box = spin.getBox();
        if (box != null) {
            for (int i = 0; i < box.size(); i++) {
                ContentValues boxValues = new ContentValues();
                boxValues.put(KEY_BOX_SPIN_ID, spin.getId());
                boxValues.put(KEY_BOX_COLOR, box.get(i).getColor());
                boxValues.put(KEY_BOX_COUNT, box.get(i).getCount());
                boxValues.put(KEY_BOX_GIFT, box.get(i).getGift());
                rowInserted = db.insert(TABLE_BOX, null, boxValues);
            }
        }

        return rowInserted;
    }

    public HashMap<String, Spin> getSpins() {
        HashMap<String, Spin> spins = new HashMap<String, Spin>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_SPIN
                + " ORDER BY "
                + KEY_SPIN_ID
                + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Spin spin = parseSpin(cursor);
                spin.setBox(getBox(spin.getId()));
                spins.put(spin.getId(), spin);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return spins;
    }

    private List<Box> getBox(String spinId) {
        List<Box> box = new ArrayList<Box>();
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                        + TABLE_BOX
                        + " WHERE "
                        + KEY_BOX_SPIN_ID
                        + " = ?"
                , new String[] {spinId});

        if (cursor.moveToFirst()) {
            do {
                box.add(parseBox(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        //db.close();
        DatabaseManager.getInstance().closeDatabase();

        return box;
    }

    private Box parseBox(Cursor cursor) {
        return new Box(
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getString(4)
        );
    }

    private Spin parseSpin(Cursor cursor) {
        return new Spin(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getLong(5),
                cursor.getLong(6),
                cursor.getInt(7) > 0,
                cursor.getInt(8) > 0,
                cursor.getLong(9),
                cursor.getInt(10) > 0
        );
    }
}
