package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.Place;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class CursorToPlaceMapper implements Mapper<Cursor, Place> {
    @Override
    public Place map(Cursor cursor) {
        return new Place(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getInt(7),
                cursor.getString(8),
                cursor.getLong(9),
                cursor.getLong(10),
                cursor.getLong(11),
                cursor.getInt(12),
                cursor.getString(13),
                cursor.getInt(14),
                cursor.getString(15),
                cursor.getString(16),
                cursor.getString(17),
                cursor.getString(18),
                cursor.getString(19),
                cursor.getDouble(20),
                cursor.getString(21),
                cursor.getString(22),
                cursor.getInt(23) > 0,
                cursor.getLong(24),
                cursor.getInt(25) > 0,
                cursor.getString(26)
        );
    }
}
