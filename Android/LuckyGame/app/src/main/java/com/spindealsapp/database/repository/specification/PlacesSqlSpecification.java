package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.PlaceTable;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class PlacesSqlSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s",
                PlaceTable.TABLE_NAME
        );
    }
}
