package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.Box;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class CursorToBoxMapper implements Mapper<Cursor, Box> {
    @Override
    public Box map(Cursor cursor) {
        return new Box(
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getString(4)
        );
    }
}
