package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.CouponTable;

/**
 * Created by Volodymyr Kusenko on 12.12.2017.
 */

public class CouponsByPlaceAndGiftSpecification implements SqlSpecification {

    private final String giftKey;
    private final String placeKey;

    public CouponsByPlaceAndGiftSpecification(String giftKey, String placeKey) {
        this.giftKey = giftKey;
        this.placeKey = placeKey;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s' AND `%4$s` = '%5$s'",
                CouponTable.TABLE_NAME,
                CouponTable.Fields.GIFT_KEY,
                giftKey,
                CouponTable.Fields.PLACE_KEY,
                placeKey
        );
    }
}
