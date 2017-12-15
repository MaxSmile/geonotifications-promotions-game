package com.spindealsapp.database.service;

import com.spindealsapp.database.repository.KeywordSqlRepository;
import com.spindealsapp.database.repository.specification.KeywordSqlSpecification;

import java.util.List;

/**
 * Created by Volodymyr Kusenko on 15.12.2017.
 */

public class KeywordServiceLayer {

    private static KeywordSqlRepository repository = new KeywordSqlRepository();

    public static void add(Iterable<String> strings) {
        repository.add(strings);
    }

    public static List<String> getKeywords() {
        return repository.query(new KeywordSqlSpecification());
    }
}
