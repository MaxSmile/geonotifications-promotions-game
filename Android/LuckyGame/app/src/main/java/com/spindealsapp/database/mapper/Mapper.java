package com.spindealsapp.database.mapper;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public interface Mapper<From, To> {
    To map(From from);
}
