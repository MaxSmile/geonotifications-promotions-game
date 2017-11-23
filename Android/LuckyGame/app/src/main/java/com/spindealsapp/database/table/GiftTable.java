package com.spindealsapp.database.table;

/**
 * Created by Volodymyr Kusenko on 22.11.2017.
 */

public class GiftTable {

    public static String TABLE_NAME = "gift";

    public static class Fields {
        public static final String ID = "id";
        public static final String COMPANY_ID = "companyId";
        public static final String DESCRIPTION = "description";
        public static final String TIME_LOCK = "timeLock";
        public static final String RULES = "rules";
        public static final String LIMIT_GIFTS = "limitGifts";
        public static final String COUNT_AVAILABLE = "countAvailable";
        public static final String SPIN_KEY = "spinKey";
        public static final String EXPIRATION_TIME = "expirationTime";
    }
}
