package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.KeywordTable;

/**
 * Created by Volodymyr Kusenko on 15.12.2017.
 */

public class KeywordSqlSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s",
                KeywordTable.TABLE_NAME
        );
    }
}
