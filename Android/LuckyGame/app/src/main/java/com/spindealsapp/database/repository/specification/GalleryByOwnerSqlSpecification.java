package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.GalleryTable;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class GalleryByOwnerSqlSpecification implements SqlSpecification {

    private final String owner;

    public GalleryByOwnerSqlSpecification(String owner) {
        this.owner = owner;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s'",
                GalleryTable.TABLE_NAME,
                GalleryTable.Fields.OWNER,
                owner
        );
    }
}
