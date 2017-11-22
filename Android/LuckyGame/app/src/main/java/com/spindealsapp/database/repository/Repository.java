package com.spindealsapp.database.repository;

import com.spindealsapp.database.repository.specification.Specification;

import java.util.List;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public interface Repository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    void remove(Specification specification);

    List<T> query(Specification specification);
}
