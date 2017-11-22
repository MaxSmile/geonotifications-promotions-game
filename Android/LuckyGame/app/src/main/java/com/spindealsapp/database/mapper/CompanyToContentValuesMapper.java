package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.CompanyTable;
import com.spindealsapp.entity.Company;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public class CompanyToContentValuesMapper implements Mapper<Company, ContentValues> {
    @Override
    public ContentValues map(Company company) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CompanyTable.Fields.ID, company.getId());
        contentValues.put(CompanyTable.Fields.NAME, company.getName());
        contentValues.put(CompanyTable.Fields.INFO, company.getInfo());
        contentValues.put(CompanyTable.Fields.LOGO, company.getLogo());
        contentValues.put(CompanyTable.Fields.FACEBOOK_URL, company.getFacebookUrl());
        contentValues.put(CompanyTable.Fields.TYPE, company.getType());

        return contentValues;
    }
}
