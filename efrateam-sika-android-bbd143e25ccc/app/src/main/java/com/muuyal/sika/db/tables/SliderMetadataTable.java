package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 7/26/17.
 */

public class SliderMetadataTable implements BaseColumns {

    public static final String TABLE_NAME = "SLIDER_METADATA";
    public static final String COLUMN_LINK = "LINK";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_ID_PRODUCT = "ID_PROD";
    public static final String COLUMN_NAME = "NAME";

    /***
     * FOREIGN KEYS
     * ***/
    public static final String COLUMN_ID = "ID";

}
