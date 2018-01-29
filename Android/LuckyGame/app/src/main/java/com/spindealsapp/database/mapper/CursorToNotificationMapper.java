package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.PlaceNotification;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class CursorToNotificationMapper implements Mapper<Cursor, PlaceNotification> {
    @Override
    public PlaceNotification map(Cursor cursor) {
        return new PlaceNotification(
                cursor.getString(1),
                cursor.getLong(2)
        );
    }
}
