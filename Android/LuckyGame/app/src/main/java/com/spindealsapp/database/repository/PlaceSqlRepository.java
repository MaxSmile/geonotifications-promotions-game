package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CursorToPlaceMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.mapper.PlaceToContentValuesMapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.service.GalleryServiceLayer;
import com.spindealsapp.database.table.PlaceTable;
import com.spindealsapp.entity.Gallery;
import com.spindealsapp.entity.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class PlaceSqlRepository implements Repository<Place> {

    private final Mapper<Place, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Place> toObjectMapper;

    public PlaceSqlRepository() {
        this.toContentValuesMapper = new PlaceToContentValuesMapper();
        this.toObjectMapper = new CursorToPlaceMapper();
    }

    @Override
    public void add(Place item) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.insert(PlaceTable.TABLE_NAME, null, toContentValuesMapper.map(item));
        DatabaseManager.getInstance().closeDatabase();
        if (item.getGallery() != null) {
            addGallery(item);
        }
    }

    @Override
    public void add(Iterable<Place> items) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            database.delete(PlaceTable.TABLE_NAME, null, null);

            for (Place item : items) {
                database.insert(PlaceTable.TABLE_NAME, null, toContentValuesMapper.map(item));
                if (item.getGallery() != null) {
                    addGallery(item);
                }
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    @Override
    public void update(Place item) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.update(PlaceTable.TABLE_NAME, toContentValuesMapper.map(item), PlaceTable.Fields.ID + " = ?", new String[] {item.getId()});
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void remove(Place item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Place> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<Place> list = new ArrayList<>();

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

    public List<String> customQuery(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<String> list = new ArrayList<>();

        try {
            Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

            for (int i = 0, size = cursor.getCount(); i < size; i++) {
                cursor.moveToPosition(i);

                list.add(cursor.getString(0));
            }

            cursor.close();

            return list;
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private void addGallery(Place place) {
        List<Gallery> gallery = new ArrayList<>();
        List<String> list = place.getGallery();
        for (String s: list) {
            gallery.add(new Gallery(place.getId(), s));
        }
        GalleryServiceLayer.add(gallery);
    }

}
