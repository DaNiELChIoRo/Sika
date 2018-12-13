package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class PhotoTable implements BaseColumns {

    public static final String TABLE_NAME = "PHOTO";
    public static final String COLUMN_ID_PHOTO = "ID_PHOTO";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SIZE = "SIZE";
    public static final String COLUMN_MIME = "MIME";
    public static final String COLUMN_GROUP = "_GROUP";
    public static final String COLUMN_WIDTH = "WIDTH";
    public static final String COLUMN_HEIGHT = "HEIGHT";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_UPDATED_AT = "UPDATED_AT";
    public static final String COLUMN_URL = "URL";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID_PRODUCT = "ID_PRODUCT";
}
