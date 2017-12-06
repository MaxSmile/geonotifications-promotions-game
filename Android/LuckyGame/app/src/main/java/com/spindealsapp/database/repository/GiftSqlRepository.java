package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CursorToGiftMapper;
import com.spindealsapp.database.mapper.GiftToContentValuesMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.table.GiftTable;
import com.spindealsapp.entity.Company;
import com.spindealsapp.entity.Gift;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 22.11.2017.
 */

public class GiftSqlRepository implements Repository<Gift> {

    private final SQLiteOpenHelper openHelper;

    private final Mapper<Gift, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Gift> toObjectMapper;

    public GiftSqlRepository() {
        this.openHelper = DBHelper.getInstance();

        this.toContentValuesMapper = new GiftToContentValuesMapper();
        this.toObjectMapper = new CursorToGiftMapper();
    }

    @Override
    public void add(Gift item) {
        //SQLiteDatabase database = openHelper.getWritableDatabase();
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = toContentValuesMapper.map(item);
        database.insert(GiftTable.TABLE_NAME, null, contentValues);
        //database.close();
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void add(Iterable<Gift> items) {
        //SQLiteDatabase database = openHelper.getWritableDatabase();
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            database.delete(GiftTable.TABLE_NAME, null, null);

            for (Gift item : items) {
                ContentValues contentValues = toContentValuesMapper.map(item);
                database.insert(GiftTable.TABLE_NAME, null, contentValues);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            //database.close();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    @Override
    public void update(Gift item) {

    }

    @Override
    public void remove(Gift item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Gift> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        //SQLiteDatabase database = openHelper.getWritableDatabase();
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<Gift> list = new ArrayList<>();

        try {
            Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

            for (int i = 0, size = cursor.getCount(); i < size; i++) {
                cursor.moveToPosition(i);

                list.add(toObjectMapper.map(cursor));
            }

            cursor.close();

            return list;
        } finally {
            //database.close();
            DatabaseManager.getInstance().closeDatabase();
        }
    }
}
