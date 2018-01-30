package com.spindealsapp.database.table;

/**
 * Created by Volodymyr Kusenko on 30.01.2018.
 */

public class SpinTable {

    public static String TABLE_NAME = "spin";

    public static class Fields {
        public static final String ID = "id";
        public static final String COMPANY_KEY = "companyKey";
        public static final String PLACE_KEY = "placeKey";
        public static final String LIMIT = "spinLimit";
        public static final String RRULE = "rrule";
        public static final String SPENT = "spent";
        public static final String AVAILABLE = "available";
        public static final String EXTRA_AVAILABLE = "extraAvailable";
        public static final String EXTRA = "extra";
        public static final String EXTRA_CREATE_TIME = "extraCreateTime";
    }
}