package com.spindealsapp.database.repository.specification;

import com.spindealsapp.database.table.PlaceTable;

/**
 * Created by Volodymyr Kusenko on 18.01.2018.
 */

public class OtherPlacesCompanySqlSpecification implements SqlSpecification {

    private final String companyId;
    private final String placeId;

    public OtherPlacesCompanySqlSpecification(String companyId, String placeId) {
        this.companyId = companyId;
        this.placeId = placeId;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE `%2$s` = '%3$s' AND `%4$s` <> '%5$s'",
                PlaceTable.TABLE_NAME,
                PlaceTable.Fields.COMPANY_KEY,
                companyId,
                PlaceTable.Fields.ID,
                placeId
        );
    }
}
