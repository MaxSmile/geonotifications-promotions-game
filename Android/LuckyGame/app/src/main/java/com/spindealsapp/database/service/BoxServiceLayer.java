package com.spindealsapp.database.service;

import com.spindealsapp.database.repository.BoxSqlRepository;
import com.spindealsapp.database.repository.specification.BoxByOwnerSqlSpecification;
import com.spindealsapp.entity.Box;

import java.util.List;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class BoxServiceLayer {

    private static BoxSqlRepository repository = new BoxSqlRepository();

    public static void add(Iterable<Box> boxes) {
        repository.add(boxes);
    }

    public static List<Box> getBoxesByOwner(String owner) {
        return repository.query(new BoxByOwnerSqlSpecification(owner));
    }
}
