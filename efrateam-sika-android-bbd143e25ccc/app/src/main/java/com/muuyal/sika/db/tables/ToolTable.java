package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class ToolTable implements BaseColumns {

    public static final String TABLE_NAME = "TOOLS";
    public static final String COLUMN_ID_TOOL = "ID_TOOL";
    public static final String COLUMN_NAME = "NAME";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID_PRODUCT = "ID_PRODUCT";
}
