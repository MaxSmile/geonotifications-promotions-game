package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spindealsapp.Constants;
import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CouponToContentValuesMapper;
import com.spindealsapp.database.mapper.CursorToCouponMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.table.CouponTable;
import com.spindealsapp.entity.CouponExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 11.12.2017.
 */

public class CouponSqlRepository implements Repository<CouponExtension> {

    private final Mapper<CouponExtension, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, CouponExtension> toObjectMapper;

    public CouponSqlRepository() {
        this.toContentValuesMapper = new CouponToContentValuesMapper();
        this.toObjectMapper = new CursorToCouponMapper();
    }

    @Override
    public void add(CouponExtension item) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.insert(CouponTable.TABLE_NAME, null, toContentValuesMapper.map(item));
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void add(Iterable<CouponExtension> items) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            for (CouponExtension item : items) {
                database.insert(CouponTable.TABLE_NAME, null, toContentValuesMapper.map(item));
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void update(CouponExtension item) {

    }

    @Override
    public void remove(CouponExtension item) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.delete(CouponTable.TABLE_NAME, CouponTable.Fields.CODE + " = ?", new String[] {item.getCode()});
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void remove(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.execSQL(sqlSpecification.toSqlQuery());
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public List<CouponExtension> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<CouponExtension> list = new ArrayList<>();

        try {
            Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

            for (int i = 0, size = cursor.getCount(); i < size; i++) {
                cursor.moveToPosition(i);

                list.add(toObjectMapper.map(cursor));
            }

            cursor.close();

            return list;
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }
}
