package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.Spin;

/**
 * Created by Volodymyr Kusenko on 30.01.2018.
 */

public class CursorToSpinMapper implements Mapper<Cursor, Spin> {
    @Override
    public Spin map(Cursor cursor) {
        return new Spin(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getLong(5),
                cursor.getLong(6),
                cursor.getInt(7) > 0,
                cursor.getInt(8) > 0,
                cursor.getLong(9),
                cursor.getInt(10) > 0
        );
    }
}
