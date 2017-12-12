package com.spindealsapp.database.mapper;

import android.database.Cursor;

import com.spindealsapp.entity.CouponExtension;

/**
 * Created by Volodymyr Kusenko on 11.12.2017.
 */

public class CursorToCouponMapper implements Mapper<Cursor, CouponExtension> {
    @Override
    public CouponExtension map(Cursor cursor) {
        return new CouponExtension(
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getLong(8),
                cursor.getLong(9),
                cursor.getLong(10),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getString(13),
                cursor.getInt(14),
                cursor.getString(15),
                cursor.getDouble(16),
                cursor.getDouble(17),
                cursor.getInt(18),
                cursor.getLong(19),
                cursor.getString(20),
                cursor.getString(21),
                cursor.getInt(22),
                cursor.getString(23),
                cursor.getString(24)
        );
    }
}
