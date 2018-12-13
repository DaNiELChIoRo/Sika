package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class ProductRelatedTable implements BaseColumns {

    public static final String TABLE_NAME = "PRODUCT_RELATED";
    public static final String COLUMN_ID_RELATED = "ID_RELATED";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_PHOTO = "PHOTO";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID_PRODUCT = "ID_PRODUCT";
}
