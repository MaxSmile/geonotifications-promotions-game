package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.BoxTable;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class BoxByOwnerSqlSpecification implements SqlSpecification {

    private final String owner;

    public BoxByOwnerSqlSpecification(String owner) {
        this.owner = owner;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                BoxTable.TABLE_NAME,
                BoxTable.Fields.OWNER,
                owner
        );
    }
}
