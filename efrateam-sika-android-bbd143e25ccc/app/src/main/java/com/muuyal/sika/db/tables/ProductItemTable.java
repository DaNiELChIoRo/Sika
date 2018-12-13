package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class ProductItemTable implements BaseColumns {

    public static final String TABLE_NAME = "PRODUCT_ITEM";
    public static final String COLUMN_ID_PRODUCT = "ID_PRODUCT";
    public static final String COLUMN_ID_PROD = "ID_PROD";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_IMAGE = "IMAGE";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID = "ID";
}
