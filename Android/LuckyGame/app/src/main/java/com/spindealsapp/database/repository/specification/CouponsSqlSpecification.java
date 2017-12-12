package com.spindealsapp.database.repository.specification;

import com.spindealsapp.Constants;
import com.spindealsapp.database.table.CouponTable;

/**
 * Created by Volodymyr Kusenko on 12.12.2017.
 */

public class CouponsSqlSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` <> '%3$s' ORDER BY %4$s DESC",
                CouponTable.TABLE_NAME,
                CouponTable.Fields.COUPON_TYPE,
                String.valueOf(Constants.COUPON_TYPE_OFFER),
                CouponTable.Fields.CREATION
        );
    }
}
