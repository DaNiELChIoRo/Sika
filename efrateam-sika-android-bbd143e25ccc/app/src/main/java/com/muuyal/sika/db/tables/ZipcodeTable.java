package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 6/28/17.
 */

public class ZipcodeTable implements BaseColumns {

    public static final String TABLE_NAME = "ZIPCODE";
    public static final String COLUMN_ID_ZIPCODE = "ID_ZIPCODE";
    public static final String COLUMN_ZIPCODE = "ZIPCODE";
    public static final String COLUMN_DISTRICT = "DISTRICT";
    public static final String COLUMN_SUBURB = "SUBURB";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_UPDATED_AT = "UPDATED_AT";

    /***
     * Foreign keys
     * ***/
    public static final String COLUMN_ID_STATE = "ID_STATE";
}
