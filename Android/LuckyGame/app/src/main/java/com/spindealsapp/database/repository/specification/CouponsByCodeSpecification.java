package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.CouponTable;

/**
 * Created by Volodymyr Kusenko on 12.12.2017.
 */

public class CouponsByCodeSpecification implements SqlSpecification {

    private final String code;

    public CouponsByCodeSpecification(String code) {
        this.code = code;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                CouponTable.TABLE_NAME,
                CouponTable.Fields.CODE,
                code
        );
    }
}
