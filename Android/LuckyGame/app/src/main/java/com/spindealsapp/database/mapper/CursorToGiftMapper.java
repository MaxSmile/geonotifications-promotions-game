package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.Gift;

/**
 * Created by Volodymyr Kusenko on 22.11.2017.
 */

public class CursorToGiftMapper implements Mapper<Cursor, Gift> {
    @Override
    public Gift map(Cursor cursor) {
        return new Gift(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getLong(4),
                cursor.getString(5),
                cursor.getLong(6),
                cursor.getLong(7),
                cursor.getString(8),
                cursor.getLong(9)
        );
    }
}
