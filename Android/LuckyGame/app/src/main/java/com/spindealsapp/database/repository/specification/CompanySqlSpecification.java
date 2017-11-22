package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.CompanyTable;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public class CompanySqlSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s",
                CompanyTable.TABLE_NAME
        );
    }
}
