package com.spindealsapp.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spindealsapp.database.DBHelper;
import com.spindealsapp.database.DatabaseManager;
import com.spindealsapp.database.mapper.CompanyToContentValuesMapper;
import com.spindealsapp.database.mapper.CursorToCompanyMapper;
import com.spindealsapp.database.mapper.Mapper;
import com.spindealsapp.database.repository.specification.Specification;
import com.spindealsapp.database.repository.specification.SqlSpecification;
import com.spindealsapp.database.table.CompanyTable;
import com.spindealsapp.entity.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public class CompanySqlRepository implements Repository<Company> {

    private final SQLiteOpenHelper openHelper;

    private final Mapper<Company, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Company> toObjectMapper;

    public CompanySqlRepository() {
        this.openHelper = DBHelper.getInstance();

        this.toContentValuesMapper = new CompanyToContentValuesMapper();
        this.toObjectMapper = new CursorToCompanyMapper();
    }

    @Override
    public void add(Company item) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = toContentValuesMapper.map(item);
        database.insert(CompanyTable.TABLE_NAME, null, contentValues);
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void add(Iterable<Company> items) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        try {
            database.delete(CompanyTable.TABLE_NAME, null, null);

            for (Company item : items) {
                ContentValues contentValues = toContentValuesMapper.map(item);
                database.insert(CompanyTable.TABLE_NAME, null, contentValues);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    @Override
    public void update(Company item) {

    }

    @Override
    public void remove(Company item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Company> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        List<Company> list = new ArrayList<>();

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
