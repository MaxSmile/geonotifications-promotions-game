package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.PlaceTable;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class OrderPlacesSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s ORDER BY `%2$s` DESC",
                PlaceTable.TABLE_NAME,
                PlaceTable.Fields.DISTANCE
        );
    }
}
