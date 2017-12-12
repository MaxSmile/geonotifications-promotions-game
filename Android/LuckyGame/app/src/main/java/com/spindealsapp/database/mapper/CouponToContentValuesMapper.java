package com.spindealsapp.database.mapper;

import android.content.ContentValues;

import com.spindealsapp.database.table.CouponTable;
import com.spindealsapp.entity.CouponExtension;

/**
 * Created by Volodymyr Kusenko on 11.12.2017.
 */

public class CouponToContentValuesMapper implements Mapper<CouponExtension, ContentValues> {
    @Override
    public ContentValues map(CouponExtension coupon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CouponTable.Fields.STATUS, coupon.getStatus());
        contentValues.put(CouponTable.Fields.CODE, coupon.getCode());
        contentValues.put(CouponTable.Fields.COMPANY_KEY, coupon.getCompanyKey());
        contentValues.put(CouponTable.Fields.GIFT_KEY, coupon.getGiftKey());
        contentValues.put(CouponTable.Fields.PLACE_KEY , coupon.getPlaceKey());
        contentValues.put(CouponTable.Fields.DESCRIPTION, coupon.getDescription());
        contentValues.put(CouponTable.Fields.CREATOR, coupon.getCreator());
        contentValues.put(CouponTable.Fields.CREATION, coupon.getCreation());
        contentValues.put(CouponTable.Fields.EXPIRED, coupon.getExpired());
        contentValues.put(CouponTable.Fields.LOCKS, coupon.getLocks());
        contentValues.put(CouponTable.Fields.COMPANY_NAME, coupon.getCompanyName());
        contentValues.put(CouponTable.Fields.PLACE_NAME, coupon.getPlaceName());
        contentValues.put(CouponTable.Fields.LOGO, coupon.getLogo());
        contentValues.put(CouponTable.Fields.TYPE, coupon.getType());
        contentValues.put(CouponTable.Fields.TYPE_STRING, coupon.getTypeString());
        contentValues.put(CouponTable.Fields.GEO_LAT, coupon.getGeoLat());
        contentValues.put(CouponTable.Fields.GEO_LON, coupon.getGeoLon());
        contentValues.put(CouponTable.Fields.LOCKED, coupon.getLocked());
        contentValues.put(CouponTable.Fields.REDEEMED, coupon.getRedeemed());
        contentValues.put(CouponTable.Fields.CITY, coupon.getCity());
        contentValues.put(CouponTable.Fields.RULES, coupon.getRules());
        contentValues.put(CouponTable.Fields.COUPON_TYPE, coupon.getCouponType());
        contentValues.put(CouponTable.Fields.KEYWORDS, coupon.getKeywords());
        contentValues.put(CouponTable.Fields.RRULE, coupon.getRrule());

        return contentValues;
    }
}
