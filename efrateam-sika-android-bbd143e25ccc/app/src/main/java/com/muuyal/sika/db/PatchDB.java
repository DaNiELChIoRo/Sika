package com.muuyal.sika.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by SOFTCO on 1/4/2017.
 */

public class PatchDB {
    public void apply(Context context, SQLiteDatabase db) {
    }

    @SuppressWarnings("unused")
    public void revert(SQLiteDatabase db) {
    }

    public boolean automaticallyCloseDb() {
        return true;
    }
}
