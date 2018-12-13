package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 6/28/17.
 */

public class StateTable implements BaseColumns {

    public static final String TABLE_NAME = "STATE";
    public static final String COLUMN_ID_STATE = "ID_STATE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_UPDATED_AT = "UPDATED_AT";

    /***
     * Foreign keys
     * ***/
    public static final String COLUMN_ID_COUNTRY = "ID_COUNTRY";
}
