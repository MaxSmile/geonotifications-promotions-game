package com.spindealsapp.database.repository.specification;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public interface SqlSpecification extends Specification {
    String toSqlQuery();
}
