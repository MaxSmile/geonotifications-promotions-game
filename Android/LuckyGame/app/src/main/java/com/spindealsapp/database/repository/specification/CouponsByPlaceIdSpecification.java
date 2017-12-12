package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.CouponTable;

/**
 * Created by Volodymyr Kusenko on 12.12.2017.
 */

public class CouponsByPlaceIdSpecification implements SqlSpecification {

    private final String id;

    public CouponsByPlaceIdSpecification(String id) {
        this.id = id;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s' ORDER BY %4$s DESC",
                CouponTable.TABLE_NAME,
                CouponTable.Fields.PLACE_KEY,
                id,
                CouponTable.Fields.CREATION
        );
    }
}
