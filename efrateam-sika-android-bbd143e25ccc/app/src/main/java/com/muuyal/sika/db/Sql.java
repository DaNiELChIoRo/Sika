package com.muuyal.sika.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.muuyal.sika.db.tables.CompanyTable;
import com.muuyal.sika.db.tables.CountryTable;
import com.muuyal.sika.db.tables.DistrictTable;
import com.muuyal.sika.db.tables.GlossaryTable;
import com.muuyal.sika.db.tables.ItemTable;
import com.muuyal.sika.db.tables.PhotoTable;
import com.muuyal.sika.db.tables.ProductItemTable;
import com.muuyal.sika.db.tables.ProductRelatedTable;
import com.muuyal.sika.db.tables.ProductTable;
import com.muuyal.sika.db.tables.PromotionTable;
import com.muuyal.sika.db.tables.SliderItemTable;
import com.muuyal.sika.db.tables.SliderMetadataTable;
import com.muuyal.sika.db.tables.SolutionSectionTable;
import com.muuyal.sika.db.tables.SolutionTable;
import com.muuyal.sika.db.tables.StateTable;
import com.muuyal.sika.db.tables.StepTable;
import com.muuyal.sika.db.tables.StoreTable;
import com.muuyal.sika.db.tables.SuggestionTable;
import com.muuyal.sika.db.tables.TaxonomyTable;
import com.muuyal.sika.db.tables.ThumbnailTable;
import com.muuyal.sika.db.tables.ToolTable;
import com.muuyal.sika.db.tables.ZipcodeTable;

/**
 * Created by Isra on 5/22/2017.
 */

public class Sql {

    /*
    public static final String SQL_CREATE_USER =
            "CREATE TABLE IF NOT EXISTS " + UserTable.TABLE_NAME + " (" +
                    UserTable.COLUMN_ID_USER + " TEXT PRIMARY KEY NOT NULL, " +
                    UserTable.COLUMN_NAME + " TEXT," +
                    UserTable.COLUMN_EMAIL + " TEXT NOT NULL," +
                    UserTable.COLUMN_PASSWORD + " TEXT NOT NULL" +
                    ")";
    */

    public static final String SQL_CREATE_COUNTRY =
            "CREATE TABLE IF NOT EXISTS " + CountryTable.TABLE_NAME + " (" +
                    CountryTable.COLUMN_ID_COUNTRY + " INTEGER PRIMARY KEY NOT NULL, " +
                    CountryTable.COLUMN_NAME + " TEXT NOT NULL," +
                    CountryTable.COLUMN_CODE + " TEXT NOT NULL," +
                    CountryTable.COLUMN_CREATED_AT + " TEXT," +
                    CountryTable.COLUMN_UPDATED_AT + " TEXT" +
                    ")";

    public static final String SQL_CREATE_STATE =
            "CREATE TABLE IF NOT EXISTS " + StateTable.TABLE_NAME + " (" +
                    StateTable.COLUMN_ID_STATE + " INTEGER PRIMARY KEY NOT NULL, " +
                    StateTable.COLUMN_NAME + " TEXT NOT NULL," +
                    StateTable.COLUMN_CREATED_AT + " TEXT," +
                    StateTable.COLUMN_UPDATED_AT + " TEXT," +
                    StateTable.COLUMN_ID_COUNTRY + " INTEGER NOT NULL" +
                    //"FOREIGN KEY(" + StateTable.COLUMN_ID_COUNTRY + ") REFERENCES " + CountryTable.TABLE_NAME + "(" + CountryTable.COLUMN_ID_COUNTRY + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_ZIPCODE =
            "CREATE TABLE IF NOT EXISTS " + ZipcodeTable.TABLE_NAME + " (" +
                    ZipcodeTable.COLUMN_ID_ZIPCODE + " INTEGER PRIMARY KEY NOT NULL, " +
                    ZipcodeTable.COLUMN_ZIPCODE + " TEXT NOT NULL," +
                    ZipcodeTable.COLUMN_DISTRICT + " TEXT," +
                    ZipcodeTable.COLUMN_SUBURB + " TEXT," +
                    ZipcodeTable.COLUMN_CREATED_AT + " TEXT," +
                    ZipcodeTable.COLUMN_UPDATED_AT + " TEXT," +
                    ZipcodeTable.COLUMN_ID_STATE + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + ZipcodeTable.COLUMN_ID_STATE + ") REFERENCES " + StateTable.TABLE_NAME + "(" + StateTable.COLUMN_ID_STATE + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_TAXONOMY =
            "CREATE TABLE IF NOT EXISTS " + TaxonomyTable.TABLE_NAME + " (" +
                    TaxonomyTable.COLUMN_ID_TAXONOMY + " INTEGER PRIMARY KEY NOT NULL, " +
                    TaxonomyTable.COLUMN_TYPE + " TEXT NOT NULL," +
                    TaxonomyTable.COLUMN_VALUE + " TEXT NOT NULL," +
                    TaxonomyTable.COLUMN_CREATED_AT + " TEXT," +
                    TaxonomyTable.COLUMN_UPDATED_AT + " TEXT," +
                    TaxonomyTable.COLUMN_ID_PARENT + " INTEGER" +
                    ")";

