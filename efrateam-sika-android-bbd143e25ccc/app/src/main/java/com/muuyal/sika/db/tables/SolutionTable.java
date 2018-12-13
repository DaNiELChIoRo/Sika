package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class SolutionTable implements BaseColumns {

    public static final String TABLE_NAME = "SOLUTION";
    public static final String COLUMN_ID_SOLUTION = "ID_SOLUTION";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_IMAGE = "IMAGE";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID_SECTION = "ID_SECTION";
}
