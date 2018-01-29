package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.NotificationTable;
import com.spindealsapp.entity.PlaceNotification;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class NotificationToContentValuesMapper implements Mapper<PlaceNotification, ContentValues> {
    @Override
    public ContentValues map(PlaceNotification notification) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationTable.Fields.PLACE_ID, notification.getPlaceId());
        contentValues.put(NotificationTable.Fields.LAST_NOTIFICATION, notification.getLastNotification());

        return contentValues;
    }
}
