package com.spindealsapp.database.repository.specification;

import com.spindealsapp.Constants;
import com.spindealsapp.database.table.CouponTable;

/**
 * Created by Volodymyr Kusenko on 11.12.2017.
 */

public class DeleteOfferSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "DELETE FROM %1$s WHERE `%2$s` = %3$s",
                CouponTable.TABLE_NAME,
                CouponTable.Fields.COUPON_TYPE,
                String.valueOf(Constants.COUPON_TYPE_OFFER)
        );
    }
}
