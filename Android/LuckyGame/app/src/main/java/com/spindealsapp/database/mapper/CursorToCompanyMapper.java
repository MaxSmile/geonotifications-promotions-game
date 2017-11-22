package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.Company;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public class CursorToCompanyMapper implements Mapper<Cursor, Company> {
    @Override
    public Company map(Cursor cursor) {
        return new Company(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6)
        );
    }
}
