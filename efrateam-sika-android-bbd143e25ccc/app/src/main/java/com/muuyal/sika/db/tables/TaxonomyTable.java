package com.muuyal.sika.db.tables;

import android.provider.BaseColumns;

/**
 * Created by isra on 6/28/17.
 */

public class TaxonomyTable implements BaseColumns {

    public static final String TABLE_NAME = "TAXONOMY";
    public static final String COLUMN_ID_TAXONOMY = "ID_TAXONOMY";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_VALUE = "VALUE";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_UPDATED_AT = "UPDATED_AT";
    public static final String COLUMN_ID_PARENT = "ID_PARENT";

}
