package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 6/28/17.
 */

public class DistrictTable implements BaseColumns {

    public static final String TABLE_NAME = "DISTRICT";
    public static final String COLUMN_ID_DISTRICT = "ID_DISTRICT";
    public static final String COLUMN_NAME = "NAME";

    /***
     * Foreign keys
     * ***/
    public static final String COLUMN_ID_STATE = "ID_STATE";
}
