package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class StepTable implements BaseColumns {

    public static final String TABLE_NAME = "STEPS";
    public static final String COLUMN_ID_STEP = "ID_STEP";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_CAPTION = "CAPTION";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID = "ID";
}
