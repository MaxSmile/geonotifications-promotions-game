package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CursorToGalleryMapper;
import com.spindealsapp.database.mapper.GalleryToContentValuesMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.table.GalleryTable;
import com.spindealsapp.entity.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 17.01.2018.
 */

public class GallerySqlRepository implements Repository<Gallery> {

    private final Mapper<Gallery, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Gallery> toObjectMapper;

    public GallerySqlRepository() {
        this.toContentValuesMapper = new GalleryToContentValuesMapper();
        this.toObjectMapper = new CursorToGalleryMapper();
    }

    @Override
    public void add(Gallery item) {

    }

    @Override
    public void add(Iterable<Gallery> items) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            for (Gallery item : items) {
                ContentValues contentValues = toContentValuesMapper.map(item);
                database.insert(GalleryTable.TABLE_NAME, null, contentValues);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    @Override
    public void update(Gallery item) {

    }

    @Override
    public void remove(Gallery item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Gallery> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<Gallery> list = new ArrayList<>();

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
