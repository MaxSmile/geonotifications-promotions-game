package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.GiftTable;
import com.spindealsapp.entity.Gift;

/**
 * Created by Volodymyr Kusenko on 22.11.2017.
 */

public class GiftToContentValuesMapper implements Mapper<Gift, ContentValues> {
    @Override
    public ContentValues map(Gift gift) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GiftTable.Fields.ID, gift.getId());
        contentValues.put(GiftTable.Fields.COMPANY_ID, gift.getCompanyKey());
        contentValues.put(GiftTable.Fields.DESCRIPTION, gift.getDescription());
        contentValues.put(GiftTable.Fields.TIME_LOCK, gift.getTimeLock());
        contentValues.put(GiftTable.Fields.RULES, gift.getRules());
        contentValues.put(GiftTable.Fields.LIMIT_GIFTS, gift.getLimitGifts());
        contentValues.put(GiftTable.Fields.COUNT_AVAILABLE, gift.getCountAvailable());
        contentValues.put(GiftTable.Fields.SPIN_KEY , gift.getSpinKey());
        contentValues.put(GiftTable.Fields.EXPIRATION_TIME , gift.getExpirationTime());
        return contentValues;
    }
}
