package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 6/28/17.
 */

public class ProductTable implements BaseColumns {

    public static final String TABLE_NAME = "PRODUCT";
    public static final String COLUMN_ID_PRODUCT = "ID_PRODUCT";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DETAILS = "DETAILS";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_DELETED_AT = "DELETED_AT";

    /***
     * Foreign Keys
     * ***/
    public static final String COLUMN_ID_TAXONOMY = "ID_TAXONOMY";
}
