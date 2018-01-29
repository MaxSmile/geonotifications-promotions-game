package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CursorToNotificationMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.mapper.NotificationToContentValuesMapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.table.NotificationTable;
import com.spindealsapp.entity.PlaceNotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class NotificationSqlRepository implements Repository<PlaceNotification> {

    private final Mapper<PlaceNotification, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, PlaceNotification> toObjectMapper;

    public NotificationSqlRepository() {
        this.toContentValuesMapper = new NotificationToContentValuesMapper();
        this.toObjectMapper = new CursorToNotificationMapper();
    }

    @Override
    public void add(PlaceNotification item) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = toContentValuesMapper.map(item);
        database.insert(NotificationTable.TABLE_NAME, null, contentValues);
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void add(Iterable<PlaceNotification> items) {

    }

    @Override
    public void update(PlaceNotification item) {

    }

    @Override
    public void remove(PlaceNotification item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<PlaceNotification> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<PlaceNotification> list = new ArrayList<>();

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
