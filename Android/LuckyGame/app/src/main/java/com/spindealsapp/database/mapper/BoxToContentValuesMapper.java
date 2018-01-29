package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.BoxTable;
import com.spindealsapp.entity.Box;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class BoxToContentValuesMapper implements Mapper<Box, ContentValues> {
    @Override
    public ContentValues map(Box box) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BoxTable.Fields.OWNER, box.getOwner());
        contentValues.put(BoxTable.Fields.COLOR, box.getColor());
        contentValues.put(BoxTable.Fields.COUNT, box.getCount());
        contentValues.put(BoxTable.Fields.GIFT, box.getGift());

        return contentValues;
    }
}
