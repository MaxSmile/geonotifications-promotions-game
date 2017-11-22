package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.CompanyTable;
import com.spindealsapp.entity.Company;

/**
 * Created by Volodymyr Kusenko on 22.11.2017.
 */

public class CompanyByIdSqlSpecification implements SqlSpecification {

    private final String id;

    public CompanyByIdSqlSpecification(final String id) {
        this.id = id;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                CompanyTable.TABLE_NAME,
                CompanyTable.Fields.ID,
                id
        );
    }
}
