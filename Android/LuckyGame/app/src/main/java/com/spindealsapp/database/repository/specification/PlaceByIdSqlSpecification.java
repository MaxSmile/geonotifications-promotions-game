package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.PlaceTable;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class PlaceByIdSqlSpecification implements SqlSpecification {

    private final String id;

    public PlaceByIdSqlSpecification(String id) {
        this.id = id;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                PlaceTable.TABLE_NAME,
                PlaceTable.Fields.ID,
                id
        );
    }
}
