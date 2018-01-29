package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.BoxToContentValuesMapper;
import com.spindealsapp.database.mapper.CursorToBoxMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.table.BoxTable;
import com.spindealsapp.entity.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class BoxSqlRepository implements Repository<Box> {

    private final Mapper<Box, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Box> toObjectMapper;

    public BoxSqlRepository() {
        this.toContentValuesMapper = new BoxToContentValuesMapper();
        this.toObjectMapper = new CursorToBoxMapper();
    }

    @Override
    public void add(Box item) {

    }

    @Override
    public void add(Iterable<Box> items) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            boolean flag = false;
            for (Box item : items) {
                if (!flag) {
                    flag = true;
                    database.delete(BoxTable.TABLE_NAME, BoxTable.Fields.OWNER + " = ?", new String[] {item.getOwner()});
                }

                ContentValues contentValues = toContentValuesMapper.map(item);
                database.insert(BoxTable.TABLE_NAME, null, contentValues);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    @Override
    public void update(Box item) {

    }

    @Override
    public void remove(Box item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Box> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<Box> list = new ArrayList<>();

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
