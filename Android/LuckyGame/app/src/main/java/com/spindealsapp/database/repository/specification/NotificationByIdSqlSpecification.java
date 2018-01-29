package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.NotificationTable;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class NotificationByIdSqlSpecification implements SqlSpecification {

    private final String id;

    public NotificationByIdSqlSpecification(String id) {
        this.id = id;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                NotificationTable.TABLE_NAME,
                NotificationTable.Fields.PLACE_ID,
                id
        );
    }
}
