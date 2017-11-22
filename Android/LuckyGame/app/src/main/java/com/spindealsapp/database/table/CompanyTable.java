package com.spindealsapp.database.table;

/**
 * Created by Volodymyr Kusenko on 21.11.2017.
 */

public class CompanyTable {
    public static String TABLE_NAME = "companies";

    public static class Fields {
        public static final String ID = "companyId";
        public static final String NAME = "name";
        public static final String INFO = "info";
        public static final String LOGO = "logo";
        public static final String FACEBOOK_URL = "facebookUrl";
        public static final String TYPE = "type";
    }
}
