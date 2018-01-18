package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.GalleryTable;
import com.spindealsapp.entity.Gallery;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class GalleryToContentValuesMapper implements Mapper<Gallery, ContentValues> {
    @Override
    public ContentValues map(Gallery gallery) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GalleryTable.Fields.OWNER, gallery.getOwner());
        contentValues.put(GalleryTable.Fields.PATH, gallery.getPath());
        return contentValues;
    }
}
