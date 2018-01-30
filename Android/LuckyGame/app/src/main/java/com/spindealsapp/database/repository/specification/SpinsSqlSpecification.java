package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.SpinTable;

/**
 * Created by Volodymyr Kusenko on 30.01.2018.
 */

public class SpinsSqlSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s ORDER BY %2$s DESC",
                SpinTable.TABLE_NAME,
                SpinTable.Fields.ID
        );
    }
}