    public static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE IF NOT EXISTS " + ProductTable.TABLE_NAME + " (" +
                    ProductTable.COLUMN_ID_PRODUCT + " INTEGER PRIMARY KEY NOT NULL, " +
                    ProductTable.COLUMN_NAME + " TEXT NOT NULL," +
                    ProductTable.COLUMN_DETAILS + " TEXT," +
                    ProductTable.COLUMN_CREATED_AT + " TEXT," +
                    ProductTable.COLUMN_DELETED_AT + " TEXT," +
                    ProductTable.COLUMN_ID_TAXONOMY + " INTEGER" +
                    ")";

    public static final String SQL_CREATE_STORE =
            "CREATE TABLE IF NOT EXISTS " + StoreTable.TABLE_NAME + " (" +
                    StoreTable.COLUMN_ID_STORE + " INTEGER PRIMARY KEY NOT NULL, " +
                    StoreTable.COLUMN_NAME + " TEXT NOT NULL," +
                    StoreTable.COLUMN_DETAILS + " TEXT," +
                    StoreTable.COLUMN_STREET + " TEXT NOT NULL," +
                    StoreTable.COLUMN_EXTERNAL_NUMBER + " TEXT," +
                    StoreTable.COLUMN_INTERNAL_NUMBER + " TEXT," +
                    StoreTable.COLUMN_VISUAL_REFERENCE + " TEXT," +
                    StoreTable.COLUMN_PHONE + " TEXT," +
                    StoreTable.COLUMN_LAT + " REAL," +
                    StoreTable.COLUMN_LNG + " REAL," +
                    StoreTable.COLUMN_ADDRESS + " TEXT," +
                    StoreTable.COLUMN_DISTANCE + " REAL," +
                    StoreTable.COLUMN_CREATED_AT + " TEXT," +
                    StoreTable.COLUMN_UPDATED_AT + " TEXT," +
                    StoreTable.COLUMN_ID_STATE + " INTEGER," +
                    StoreTable.COLUMN_ID_PRODUCT + " INTEGER" +
                    ")";

    public static final String SQL_CREATE_COMPANY =
            "CREATE TABLE IF NOT EXISTS " + CompanyTable.TABLE_NAME + " (" +
                    CompanyTable.COLUMN_ID_COMPANY + " INTEGER PRIMARY KEY NOT NULL, " +
                    CompanyTable.COLUMN_NAME + " TEXT NOT NULL," +
                    CompanyTable.COLUMN_DETAILS + " TEXT," +
                    CompanyTable.COLUMN_CREATED_AT + " TEXT," +
                    CompanyTable.COLUMN_UPDATED_AT + " TEXT" +
                    ")";

    public static final String SQL_CREATE_SUGGESTIONS =
            "CREATE TABLE IF NOT EXISTS " + SuggestionTable.TABLE_NAME + " (" +
                    SuggestionTable.COLUMN_ID_SUGGESTION + " INTEGER PRIMARY KEY NOT NULL, " +
                    SuggestionTable.COLUMN_TITLE + " TEXT," +
                    SuggestionTable.COLUMN_COLOR + " TEXT" +
                    ")";

    public static final String SQL_CREATE_SOLUTION_SECTIONS =
            "CREATE TABLE IF NOT EXISTS " + SolutionSectionTable.TABLE_NAME + " (" +
                    SolutionSectionTable.COLUMN_ID_SECTION + " INTEGER PRIMARY KEY NOT NULL, " +
                    SolutionSectionTable.COLUMN_TITLE + " TEXT" +
                    ")";

    public static final String SQL_CREATE_SOLUTIONS =
            "CREATE TABLE IF NOT EXISTS " + SolutionTable.TABLE_NAME + " (" +
                    SolutionTable.COLUMN_ID_SOLUTION + " INTEGER PRIMARY KEY NOT NULL, " +
                    SolutionTable.COLUMN_TITLE + " TEXT," +
                    SolutionTable.COLUMN_IMAGE + " TEXT," +
                    SolutionTable.COLUMN_ID_SECTION + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + SolutionTable.COLUMN_ID_SECTION + ") REFERENCES " + SolutionSectionTable.TABLE_NAME + "(" + SolutionSectionTable.COLUMN_ID_SECTION + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_ITEMS =
            "CREATE TABLE IF NOT EXISTS " + ItemTable.TABLE_NAME + " (" +
                    ItemTable.COLUMN_ID + " TEXT PRIMARY KEY NOT NULL, " +
                    ItemTable.COLUMN_ID_ITEM + " INTEGER, " +
                    ItemTable.COLUMN_ID_SOLUTION + " INTEGER NOT NULL," +
                    ItemTable.COLUMN_TITLE + " TEXT," +
                    ItemTable.COLUMN_IMAGE + " TEXT," +
                    ItemTable.COLUMN_CAPTION + " TEXT," +
                    ItemTable.COLUMN_GLOSSARY + " TEXT," +
                    ItemTable.COLUMN_DEMO + " TEXT," +
                    ItemTable.COLUMN_TYPE + " TEXT," +
                    "FOREIGN KEY(" + ItemTable.COLUMN_ID_SOLUTION + ") REFERENCES " + SolutionTable.TABLE_NAME + "(" + SolutionTable.COLUMN_ID_SOLUTION + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_PRODUCTS_BY_ITEM =
            "CREATE TABLE IF NOT EXISTS " + ProductItemTable.TABLE_NAME + " (" +
                    ProductItemTable.COLUMN_ID_PRODUCT + " TEXT PRIMARY KEY NOT NULL, " +
                    ProductItemTable.COLUMN_ID + " TEXT NOT NULL," +
                    ProductItemTable.COLUMN_ID_PROD + " INTEGER," +
                    ProductItemTable.COLUMN_TITLE + " INTEGER," +
                    ProductItemTable.COLUMN_IMAGE + " TEXT," +
                    "FOREIGN KEY(" + ProductItemTable.COLUMN_ID + ") REFERENCES " + ItemTable.TABLE_NAME + "(" + ItemTable.COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_STEPS =
            "CREATE TABLE IF NOT EXISTS " + StepTable.TABLE_NAME + " (" +
                    StepTable.COLUMN_ID_STEP + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    StepTable.COLUMN_ID + " TEXT NOT NULL," +
                    StepTable.COLUMN_IMAGE + " TEXT," +
                    StepTable.COLUMN_CAPTION + " TEXT," +
                    "FOREIGN KEY(" + StepTable.COLUMN_ID + ") REFERENCES " + ItemTable.TABLE_NAME + "(" + ItemTable.COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_PROMOTIONS =
            "CREATE TABLE IF NOT EXISTS " + PromotionTable.TABLE_NAME + " (" +
                    PromotionTable.COLUMN_PROMOTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PromotionTable.COLUMN_IMAGE + " TEXT," +
                    PromotionTable.COLUMN_DESCRIPTION + " TEXT," +
                    PromotionTable.COLUMN_LINK + " TEXT" +
                    ")";

    public static final String SQL_CREATE_SLIDER_ITEMS =
            "CREATE TABLE IF NOT EXISTS " + SliderItemTable.TABLE_NAME + " (" +
                    SliderItemTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SliderItemTable.COLUMN_IMAGE + " TEXT," +
                    SliderItemTable.COLUMN_TYPE + " TEXT" +
                    ")";

    public static final String SQL_CREATE_SLIDER_METADATA =
            "CREATE TABLE IF NOT EXISTS " + SliderMetadataTable.TABLE_NAME + " (" +
                    SliderMetadataTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SliderMetadataTable.COLUMN_LINK + " TEXT," +
                    SliderMetadataTable.COLUMN_DESCRIPTION + " TEXT," +
                    SliderMetadataTable.COLUMN_ID_PRODUCT + " TEXT," +
                    SliderMetadataTable.COLUMN_NAME + " TEXT," +
                    SliderMetadataTable.COLUMN_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + SliderMetadataTable.COLUMN_ID + ") REFERENCES " + SliderItemTable.TABLE_NAME + "(" + SliderItemTable.COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_PHOTOS =
            "CREATE TABLE IF NOT EXISTS " + PhotoTable.TABLE_NAME + " (" +
                    PhotoTable.COLUMN_ID_PHOTO + " INTEGER PRIMARY KEY NOT NULL, " +
                    PhotoTable.COLUMN_ID_PRODUCT + " INTEGER NOT NULL," +
                    PhotoTable.COLUMN_NAME + " TEXT," +
                    PhotoTable.COLUMN_SIZE + " INTEGER," +
                    PhotoTable.COLUMN_MIME + " TEXT," +
                    PhotoTable.COLUMN_GROUP + " TEXT," +
                    PhotoTable.COLUMN_WIDTH + " INTEGER," +
                    PhotoTable.COLUMN_HEIGHT + " INTEGER," +
                    PhotoTable.COLUMN_CREATED_AT + " TEXT," +
                    PhotoTable.COLUMN_UPDATED_AT + " TEXT," +
                    PhotoTable.COLUMN_URL + " TEXT," +
                    "FOREIGN KEY(" + PhotoTable.COLUMN_ID_PRODUCT + ") REFERENCES " + ProductTable.TABLE_NAME + "(" + ProductTable.COLUMN_ID_PRODUCT + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_THUMBNAILS =
            "CREATE TABLE IF NOT EXISTS " + ThumbnailTable.TABLE_NAME + " (" +
                    ThumbnailTable.COLUMN_ID_THUMBNAIL + " INTEGER PRIMARY KEY NOT NULL, " +
                    ThumbnailTable.COLUMN_ID_PHOTO + " INTEGER NOT NULL," +
                    ThumbnailTable.COLUMN_NAME + " TEXT," +
                    ThumbnailTable.COLUMN_SIZE + " INTEGER," +
                    ThumbnailTable.COLUMN_MIME + " TEXT," +
                    ThumbnailTable.COLUMN_GROUP + " TEXT," +
                    ThumbnailTable.COLUMN_WIDTH + " INTEGER," +
                    ThumbnailTable.COLUMN_HEIGHT + " INTEGER," +
                    ThumbnailTable.COLUMN_CREATED_AT + " TEXT," +
                    ThumbnailTable.COLUMN_UPDATED_AT + " TEXT," +
                    ThumbnailTable.COLUMN_URL + " TEXT," +
                    "FOREIGN KEY(" + ThumbnailTable.COLUMN_ID_PHOTO + ") REFERENCES " + PhotoTable.TABLE_NAME + "(" + PhotoTable.COLUMN_ID_PHOTO + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_PRODUCT_RELATED =
            "CREATE TABLE IF NOT EXISTS " + ProductRelatedTable.TABLE_NAME + " (" +
                    ProductRelatedTable.COLUMN_ID_RELATED + " TEXT PRIMARY KEY NOT NULL, " +
                    ProductRelatedTable.COLUMN_ID_PRODUCT + " INTEGER NOT NULL," +
                    ProductRelatedTable.COLUMN_ID + " INTEGER NOT NULL," +
                    ProductRelatedTable.COLUMN_NAME + " TEXT," +
                    ProductRelatedTable.COLUMN_PHOTO + " TEXT," +
                    "FOREIGN KEY(" + ProductRelatedTable.COLUMN_ID_PRODUCT + ") REFERENCES " + ProductTable.TABLE_NAME + "(" + ProductTable.COLUMN_ID_PRODUCT + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_TOOLS =
            "CREATE TABLE IF NOT EXISTS " + ToolTable.TABLE_NAME + " (" +
                    ToolTable.COLUMN_ID_TOOL + " TEXT PRIMARY KEY NOT NULL, " +
                    ToolTable.COLUMN_ID_PRODUCT + " INTEGER NOT NULL," +
                    ToolTable.COLUMN_NAME + " TEXT," +
                    "FOREIGN KEY(" + ToolTable.COLUMN_ID_PRODUCT + ") REFERENCES " + ProductTable.TABLE_NAME + "(" + ProductTable.COLUMN_ID_PRODUCT + ") ON DELETE CASCADE" +
                    ")";

    public static final String SQL_CREATE_GLOSSARY =
            "CREATE TABLE IF NOT EXISTS " + GlossaryTable.TABLE_NAME + " (" +
                    GlossaryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GlossaryTable.COLUMN_TITLE + " TEXT," +
                    GlossaryTable.COLUMN_DESCRIPTION + " TEXT" +
                    ")";

    public static final String SQL_CREATE_DISTRICTS =
            "CREATE TABLE IF NOT EXISTS " + DistrictTable.TABLE_NAME + " (" +
                    DistrictTable.COLUMN_ID_DISTRICT + " TEXT PRIMARY KEY NOT NULL, " +
                    DistrictTable.COLUMN_NAME + " TEXT," +
                    DistrictTable.COLUMN_ID_STATE + " INTEGER," +
                    "FOREIGN KEY(" + DistrictTable.COLUMN_ID_STATE + ") REFERENCES " + StateTable.TABLE_NAME + "(" + StateTable.COLUMN_ID_STATE + ") ON DELETE CASCADE" +
                    ")";

    /***
     * HERE DEFINE SPECIAL QUERIES
     ***/

    /***
     * Here define the Patches DB for version in PROD
     ***/
    public static final PatchDB[] PATCHES_DB = new PatchDB[]{
            new PatchDB() {//PATCH V2
                @Override
                public void apply(Context context, SQLiteDatabase db) {
                    db.execSQL("ALTER TABLE " + TaxonomyTable.TABLE_NAME + " ADD " + TaxonomyTable.COLUMN_ID_PARENT + " INTEGER;");
                }
            },
            new PatchDB() {//PATCH V3
                @Override
                public void apply(Context context, SQLiteDatabase db) {
                    //Delete old tables
                    db.execSQL("DROP TABLE IF EXISTS " + StepTable.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + ProductItemTable.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + ItemTable.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + SolutionTable.TABLE_NAME);

                    //Create new tables
                    db.execSQL(Sql.SQL_CREATE_SOLUTION_SECTIONS);
                    db.execSQL(Sql.SQL_CREATE_SOLUTIONS);
                    db.execSQL(Sql.SQL_CREATE_ITEMS);
                    db.execSQL(Sql.SQL_CREATE_PRODUCTS_BY_ITEM);
                    db.execSQL(Sql.SQL_CREATE_STEPS);
                }
            },
            new PatchDB() {//PATCH V4
                @Override
                public void apply(Context context, SQLiteDatabase db) {
                    db.execSQL("ALTER TABLE " + StoreTable.TABLE_NAME + " ADD " + StoreTable.COLUMN_DISTANCE + " REAL;");
                }
            }
    };
}