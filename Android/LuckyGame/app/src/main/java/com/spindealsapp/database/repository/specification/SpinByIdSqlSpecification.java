package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.SpinTable;

/**
 * Created by Volodymyr Kusenko on 30.01.2018.
 */

public class SpinByIdSqlSpecification implements SqlSpecification {

    private final String id;

    public SpinByIdSqlSpecification(String id) {
        this.id = id;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                SpinTable.TABLE_NAME,
                SpinTable.Fields.ID,
                id
        );
    }
}
