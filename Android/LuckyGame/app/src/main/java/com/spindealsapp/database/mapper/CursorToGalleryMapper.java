package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.Gallery;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class CursorToGalleryMapper implements Mapper<Cursor, Gallery> {
    @Override
    public Gallery map(Cursor cursor) {
        return new Gallery(
                cursor.getString(1),
                cursor.getString(2));
    }
}
