package com.spindealsapp.database.mapper;

import android.database.Cursor;

/**
 * Created by Volodymyr Kusenko on 15.12.2017.
 */

public class CursorToKeywordMapper implements Mapper<Cursor, String> {
    @Override
    public String map(Cursor cursor) {
        return cursor.getString(1);
    }
}
