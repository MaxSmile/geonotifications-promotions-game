package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CursorToSpinMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.mapper.SpinToContentValuesMapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.service.BoxServiceLayer;
import com.spindealsapp.database.table.SpinTable;
import com.spindealsapp.entity.Box;
import com.spindealsapp.entity.Spin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 30.01.2018.
 */

public class SpinSqlRepository implements Repository<Spin> {

    private final Mapper<Spin, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Spin> toObjectMapper;

    public SpinSqlRepository() {
        this.toContentValuesMapper = new SpinToContentValuesMapper();
        this.toObjectMapper = new CursorToSpinMapper();
    }

    @Override
    public void add(Spin item) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.insert(SpinTable.TABLE_NAME, null, toContentValuesMapper.map(item));
        DatabaseManager.getInstance().closeDatabase();
        if (item.getBox() != null) {
            addBoxes(item);
        }
    }

    @Override
    public void add(Iterable<Spin> items) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            database.delete(SpinTable.TABLE_NAME, null, null);

            for (Spin item : items) {
                database.insert(SpinTable.TABLE_NAME, null, toContentValuesMapper.map(item));
                if (item.getBox() != null) {
                    addBoxes(item);
                }
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    @Override
    public void update(Spin item) {

    }

    @Override
    public void remove(Spin item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Spin> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<Spin> list = new ArrayList<>();

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

    private void addBoxes(Spin spin) {
        List<Box> boxes = spin.getBox();
        for (Box box: boxes) {
            box.setOwner(spin.getId());
        }
        BoxServiceLayer.add(boxes);
    }
}
