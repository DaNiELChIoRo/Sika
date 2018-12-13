package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class ItemTable implements BaseColumns {

    public static final String TABLE_NAME = "ITEM";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_ID_ITEM = "ID_ITEM";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_CAPTION = "CAPTION";
    public static final String COLUMN_CAUSE = "CAUSE";
    public static final String COLUMN_GLOSSARY = "GLOSSARY";
    public static final String COLUMN_DEMO = "DEMO";
    public static final String COLUMN_TYPE = "TYPE";


    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID_SOLUTION = "ID_SOLUTION";
}
