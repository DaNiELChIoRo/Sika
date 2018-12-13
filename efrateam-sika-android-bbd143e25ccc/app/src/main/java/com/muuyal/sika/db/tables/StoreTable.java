package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 6/28/17.
 */

public class StoreTable implements BaseColumns {

    public static final String TABLE_NAME = "STORE";
    public static final String COLUMN_ID_STORE = "ID_STORE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DETAILS = "DETAILS";
    public static final String COLUMN_STREET = "STREET";
    public static final String COLUMN_EXTERNAL_NUMBER = "E_NUMBER";
    public static final String COLUMN_INTERNAL_NUMBER = "I_NUMBER";
    public static final String COLUMN_VISUAL_REFERENCE = "REFERENCE";
    public static final String COLUMN_PHONE = "PHONE";
    public static final String COLUMN_LAT = "LAT";
    public static final String COLUMN_LNG = "LNG";
    public static final String COLUMN_ADDRESS = "ADDRESS";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_UPDATED_AT = "UPDATED_AT";
    public static final String COLUMN_DISTANCE = "DISTANCE";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID_STATE = "ID_STATE";
    public static final String COLUMN_ID_PRODUCT = "ID_PRODUCT";
    public static final String COLUMN_ID_DISTRICT = "ID_PRODUCT";
}
