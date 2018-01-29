package com.spindealsapp.database.table;

/**
 * Created by Volodymyr Kusenko on 29.01.2018.
 */

public class BoxTable {
    public static String TABLE_NAME = "box";

    public static class Fields {
        public static final String OWNER = "spinId";
        public static final String COLOR = "color";
        public static final String COUNT = "count";
        public static final String GIFT = "gift";
    }
}
