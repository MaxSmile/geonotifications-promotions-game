package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CursorToKeywordMapper;
import com.spindealsapp.database.mapper.KeywordToContentValuesMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.table.KeywordTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 15.12.2017.
 */

public class KeywordSqlRepository implements Repository<String> {

    private final Mapper<String, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, String> toObjectMapper;

    public KeywordSqlRepository() {
        this.toContentValuesMapper = new KeywordToContentValuesMapper();
        this.toObjectMapper = new CursorToKeywordMapper();
    }

    @Override
    public void add(String item) {

    }

    @Override
    public void add(Iterable<String> items) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            database.delete(KeywordTable.TABLE_NAME, null, null);

            for (String item : items) {
                ContentValues contentValues = toContentValuesMapper.map(item);
                database.insert(KeywordTable.TABLE_NAME, null, contentValues);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    @Override
    public void update(String item) {

    }

    @Override
    public void remove(String item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<String> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<String> list = new ArrayList<>();

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
