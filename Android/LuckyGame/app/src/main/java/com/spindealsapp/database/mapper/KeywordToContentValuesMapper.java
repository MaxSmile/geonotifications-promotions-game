package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.KeywordTable;

/**
 * Created by Volodymyr Kusenko on 15.12.2017.
 */

public class KeywordToContentValuesMapper implements Mapper<String, ContentValues> {
    @Override
    public ContentValues map(String s) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KeywordTable.Fields.KEYWORD, s);

        return contentValues;
    }
}
