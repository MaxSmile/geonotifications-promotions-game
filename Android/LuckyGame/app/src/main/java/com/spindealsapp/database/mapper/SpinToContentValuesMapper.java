package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.SpinTable;
import com.spindealsapp.entity.Spin;

/**
 * Created by Volodymyr Kusenko on 30.01.2018.
 */

public class SpinToContentValuesMapper implements Mapper<Spin, ContentValues> {
    @Override
    public ContentValues map(Spin spin) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SpinTable.Fields.ID, spin.getId());
        contentValues.put(SpinTable.Fields.COMPANY_KEY, spin.getCompanyKey());
        contentValues.put(SpinTable.Fields.PLACE_KEY, spin.getPlaceKey());
        contentValues.put(SpinTable.Fields.LIMIT, spin.getLimit());
        contentValues.put(SpinTable.Fields.RRULE, spin.getRrule());
        contentValues.put(SpinTable.Fields.SPENT, spin.getSpent());
        contentValues.put(SpinTable.Fields.AVAILABLE, spin.isAvailable() ? 1 : 0);
        contentValues.put(SpinTable.Fields.EXTRA_AVAILABLE, spin.isExtraAvailable() ? 1 : 0);
        contentValues.put(SpinTable.Fields.EXTRA_CREATE_TIME, spin.getExtraCreateTime());
        contentValues.put(SpinTable.Fields.EXTRA, spin.isExtra() ? 1 : 0);

        return contentValues;
    }
}
