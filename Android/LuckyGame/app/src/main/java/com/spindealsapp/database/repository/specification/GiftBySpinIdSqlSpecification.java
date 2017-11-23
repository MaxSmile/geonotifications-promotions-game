package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.GiftTable;

/**
 * Created by Volodymyr Kusenko on 23.11.2017.
 */

public class GiftBySpinIdSqlSpecification implements SqlSpecification {

    private final String id;

    public GiftBySpinIdSqlSpecification(String id) {
        this.id = id;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                GiftTable.TABLE_NAME,
                GiftTable.Fields.SPIN_KEY,
                id
        );
    }
}
