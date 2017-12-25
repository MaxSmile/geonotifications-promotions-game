package com.spindealsapp.database.repository.specification;

import com.spindealsapp.Constants;
import com.spindealsapp.database.table.CouponTable;

/**
 * Created by Volodymyr Kusenko on 22.12.2017.
 */

public class DeleteOldCouponSpecification implements SqlSpecification {

    private long timeLimit;

    public DeleteOldCouponSpecification() {
        this.timeLimit = System.currentTimeMillis() - (10 * Constants.DAY_TIME_SHIFT);
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "DELETE FROM %1$s WHERE `%2$s` <= %3$s OR ( `%4$s` > 0 AND `%4$s` <= %3$s )",
                CouponTable.TABLE_NAME,
                CouponTable.Fields.EXPIRED,
                this.timeLimit,
                CouponTable.Fields.REDEEMED
        );
    }
}
