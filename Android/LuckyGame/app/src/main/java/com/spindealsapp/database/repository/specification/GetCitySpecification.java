package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.PlaceTable;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class GetCitySpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT DISTINCT %1$s FROM %2$s ORDER BY %1$s ASC",
                PlaceTable.Fields.CITY,
                PlaceTable.TABLE_NAME
        );
    }
}
