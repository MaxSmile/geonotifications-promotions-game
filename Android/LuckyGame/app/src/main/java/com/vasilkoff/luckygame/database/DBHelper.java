package com.vasilkoff.luckygame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kusenko on 14.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_COUPONS = "coupons";

    private static final String KEY_ID = "_id";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE_EXPIRE = "dateExpire";
    private static final String KEY_CODE = "code";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase, TABLE_COUPONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_COUPONS);
        onCreate(sqLiteDatabase);
    }

    private void createTable(SQLiteDatabase db, String name) {
        db.execSQL("create table " + name + "("
                + KEY_ID + " integer primary key,"
                + KEY_ACTIVE + " integer,"
                + KEY_NAME + " text,"
                + KEY_DESCRIPTION + " text,"
                + KEY_DATE_EXPIRE + " text,"
                + KEY_CODE + " text"
                + ")");
    }

    public void saveCoupon(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CODE, code);


        db.insert(TABLE_COUPONS, null, contentValues);
        db.close();
    }
}
