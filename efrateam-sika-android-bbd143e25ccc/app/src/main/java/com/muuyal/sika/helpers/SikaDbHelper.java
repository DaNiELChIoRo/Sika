package com.muuyal.sika.helpers;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.muuyal.sika.MyApplication;
import com.muuyal.sika.R;
import com.muuyal.sika.db.Sql;
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
import com.muuyal.sika.model.Company;
import com.muuyal.sika.model.Country;
import com.muuyal.sika.model.District;
import com.muuyal.sika.model.Glossary;
import com.muuyal.sika.model.Item;
import com.muuyal.sika.model.Photo;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.ProductItem;
import com.muuyal.sika.model.ProductRelated;
import com.muuyal.sika.model.Promotion;
import com.muuyal.sika.model.SliderItem;
import com.muuyal.sika.model.SliderMetadata;
import com.muuyal.sika.model.Solution;
import com.muuyal.sika.model.SolutionSection;
import com.muuyal.sika.model.State;
import com.muuyal.sika.model.Step;
import com.muuyal.sika.model.Store;
import com.muuyal.sika.model.Suggestion;
import com.muuyal.sika.model.Taxonomy;
import com.muuyal.sika.model.Thumbnail;
import com.muuyal.sika.model.Zipcode;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isra on 5/22/17.
 */

public class SikaDbHelper extends SQLiteOpenHelper {

    private static final String TAG = SikaDbHelper.class.getSimpleName();
    private static final int VERSION_BD = 4;
    private static final String DB_NAME = MyApplication.getAppContext().getString(R.string.app_name) + ".db";

    private static final String PRE_ITEM = "PRE_ITEM_";
    private static final String PRE_PRODUCT_ITEM = "PRE_P_ITEM_";
    private static final String PRE_DISTRICT = "PRE_DIST_";

    private static SikaDbHelper _instance;
    private Context mContext;

    public synchronized static SikaDbHelper getInstance(Context mContext) {
        if (_instance == null) {
            _instance = new SikaDbHelper(mContext);
        }

        return _instance;
    }

    private SikaDbHelper(Context mContext) {
        super(mContext, DB_NAME, null, VERSION_BD);
        this.mContext = mContext;

        // This will happen in onConfigure for API >= 16
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            SQLiteDatabase db = getWritableDatabase();
            db.enableWriteAheadLogging();
            String pragma = "PRAGMA foreign_keys = ON;";
            db.execSQL(pragma);
        } else {
            SQLiteDatabase db = getWritableDatabase();
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Sql.SQL_CREATE_COUNTRY);
        db.execSQL(Sql.SQL_CREATE_STATE);
        db.execSQL(Sql.SQL_CREATE_ZIPCODE);
        db.execSQL(Sql.SQL_CREATE_TAXONOMY);
        db.execSQL(Sql.SQL_CREATE_PRODUCT);
        db.execSQL(Sql.SQL_CREATE_STORE);
        db.execSQL(Sql.SQL_CREATE_COMPANY);
        db.execSQL(Sql.SQL_CREATE_SOLUTION_SECTIONS);
        db.execSQL(Sql.SQL_CREATE_SOLUTIONS);
        db.execSQL(Sql.SQL_CREATE_ITEMS);
        db.execSQL(Sql.SQL_CREATE_PRODUCTS_BY_ITEM);
        db.execSQL(Sql.SQL_CREATE_STEPS);
        db.execSQL(Sql.SQL_CREATE_PROMOTIONS);
        db.execSQL(Sql.SQL_CREATE_SLIDER_ITEMS);
        db.execSQL(Sql.SQL_CREATE_SLIDER_METADATA);
        db.execSQL(Sql.SQL_CREATE_PHOTOS);
        db.execSQL(Sql.SQL_CREATE_THUMBNAILS);
        db.execSQL(Sql.SQL_CREATE_PRODUCT_RELATED);
        db.execSQL(Sql.SQL_CREATE_TOOLS);
        db.execSQL(Sql.SQL_CREATE_GLOSSARY);
        db.execSQL(Sql.SQL_CREATE_DISTRICTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LoggerUtils.logInfo(TAG, "@@@@upgradeData");
        if (oldVersion < newVersion) {
            try {
                for (int i = oldVersion; i < VERSION_BD; i++) {
                    Sql.PATCHES_DB[i - 1].apply(MyApplication.getAppContext(), db);
                    LoggerUtils.logInfo(TAG, "onUpgrade", "@@@@Applying Patch: " + i);
                }
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "onUpgrade", "Error apply upgrade: " + e);
            }
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        setWriteAheadLoggingEnabled(true);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /***
     * This method return the writable db
     *
     * @param mContext is the context of the App
     ***/
    private static synchronized SQLiteDatabase getWritableDb(Context mContext) {
        return getInstance(mContext).getWritableDatabase();
    }

    /***
     * This method return the Readable db
     *
     * @param mContext is the context of the App
     ***/
    private static synchronized SQLiteDatabase getReadableDb(Context mContext) {
        return getInstance(mContext).getReadableDatabase();
    }

    /***
     * This method createDatabase and apply Patches to db
     *
     * @param mContext is the context of the App
     ***/
    public static int createDatabase(Context mContext) {
        return getReadableDb(mContext).getVersion();
    }

    /***
     * This method close actual cursor
     *
     * @param cursor is the actual cursor
     ***/
    private static void closeCursor(Cursor cursor) {
        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * This method close actual cursor
     *
     * @param db is the actual db
     ***/
    private static void closeDb(SQLiteDatabase db) {
        try {
            if (db != null && db.isOpen()) {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * This method drop and recreate table from database
     *
     * @param mContext is the context App
     * ***/
    public static void clearDatabase(Context mContext) {
        if (mContext != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                db.execSQL("DROP TABLE IF EXISTS " + DistrictTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + GlossaryTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + ToolTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + ProductRelatedTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + ThumbnailTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + PhotoTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + SliderMetadataTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + SliderItemTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + PromotionTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + StepTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + ProductItemTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + ItemTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + SolutionTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + SolutionSectionTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + CompanyTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + StoreTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + ProductTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TaxonomyTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + ZipcodeTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + StateTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + CountryTable.TABLE_NAME);

                //Recreate tables db
                SikaDbHelper.getInstance(mContext).onCreate(db);
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "clearDatabase", "Error clear database: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
    }

    /***********************************************************************************************
     * ************************************ COUNTRIES **********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of countries
     *
     * @param mContext is the context app
     * @param mCountries are the country list
     * ***/
    public static synchronized void saveCountries(Context mContext, List<Country> mCountries) {
        if (mContext != null && mCountries != null) {
            for (Country mCountry : mCountries) {
                saveCountry(mContext, mCountry);
            }
        }
    }

    /***
     * This method save a Country
     *
     * @param mContext is the context App
     * @param mCountry    is the Country to save
     ***/
    public static synchronized boolean saveCountry(Context mContext, Country mCountry) {
        if (mContext != null && mCountry != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(CountryTable.COLUMN_ID_COUNTRY, mCountry.getId());
                values.put(CountryTable.COLUMN_NAME, mCountry.getName());
                values.put(CountryTable.COLUMN_CODE, mCountry.getCode());
                values.put(CountryTable.COLUMN_CREATED_AT, mCountry.getCreatedAt());
                values.put(CountryTable.COLUMN_UPDATED_AT, mCountry.getUpdatedAt());

                long newRowId = db.insertWithOnConflict(CountryTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Save States if exist
                saveStates(mContext, mCountry.getStates(), mCountry.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveCountry", "Error saveCountry: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the country list
     *
     * @param mContext is the context App
     * @return a country list
     ***/
    public static synchronized List<Country> getCountries(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Country> mCountryList = new ArrayList<>();

                String[] projection = {
                        CountryTable.COLUMN_ID_COUNTRY,
                        CountryTable.COLUMN_NAME,
                        CountryTable.COLUMN_CODE,
                        CountryTable.COLUMN_CREATED_AT,
                        CountryTable.COLUMN_UPDATED_AT
                };

                String orderby = CountryTable.COLUMN_NAME + " ASC";
                cursor = db.query(CountryTable.TABLE_NAME, projection, null, null, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Country mCountry = new Country();
                    mCountry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CountryTable.COLUMN_ID_COUNTRY)));
                    mCountry.setName(cursor.getString(cursor.getColumnIndexOrThrow(CountryTable.COLUMN_NAME)));
                    mCountry.setCode(cursor.getString(cursor.getColumnIndexOrThrow(CountryTable.COLUMN_CODE)));
                    mCountry.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(CountryTable.COLUMN_CREATED_AT)));
                    mCountry.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(CountryTable.COLUMN_UPDATED_AT)));

                    mCountry.setStates(getStatesByCountryId(mContext, mCountry.getId()));

                    mCountryList.add(mCountry);
                }

                return mCountryList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getCountry", "Error getCountry: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ STATES *************************************************
     * ********************************************************************************************/

    /***
     * This method save a list of states
     *
     * @param mContext is the context app
     * @param mStates are the states list
     * @param countryId is the Id country
     * ***/
    public static synchronized void saveStates(Context mContext, List<State> mStates, int countryId) {
        if (mContext != null && mStates != null) {
            for (State mState : mStates) {
                saveState(mContext, mState, countryId);
            }
        }
    }

    /***
     * This method save a State
     *
     * @param mContext is the context App
     * @param mState    is the State to save
     * @param countryId is the Id country
     ***/
    public static synchronized boolean saveState(Context mContext, State mState, int countryId) {
        if (mContext != null && mState != null && countryId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(StateTable.COLUMN_ID_STATE, mState.getId());
                values.put(StateTable.COLUMN_NAME, mState.getName());
                values.put(StateTable.COLUMN_CREATED_AT, mState.getCreatedAt());
                values.put(StateTable.COLUMN_UPDATED_AT, mState.getUpdatedAt());

                values.put(StateTable.COLUMN_ID_COUNTRY, countryId);

                long newRowId = db.insertWithOnConflict(StateTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save districts
                saveDistricts(mContext, mState.getDistricts(), mState.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveState", "Error saveState: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the state list
     *
     * @param mContext is the context App
     * @param countryId is the ID of country
     * @return a state list
     ***/
    public static synchronized List<State> getStatesByCountryId(Context mContext, int countryId) {
        if (mContext != null && countryId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<State> mStateList = new ArrayList<>();

                String[] projection = {
                        StateTable.COLUMN_ID_STATE,
                        StateTable.COLUMN_NAME,
                        StateTable.COLUMN_CREATED_AT,
                        StateTable.COLUMN_UPDATED_AT
                };

                String selection = StateTable.COLUMN_ID_COUNTRY + " = ?";
                String[] selectionArgs = {String.valueOf(countryId)};
                String orderby = StateTable.COLUMN_NAME + " ASC";

                cursor = db.query(StateTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    State mState = new State();
                    mState.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StateTable.COLUMN_ID_STATE)));
                    mState.setName(cursor.getString(cursor.getColumnIndexOrThrow(StateTable.COLUMN_NAME)));
                    mState.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StateTable.COLUMN_CREATED_AT)));
                    mState.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StateTable.COLUMN_UPDATED_AT)));

                    //Here setup district
                    mState.setDistricts(getDistrictsByIdState(mContext, mState.getId()));

                    mStateList.add(mState);
                }

                return mStateList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getStatesByCountryId", "Error getStatesByCountryId: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return the state list
     *
     * @param mContext is the context App
     * @param idState is the ID of state
     * @return a state list
     ***/
    public static synchronized State getStateById(Context mContext, int idState) {
        if (mContext != null && idState > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                State mState = null;

                String[] projection = {
                        StateTable.COLUMN_ID_STATE,
                        StateTable.COLUMN_NAME,
                        StateTable.COLUMN_CREATED_AT,
                        StateTable.COLUMN_UPDATED_AT
                };

                String selection = StateTable.COLUMN_ID_STATE + " = ?";
                String[] selectionArgs = {String.valueOf(idState)};
                String orderby = StateTable.COLUMN_NAME + " ASC";

                cursor = db.query(StateTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                if (cursor != null && cursor.moveToFirst()) {
                    mState = new State();
                    mState.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StateTable.COLUMN_ID_STATE)));
                    mState.setName(cursor.getString(cursor.getColumnIndexOrThrow(StateTable.COLUMN_NAME)));
                    mState.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StateTable.COLUMN_CREATED_AT)));
                    mState.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StateTable.COLUMN_UPDATED_AT)));

                    //Here setup district
                    mState.setDistricts(getDistrictsByIdState(mContext, mState.getId()));
                } else {
                    LoggerUtils.logError(TAG, "getStateById", "Error state not found");
                }

                return mState;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getStateById", "Error getStateById: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ DISTRICTS **********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of district
     *
     * @param mContext is the context app
     * @param mDistricts are the district list
     * @param idState is the Id state
     * ***/
    public static synchronized void saveDistricts(Context mContext, List<District> mDistricts, int idState) {
        if (mContext != null && mDistricts != null && idState > -1) {
            for (District mDistrict : mDistricts) {
                saveDistrict(mContext, mDistrict, idState);
            }
        }
    }

    /***
     * This method save a District
     *
     * @param mContext is the context App
     * @param mDistrict    is the District to save
     * @param idState is the Id state
     ***/
    public static synchronized boolean saveDistrict(Context mContext, District mDistrict, int idState) {
        if (mContext != null && mDistrict != null && idState > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(DistrictTable.COLUMN_ID_DISTRICT, mDistrict.getId());
                values.put(DistrictTable.COLUMN_NAME, mDistrict.getName());

                values.put(DistrictTable.COLUMN_ID_STATE, idState);

                long newRowId = db.insertWithOnConflict(DistrictTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveDistrict", "Error saveDistrict: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the state list
     *
     * @param mContext is the context App
     * @param idState is the ID of state
     * @return a district by state list
     ***/
    public static synchronized List<District> getDistrictsByIdState(Context mContext, int idState) {
        if (mContext != null && idState > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<District> mDistrictList = new ArrayList<>();

                String[] projection = {
                        DistrictTable.COLUMN_ID_DISTRICT,
                        DistrictTable.COLUMN_NAME
                };

                String selection = DistrictTable.COLUMN_ID_STATE + " = ?";
                String[] selectionArgs = {String.valueOf(idState)};
                String orderby = DistrictTable.COLUMN_NAME + " ASC";

                cursor = db.query(DistrictTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    District mDistrict = new District();
                    mDistrict.setId(cursor.getString(cursor.getColumnIndexOrThrow(DistrictTable.COLUMN_ID_DISTRICT)));
                    mDistrict.setName(cursor.getString(cursor.getColumnIndexOrThrow(DistrictTable.COLUMN_NAME)));

                    mDistrictList.add(mDistrict);
                }

                return mDistrictList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getDistrictsByIdState", "Error getDistrictsByIdState: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ ZIPCODES ***********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of Zipcodes
     *
     * @param mContext is the context app
     * @param mZipcodes are the zipcodes list
     * @param stateId is the Id state
     * ***/
    public static synchronized void saveZipcodes(Context mContext, List<Zipcode> mZipcodes, int stateId) {
        if (mContext != null && mZipcodes != null) {
            for (Zipcode mZipcode : mZipcodes) {
                saveZipcode(mContext, mZipcode, stateId);
            }
        }
    }

    /***
     * This method save a Zipcode
     *
     * @param mContext is the context App
     * @param mZipcode    is the Zipcode to save
     * @param stateId is the Id state
     ***/
    private static synchronized boolean saveZipcode(Context mContext, Zipcode mZipcode, int stateId) {
        if (mContext != null && mZipcode != null && stateId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ZipcodeTable.COLUMN_ID_ZIPCODE, mZipcode.getId());
                values.put(ZipcodeTable.COLUMN_ZIPCODE, mZipcode.getZipcode());
                values.put(ZipcodeTable.COLUMN_DISTRICT, mZipcode.getDistrict());
                values.put(ZipcodeTable.COLUMN_SUBURB, mZipcode.getSuburb());
                values.put(ZipcodeTable.COLUMN_CREATED_AT, mZipcode.getCreatedAt());
                values.put(ZipcodeTable.COLUMN_UPDATED_AT, mZipcode.getUpdatedAt());

                values.put(ZipcodeTable.COLUMN_ID_STATE, stateId);

                long newRowId = db.insertWithOnConflict(ZipcodeTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveZipcode", "Error saveZipcode: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the zipcode list
     *
     * @param mContext is the context App
     * @param stateId is the Id from state
     * @return a zipcode list
     ***/
    public static synchronized List<Zipcode> getZipcodesByStateId(Context mContext, int stateId) {
        if (mContext != null && stateId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Zipcode> mZipcodeList = new ArrayList<>();

                String[] projection = {
                        ZipcodeTable.COLUMN_ID_ZIPCODE,
                        ZipcodeTable.COLUMN_ZIPCODE,
                        ZipcodeTable.COLUMN_DISTRICT,
                        ZipcodeTable.COLUMN_SUBURB,
                        ZipcodeTable.COLUMN_CREATED_AT,
                        ZipcodeTable.COLUMN_UPDATED_AT
                };

                String selection = ZipcodeTable.COLUMN_ID_STATE + " = ?";
                String[] selectionArgs = {String.valueOf(stateId)};
                String orderby = ZipcodeTable.COLUMN_SUBURB + " ASC";

                cursor = db.query(ZipcodeTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Zipcode mZipcode = new Zipcode();
                    mZipcode.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ZipcodeTable.COLUMN_ID_ZIPCODE)));
                    mZipcode.setZipcode(cursor.getString(cursor.getColumnIndexOrThrow(ZipcodeTable.COLUMN_ZIPCODE)));
                    mZipcode.setDistrict(cursor.getString(cursor.getColumnIndexOrThrow(ZipcodeTable.COLUMN_DISTRICT)));
                    mZipcode.setSuburb(cursor.getString(cursor.getColumnIndexOrThrow(ZipcodeTable.COLUMN_SUBURB)));
                    mZipcode.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ZipcodeTable.COLUMN_CREATED_AT)));
                    mZipcode.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ZipcodeTable.COLUMN_UPDATED_AT)));

                    mZipcodeList.add(mZipcode);
                }

                return mZipcodeList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getZipcodesByStateId", "Error getZipcodesByStateId: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ TAXONOMIES *********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of TAXONOMIES
     *
     * @param mContext is the context app
     * @param mTaxonomies are the Taxonomy list
     * ***/
    public static synchronized void saveTaxonomies(Context mContext, List<Taxonomy> mTaxonomies) {
        if (mContext != null && mTaxonomies != null) {
            for (Taxonomy mTaxonomy : mTaxonomies) {
                saveTaxonomy(mContext, mTaxonomy);
            }
        }
    }

    /***
     * This method save a Taxonomies
     *
     * @param mContext is the context App
     * @param mTaxonomy    is the Taxonomy to save
     ***/
    private static synchronized boolean saveTaxonomy(Context mContext, Taxonomy mTaxonomy) {
        if (mContext != null && mTaxonomy != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(TaxonomyTable.COLUMN_ID_TAXONOMY, mTaxonomy.getId());
                values.put(TaxonomyTable.COLUMN_TYPE, mTaxonomy.getType());
                values.put(TaxonomyTable.COLUMN_VALUE, mTaxonomy.getValue());
                values.put(TaxonomyTable.COLUMN_CREATED_AT, mTaxonomy.getCreatedAt());
                values.put(TaxonomyTable.COLUMN_UPDATED_AT, mTaxonomy.getUpdatedAt());
                values.put(TaxonomyTable.COLUMN_ID_PARENT, mTaxonomy.getIdParent());

                long newRowId = db.insertWithOnConflict(TaxonomyTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save children taxonomies
                if (mTaxonomy.getTaxonomies() != null && !mTaxonomy.getTaxonomies().isEmpty())
                    saveTaxonomies(mContext, mTaxonomy.getTaxonomies());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveTaxonomy", "Error saveTaxonomy: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the taxonomy list
     *
     * @param mContext is the context App
     * @return a taxonomy list
     ***/
    public static synchronized List<Taxonomy> getTaxonomies(Context mContext, int idParent) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Taxonomy> mTaxonomyList = new ArrayList<>();

                String[] projection = {
                        TaxonomyTable.COLUMN_ID_TAXONOMY,
                        TaxonomyTable.COLUMN_TYPE,
                        TaxonomyTable.COLUMN_VALUE,
                        TaxonomyTable.COLUMN_CREATED_AT,
                        TaxonomyTable.COLUMN_UPDATED_AT,
                        TaxonomyTable.COLUMN_ID_PARENT
                };

                String orderby = TaxonomyTable.COLUMN_VALUE + " ASC";

                String selection = TaxonomyTable.COLUMN_ID_PARENT + " = ?";
                String[] selectionArgs = {String.valueOf(idParent)};
                cursor = db.query(TaxonomyTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Taxonomy mTaxonomy = new Taxonomy();
                    mTaxonomy.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TaxonomyTable.COLUMN_ID_TAXONOMY)));
                    mTaxonomy.setType(cursor.getString(cursor.getColumnIndexOrThrow(TaxonomyTable.COLUMN_TYPE)));
                    mTaxonomy.setValue(cursor.getString(cursor.getColumnIndexOrThrow(TaxonomyTable.COLUMN_VALUE)));
                    mTaxonomy.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(TaxonomyTable.COLUMN_CREATED_AT)));
                    mTaxonomy.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(TaxonomyTable.COLUMN_UPDATED_AT)));
                    mTaxonomy.setIdParent(cursor.getInt(cursor.getColumnIndexOrThrow(TaxonomyTable.COLUMN_ID_PARENT)));

                    //get children taxonomies if exist
                    mTaxonomy.setTaxonomies(getTaxonomies(mContext, mTaxonomy.getId()));

                    mTaxonomyList.add(mTaxonomy);
                }

                return mTaxonomyList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getTaxonomies", "Error getTaxonomies: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ PRODUCTS ***********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of Products
     *
     * @param mContext is the context app
     * @param mProducts is the Product list
     * ***/
    public static synchronized void saveProducts(Context mContext, List<Product> mProducts) {
        if (mContext != null && mProducts != null) {
            for (Product mProduct : mProducts) {
                if (!existProduct(mContext, mProduct.getId())) {
                    saveProduct(mContext, mProduct);
                } else {
                    updateProduct(mContext, mProduct);
                }
            }
        }
    }

    /***
     * This method return a product exist in db
     *
     * @param mContext      is the context App
     * @param idProduct is the id of product to save
     * @return true if exist, false other case
     ***/
    public static synchronized boolean existProduct(Context mContext, long idProduct) {
        boolean existAssignmnet = false;
        if (mContext != null && idProduct > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                String[] projection = {
                        ProductTable.COLUMN_ID_PRODUCT
                };

                String selection = ProductTable.COLUMN_ID_PRODUCT + " = ?";
                String[] selectionArgs = {String.valueOf(idProduct)};

                cursor = db.query(ProductTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.getCount() > 0) {
                    existAssignmnet = true;
                } else {
                    LoggerUtils.logError(TAG, "existProduct", "not exist product");
                }
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "existProduct", "Error existProduct: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return existAssignmnet;
    }

    /***
     * This method save a Product
     *
     * @param mContext is the context App
     * @param mProduct    is the Product to save
     ***/
    public static synchronized boolean saveProduct(Context mContext, Product mProduct) {
        if (mContext != null && mProduct != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ProductTable.COLUMN_ID_PRODUCT, mProduct.getId());
                values.put(ProductTable.COLUMN_NAME, mProduct.getName());
                values.put(ProductTable.COLUMN_DETAILS, mProduct.getDetails());
                values.put(ProductTable.COLUMN_CREATED_AT, mProduct.getCreatedAt());
                values.put(ProductTable.COLUMN_DELETED_AT, mProduct.getDeletedAt());

                long newRowId = db.insertWithOnConflict(ProductTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_ABORT);

                //Here save photo
                savePhoto(mContext, mProduct.getPhoto(), mProduct.getId());
                saveRelatedProducts(mContext, mProduct.getRelated(), mProduct.getId());
                saveTools(mContext, mProduct.getTools(), mProduct.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveProduct", "Error saveProduct: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }


    /***
     * This method update a Product
     *
     * @param mContext is the context App
     * @param mProduct    is the Product to save
     ***/
    private static synchronized boolean updateProduct(Context mContext, Product mProduct) {
        if (mContext != null && mProduct != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ProductTable.COLUMN_NAME, mProduct.getName());
                values.put(ProductTable.COLUMN_DETAILS, mProduct.getDetails());
                values.put(ProductTable.COLUMN_CREATED_AT, mProduct.getCreatedAt());
                values.put(ProductTable.COLUMN_DELETED_AT, mProduct.getDeletedAt());

                String where = ProductTable.COLUMN_ID_PRODUCT + " = ?";
                String[] whereArgs = {String.valueOf(mProduct.getId())};

                long newRowId = db.updateWithOnConflict(ProductTable.TABLE_NAME, values, where, whereArgs, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save photo
                savePhoto(mContext, mProduct.getPhoto(), mProduct.getId());
                saveRelatedProducts(mContext, mProduct.getRelated(), mProduct.getId());
                saveTools(mContext, mProduct.getTools(), mProduct.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "updateProduct", "Error updateProduct: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the Product list
     *
     * @param mContext is the context App
     * @return a product list
     ***/
    public static synchronized List<Product> getProducts(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Product> mProductList = new ArrayList<>();

                String[] projection = {
                        ProductTable.COLUMN_ID_PRODUCT,
                        ProductTable.COLUMN_NAME,
                        ProductTable.COLUMN_DETAILS,
                        ProductTable.COLUMN_CREATED_AT,
                        ProductTable.COLUMN_DELETED_AT
                };

                String orderby = ProductTable.COLUMN_NAME + " ASC";
                cursor = db.query(ProductTable.TABLE_NAME, projection, null, null, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Product mProduct = new Product();
                    mProduct.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_ID_PRODUCT)));
                    mProduct.setName(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_NAME)));
                    mProduct.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DETAILS)));
                    mProduct.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_CREATED_AT)));
                    mProduct.setDeletedAt(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DELETED_AT)));

                    //Here set setup photo
                    mProduct.setPhoto(getPhoto(mContext, mProduct.getId()));
                    mProduct.setRelated(getRelatedProducts(mContext, mProduct.getId()));
                    mProduct.setTools(getTools(mContext, mProduct.getId()));

                    mProductList.add(mProduct);
                }

                return mProductList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getProducts", "Error getProducts: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return the Product list
     *
     * @param mContext is the context App
     * @param productId is the product id to search
     * @return a product by id
     ***/
    public static synchronized Product getProductById(Context mContext, int productId) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                Product mProduct = null;

                String[] projection = {
                        ProductTable.COLUMN_ID_PRODUCT,
                        ProductTable.COLUMN_NAME,
                        ProductTable.COLUMN_DETAILS,
                        ProductTable.COLUMN_CREATED_AT,
                        ProductTable.COLUMN_DELETED_AT
                };

                String selection = ProductTable.COLUMN_ID_PRODUCT + " = ?";
                String[] selectionArgs = {String.valueOf(productId)};
                cursor = db.query(ProductTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    mProduct = new Product();
                    mProduct.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_ID_PRODUCT)));
                    mProduct.setName(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_NAME)));
                    mProduct.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DETAILS)));
                    mProduct.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_CREATED_AT)));
                    mProduct.setDeletedAt(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DELETED_AT)));

                    //Here set setup photo
                    mProduct.setPhoto(getPhoto(mContext, mProduct.getId()));
                    mProduct.setRelated(getRelatedProducts(mContext, mProduct.getId()));
                    mProduct.setTools(getTools(mContext, mProduct.getId()));

                } else {
                    LoggerUtils.logError(TAG, "getProductById", "Error getProductById is empty");
                }

                return mProduct;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getProducts", "Error getProducts: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method save a list of Products by Taxonomy ID
     *
     * @param mContext is the context app
     * @param mProducts is the Product list
     * @param taxonomyId is the Id of Taxonomy
     * ***/
    public static synchronized void saveProductsByTaxonomyId(Context mContext, List<Product> mProducts, int taxonomyId) {
        if (mContext != null && mProducts != null) {
            for (Product mProduct : mProducts) {
                saveProductByTaxonomyId(mContext, mProduct, taxonomyId);
            }
        }
    }

    /***
     * This method save a Product
     *
     * @param mContext is the context App
     * @param mProduct    is the Product to save
     * @param taxonomyId is the Id of Taxonomy
     ***/
    private static synchronized boolean saveProductByTaxonomyId(Context mContext, Product mProduct, int taxonomyId) {
        if (mContext != null && mProduct != null && taxonomyId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ProductTable.COLUMN_ID_PRODUCT, mProduct.getId());
                values.put(ProductTable.COLUMN_NAME, mProduct.getName());
                values.put(ProductTable.COLUMN_DETAILS, mProduct.getDetails());
                values.put(ProductTable.COLUMN_CREATED_AT, mProduct.getCreatedAt());
                values.put(ProductTable.COLUMN_DELETED_AT, mProduct.getDeletedAt());

                values.put(ProductTable.COLUMN_ID_TAXONOMY, taxonomyId);

                long newRowId = db.insertWithOnConflict(ProductTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save photo
                savePhoto(mContext, mProduct.getPhoto(), mProduct.getId());
                saveRelatedProducts(mContext, mProduct.getRelated(), mProduct.getId());
                saveTools(mContext, mProduct.getTools(), mProduct.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveProductByTaxonomyId", "Error saveProductByTaxonomyId: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the Product list
     *
     * @param mContext is the context App
     * @param taxonomyId is the Id taxonomy
     * @return a product list
     ***/
    public static synchronized List<Product> getProductsByTaxonomyId(Context mContext, int taxonomyId) {
        if (mContext != null && taxonomyId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Product> mProductList = new ArrayList<>();

                String[] projection = {
                        ProductTable.COLUMN_ID_PRODUCT,
                        ProductTable.COLUMN_NAME,
                        ProductTable.COLUMN_DETAILS,
                        ProductTable.COLUMN_CREATED_AT,
                        ProductTable.COLUMN_DELETED_AT
                };

                String selection = ProductTable.COLUMN_ID_TAXONOMY + " = ?";
                String[] selectionArgs = {String.valueOf(taxonomyId)};
                String orderby = ProductTable.COLUMN_NAME + " ASC";
                cursor = db.query(ProductTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Product mProduct = new Product();
                    mProduct.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_ID_PRODUCT)));
                    mProduct.setName(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_NAME)));
                    mProduct.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DETAILS)));
                    mProduct.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_CREATED_AT)));
                    mProduct.setDeletedAt(cursor.getString(cursor.getColumnIndexOrThrow(ProductTable.COLUMN_DELETED_AT)));

                    //Here set setup photo
                    mProduct.setPhoto(getPhoto(mContext, mProduct.getId()));
                    mProduct.setRelated(getRelatedProducts(mContext, mProduct.getId()));
                    mProduct.setTools(getTools(mContext, mProduct.getId()));

                    mProductList.add(mProduct);
                }

                return mProductList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getProductsByTaxonomyId", "Error getProductsByTaxonomyId: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }


    /***********************************************************************************************
     * ************************************ STORES *************************************************
     * ********************************************************************************************/

    /***
     * This method save a list of Stores
     *
     * @param mContext is the context app
     * @param mStores is the Store list
     * ***/
    public static synchronized void saveStores(Context mContext, List<Store> mStores) {
        if (mContext != null && mStores != null) {
            for (Store mStore : mStores) {
                saveStore(mContext, mStore);
            }
        }
    }

    /***
     * This method save a list of Stores
     *
     * @param mContext is the context app
     * @param mStores is the Store list
     * @param districtId is the currect districtID
     * ***/
    public static synchronized void saveStoresByDistrict(Context mContext, List<Store> mStores, String districtId) {
        if (mContext != null && mStores != null) {
            for (Store mStore : mStores) {
                saveStoreByDistrictId(mContext, mStore, districtId);
            }
        }
    }

    /***
     * This method save a list of Stores
     *
     * @param mContext is the context app
     * @param mStores is the Store list
     * @param productId is the Id of product
     * ***/
    public static synchronized void saveStoresByProduct(Context mContext, List<Store> mStores, int productId) {
        if (mContext != null && mStores != null) {
            for (Store mStore : mStores) {
                saveStoreByProductId(mContext, mStore, productId);
            }
        }
    }

    /***
     * This method save a list of Stores
     *
     * @param mContext is the context app
     * @param mStores is the Store list
     * @param stateId is the Id of state
     * ***/
    public static synchronized void saveStoresByState(Context mContext, List<Store> mStores, int stateId) {
        if (mContext != null && mStores != null) {
            for (Store mStore : mStores) {
                saveStoreByStateId(mContext, mStore, stateId);
            }
        }
    }

    /***
     * This method save a Store
     *
     * @param mContext is the context App
     * @param mStore    is the Store to save
     ***/
    private static synchronized boolean saveStore(Context mContext, Store mStore) {
        if (mContext != null && mStore != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(StoreTable.COLUMN_ID_STORE, mStore.getId());
                values.put(StoreTable.COLUMN_NAME, mStore.getName());
                values.put(StoreTable.COLUMN_DETAILS, mStore.getDetails());
                values.put(StoreTable.COLUMN_STREET, mStore.getStreet());
                values.put(StoreTable.COLUMN_EXTERNAL_NUMBER, mStore.getExternalNumber());
                values.put(StoreTable.COLUMN_INTERNAL_NUMBER, mStore.getInternalNumber());
                values.put(StoreTable.COLUMN_VISUAL_REFERENCE, mStore.getVisualReference());
                values.put(StoreTable.COLUMN_PHONE, mStore.getPhone());
                values.put(StoreTable.COLUMN_LAT, mStore.getLat());
                values.put(StoreTable.COLUMN_LNG, mStore.getLng());
                values.put(StoreTable.COLUMN_ADDRESS, mStore.getAddress());
                values.put(StoreTable.COLUMN_DISTANCE, mStore.getDistance());
                values.put(StoreTable.COLUMN_CREATED_AT, mStore.getCreatedAt());
                values.put(StoreTable.COLUMN_UPDATED_AT, mStore.getUpdatedAt());

                long newRowId = db.insertWithOnConflict(StoreTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveStoreByStateId", "Error saveStoreByStateId: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method save a Store by district
     *
     * @param mContext is the context App
     * @param mStore    is the Store to save
     * @param mDistrictId is the current districtId
     ***/
    private static synchronized boolean saveStoreByDistrictId(Context mContext, Store mStore, String mDistrictId) {
        if (mContext != null && mStore != null && !TextUtils.isEmpty(mDistrictId)) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(StoreTable.COLUMN_ID_STORE, mStore.getId());
                values.put(StoreTable.COLUMN_NAME, mStore.getName());
                values.put(StoreTable.COLUMN_DETAILS, mStore.getDetails());
                values.put(StoreTable.COLUMN_STREET, mStore.getStreet());
                values.put(StoreTable.COLUMN_EXTERNAL_NUMBER, mStore.getExternalNumber());
                values.put(StoreTable.COLUMN_INTERNAL_NUMBER, mStore.getInternalNumber());
                values.put(StoreTable.COLUMN_VISUAL_REFERENCE, mStore.getVisualReference());
                values.put(StoreTable.COLUMN_PHONE, mStore.getPhone());
                values.put(StoreTable.COLUMN_LAT, mStore.getLat());
                values.put(StoreTable.COLUMN_LNG, mStore.getLng());
                values.put(StoreTable.COLUMN_ADDRESS, mStore.getAddress());
                values.put(StoreTable.COLUMN_DISTANCE, mStore.getDistance());
                values.put(StoreTable.COLUMN_CREATED_AT, mStore.getCreatedAt());
                values.put(StoreTable.COLUMN_UPDATED_AT, mStore.getUpdatedAt());

                values.put(StoreTable.COLUMN_ID_DISTRICT, mDistrictId);

                long newRowId = db.insertWithOnConflict(StoreTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveStoreByProduct", "Error saveStoreByProduct: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method save a Store by productId
     *
     * @param mContext is the context App
     * @param mStore    is the Store to save
     * @param productId is the Id of product
     ***/
    private static synchronized boolean saveStoreByProductId(Context mContext, Store mStore, int productId) {
        if (mContext != null && mStore != null && productId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(StoreTable.COLUMN_ID_STORE, mStore.getId());
                values.put(StoreTable.COLUMN_NAME, mStore.getName());
                values.put(StoreTable.COLUMN_DETAILS, mStore.getDetails());
                values.put(StoreTable.COLUMN_STREET, mStore.getStreet());
                values.put(StoreTable.COLUMN_EXTERNAL_NUMBER, mStore.getExternalNumber());
                values.put(StoreTable.COLUMN_INTERNAL_NUMBER, mStore.getInternalNumber());
                values.put(StoreTable.COLUMN_VISUAL_REFERENCE, mStore.getVisualReference());
                values.put(StoreTable.COLUMN_PHONE, mStore.getPhone());
                values.put(StoreTable.COLUMN_LAT, mStore.getLat());
                values.put(StoreTable.COLUMN_LNG, mStore.getLng());
                values.put(StoreTable.COLUMN_ADDRESS, mStore.getAddress());
                values.put(StoreTable.COLUMN_DISTANCE, mStore.getDistance());
                values.put(StoreTable.COLUMN_CREATED_AT, mStore.getCreatedAt());
                values.put(StoreTable.COLUMN_UPDATED_AT, mStore.getUpdatedAt());

                values.put(StoreTable.COLUMN_ID_PRODUCT, productId);

                long newRowId = db.insertWithOnConflict(StoreTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveStoreByProduct", "Error saveStoreByProduct: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method save a Store by stateId
     *
     * @param mContext is the context App
     * @param mStore    is the Store to save
     * @param stateId is the Id of state
     ***/
    private static synchronized boolean saveStoreByStateId(Context mContext, Store mStore, int stateId) {
        if (mContext != null && mStore != null && stateId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(StoreTable.COLUMN_ID_STORE, mStore.getId());
                values.put(StoreTable.COLUMN_NAME, mStore.getName());
                values.put(StoreTable.COLUMN_DETAILS, mStore.getDetails());
                values.put(StoreTable.COLUMN_STREET, mStore.getStreet());
                values.put(StoreTable.COLUMN_EXTERNAL_NUMBER, mStore.getExternalNumber());
                values.put(StoreTable.COLUMN_INTERNAL_NUMBER, mStore.getInternalNumber());
                values.put(StoreTable.COLUMN_VISUAL_REFERENCE, mStore.getVisualReference());
                values.put(StoreTable.COLUMN_PHONE, mStore.getPhone());
                values.put(StoreTable.COLUMN_LAT, mStore.getLat());
                values.put(StoreTable.COLUMN_LNG, mStore.getLng());
                values.put(StoreTable.COLUMN_ADDRESS, mStore.getAddress());
                values.put(StoreTable.COLUMN_DISTANCE, mStore.getDistance());
                values.put(StoreTable.COLUMN_CREATED_AT, mStore.getCreatedAt());
                values.put(StoreTable.COLUMN_UPDATED_AT, mStore.getUpdatedAt());

                values.put(StoreTable.COLUMN_ID_STATE, stateId);

                long newRowId = db.insertWithOnConflict(StoreTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveStoreByStateId", "Error saveStoreByStateId: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the Stores list
     *
     * @param mContext is the context App
     * @return a store list
     ***/
    public static synchronized List<Store> getStores(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Store> mStoreList = new ArrayList<>();

                String[] projection = {
                        StoreTable.COLUMN_ID_STORE,
                        StoreTable.COLUMN_NAME,
                        StoreTable.COLUMN_DETAILS,
                        StoreTable.COLUMN_STREET,
                        StoreTable.COLUMN_EXTERNAL_NUMBER,
                        StoreTable.COLUMN_INTERNAL_NUMBER,
                        StoreTable.COLUMN_VISUAL_REFERENCE,
                        StoreTable.COLUMN_PHONE,
                        StoreTable.COLUMN_LAT,
                        StoreTable.COLUMN_LNG,
                        StoreTable.COLUMN_ADDRESS,
                        StoreTable.COLUMN_DISTANCE,
                        StoreTable.COLUMN_CREATED_AT,
                        StoreTable.COLUMN_UPDATED_AT,
                };

                String orderby = StoreTable.COLUMN_DISTANCE + " ASC";

                cursor = db.query(StoreTable.TABLE_NAME, projection, null, null, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Store mStore = new Store();
                    mStore.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ID_STORE)));
                    mStore.setName(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_NAME)));
                    mStore.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DETAILS)));
                    mStore.setStreet(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_STREET)));
                    mStore.setExternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_EXTERNAL_NUMBER)));
                    mStore.setInternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_INTERNAL_NUMBER)));
                    mStore.setVisualReference(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_VISUAL_REFERENCE)));
                    mStore.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_PHONE)));
                    mStore.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LAT)));
                    mStore.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LNG)));
                    mStore.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ADDRESS)));
                    mStore.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DISTANCE)));
                    mStore.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_CREATED_AT)));
                    mStore.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_UPDATED_AT)));

                    mStoreList.add(mStore);
                }

                return mStoreList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getStores", "Error getStores: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }


    /***
     * This method return the Stores list by productId
     *
     * @param mContext is the context App
     * @param districtId is the Id of district
     * @return a store list
     ***/
    public static synchronized List<Store> getStoresByDistrictId(Context mContext, String districtId) {
        if (mContext != null && !TextUtils.isEmpty(districtId)) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Store> mStoreList = new ArrayList<>();

                String[] projection = {
                        StoreTable.COLUMN_ID_STORE,
                        StoreTable.COLUMN_NAME,
                        StoreTable.COLUMN_DETAILS,
                        StoreTable.COLUMN_STREET,
                        StoreTable.COLUMN_EXTERNAL_NUMBER,
                        StoreTable.COLUMN_INTERNAL_NUMBER,
                        StoreTable.COLUMN_VISUAL_REFERENCE,
                        StoreTable.COLUMN_PHONE,
                        StoreTable.COLUMN_LAT,
                        StoreTable.COLUMN_LNG,
                        StoreTable.COLUMN_ADDRESS,
                        StoreTable.COLUMN_DISTANCE,
                        StoreTable.COLUMN_CREATED_AT,
                        StoreTable.COLUMN_UPDATED_AT,
                };

                String selection = StoreTable.COLUMN_ID_DISTRICT + " = ?";
                String[] selectionArgs = {districtId};
                String orderby = StoreTable.COLUMN_DISTANCE + " ASC";

                cursor = db.query(StoreTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Store mStore = new Store();
                    mStore.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ID_STORE)));
                    mStore.setName(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_NAME)));
                    mStore.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DETAILS)));
                    mStore.setStreet(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_STREET)));
                    mStore.setExternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_EXTERNAL_NUMBER)));
                    mStore.setInternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_INTERNAL_NUMBER)));
                    mStore.setVisualReference(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_VISUAL_REFERENCE)));
                    mStore.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_PHONE)));
                    mStore.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LAT)));
                    mStore.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LNG)));
                    mStore.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ADDRESS)));
                    mStore.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DISTANCE)));
                    mStore.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_CREATED_AT)));
                    mStore.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_UPDATED_AT)));

                    mStoreList.add(mStore);
                }

                return mStoreList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getStoresByDistrictId", "Error getStoresByDistrictId: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return the Stores list by productId
     *
     * @param mContext is the context App
     * @param productId is the Id of product
     * @return a store list
     ***/
    public static synchronized List<Store> getStoresByProductId(Context mContext, int productId) {
        if (mContext != null && productId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Store> mStoreList = new ArrayList<>();

                String[] projection = {
                        StoreTable.COLUMN_ID_STORE,
                        StoreTable.COLUMN_NAME,
                        StoreTable.COLUMN_DETAILS,
                        StoreTable.COLUMN_STREET,
                        StoreTable.COLUMN_EXTERNAL_NUMBER,
                        StoreTable.COLUMN_INTERNAL_NUMBER,
                        StoreTable.COLUMN_VISUAL_REFERENCE,
                        StoreTable.COLUMN_PHONE,
                        StoreTable.COLUMN_LAT,
                        StoreTable.COLUMN_LNG,
                        StoreTable.COLUMN_ADDRESS,
                        StoreTable.COLUMN_DISTANCE,
                        StoreTable.COLUMN_CREATED_AT,
                        StoreTable.COLUMN_UPDATED_AT,
                };

                String selection = StoreTable.COLUMN_ID_PRODUCT + " = ?";
                String[] selectionArgs = {String.valueOf(productId)};
                String orderby = StoreTable.COLUMN_DISTANCE + " ASC";

                cursor = db.query(StoreTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Store mStore = new Store();
                    mStore.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ID_STORE)));
                    mStore.setName(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_NAME)));
                    mStore.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DETAILS)));
                    mStore.setStreet(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_STREET)));
                    mStore.setExternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_EXTERNAL_NUMBER)));
                    mStore.setInternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_INTERNAL_NUMBER)));
                    mStore.setVisualReference(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_VISUAL_REFERENCE)));
                    mStore.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_PHONE)));
                    mStore.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LAT)));
                    mStore.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LNG)));
                    mStore.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ADDRESS)));
                    mStore.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DISTANCE)));
                    mStore.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_CREATED_AT)));
                    mStore.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_UPDATED_AT)));

                    mStoreList.add(mStore);
                }

                return mStoreList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getStoresByProductId", "Error getStoresByProductId: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return the Stores list by stateId
     *
     * @param mContext is the context App
     * @param stateId is the Id of product
     * @return a store list
     ***/
    public static synchronized List<Store> getStoresByStateId(Context mContext, int stateId) {
        if (mContext != null && stateId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Store> mStoreList = new ArrayList<>();

                String[] projection = {
                        StoreTable.COLUMN_ID_STORE,
                        StoreTable.COLUMN_NAME,
                        StoreTable.COLUMN_DETAILS,
                        StoreTable.COLUMN_STREET,
                        StoreTable.COLUMN_EXTERNAL_NUMBER,
                        StoreTable.COLUMN_INTERNAL_NUMBER,
                        StoreTable.COLUMN_VISUAL_REFERENCE,
                        StoreTable.COLUMN_PHONE,
                        StoreTable.COLUMN_LAT,
                        StoreTable.COLUMN_LNG,
                        StoreTable.COLUMN_ADDRESS,
                        StoreTable.COLUMN_DISTANCE,
                        StoreTable.COLUMN_CREATED_AT,
                        StoreTable.COLUMN_UPDATED_AT,
                };

                String selection = StoreTable.COLUMN_ID_STATE + " = ?";
                String[] selectionArgs = {String.valueOf(stateId)};
                String orderby = StoreTable.COLUMN_DISTANCE + " ASC";

                cursor = db.query(StoreTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Store mStore = new Store();
                    mStore.setId(cursor.getInt(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ID_STORE)));
                    mStore.setName(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_NAME)));
                    mStore.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DETAILS)));
                    mStore.setStreet(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_STREET)));
                    mStore.setExternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_EXTERNAL_NUMBER)));
                    mStore.setInternalNumber(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_INTERNAL_NUMBER)));
                    mStore.setVisualReference(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_VISUAL_REFERENCE)));
                    mStore.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_PHONE)));
                    mStore.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LAT)));
                    mStore.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_LNG)));
                    mStore.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_ADDRESS)));
                    mStore.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_DISTANCE)));
                    mStore.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_CREATED_AT)));
                    mStore.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(StoreTable.COLUMN_UPDATED_AT)));

                    mStoreList.add(mStore);
                }

                return mStoreList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getStoresByStateId", "Error getStoresByStateId: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ COMPANIES **********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of companies
     *
     * @param mContext is the context app
     * @param mCompanies are the company list
     * ***/
    public static synchronized void saveCompanies(Context mContext, List<Company> mCompanies) {
        if (mContext != null && mCompanies != null) {
            for (Company mCompany : mCompanies) {
                saveCompany(mContext, mCompany);
            }
        }
    }

    /***
     * This method save a Company
     *
     * @param mContext is the context App
     * @param mCompany    is the Company to save
     ***/
    private static synchronized boolean saveCompany(Context mContext, Company mCompany) {
        if (mContext != null && mCompany != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(CompanyTable.COLUMN_ID_COMPANY, mCompany.getId());
                values.put(CompanyTable.COLUMN_NAME, mCompany.getName());
                values.put(CompanyTable.COLUMN_DETAILS, mCompany.getDetails());
                values.put(CompanyTable.COLUMN_CREATED_AT, mCompany.getCreatedAt());
                values.put(CompanyTable.COLUMN_UPDATED_AT, mCompany.getUpdatedAt());

                long newRowId = db.insertWithOnConflict(CompanyTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveCompany", "Error saveCompany: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the company list
     *
     * @param mContext is the context App
     * @return a company list
     ***/
    public static synchronized List<Company> getCompanies(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Company> mCompanyList = new ArrayList<>();

                String[] projection = {
                        CompanyTable.COLUMN_ID_COMPANY,
                        CompanyTable.COLUMN_NAME,
                        CompanyTable.COLUMN_DETAILS,
                        CompanyTable.COLUMN_CREATED_AT,
                        CompanyTable.COLUMN_UPDATED_AT
                };

                String orderby = CompanyTable.COLUMN_NAME + " ASC";
                cursor = db.query(CompanyTable.TABLE_NAME, projection, null, null, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Company mCompany = new Company();
                    mCompany.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_ID_COMPANY)));
                    mCompany.setName(cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_NAME)));
                    mCompany.setDetails(cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_DETAILS)));
                    mCompany.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_CREATED_AT)));
                    mCompany.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(CompanyTable.COLUMN_UPDATED_AT)));

                    mCompanyList.add(mCompany);
                }

                return mCompanyList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getCompany", "Error getCompany: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ SUGGESTIONS ********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of suggestions
     *
     * @param mContext is the context app
     * @param mSuggestions are the suggestion list
     * ***/
    public static synchronized void saveSuggestions(Context mContext, List<Suggestion> mSuggestions) {
        if (mContext != null && mSuggestions != null) {
            for (Suggestion mSuggestion : mSuggestions) {
                saveSuggestion(mContext, mSuggestion);
            }
        }
    }

    /***
     * This method save a Suggestion
     *
     * @param mContext is the context App
     * @param mSuggestion    is the Suggestion to save
     ***/
    private static synchronized boolean saveSuggestion(Context mContext, Suggestion mSuggestion) {
        if (mContext != null && mSuggestion != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(SuggestionTable.COLUMN_ID_SUGGESTION, mSuggestion.getId());
                values.put(SuggestionTable.COLUMN_TITLE, mSuggestion.getTitle());
                values.put(SuggestionTable.COLUMN_COLOR, mSuggestion.getColor());

                long newRowId = db.insertWithOnConflict(SuggestionTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save items
                saveItems(mContext, mSuggestion.getItems(), mSuggestion.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveSuggestion", "Error saveSuggestion: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the suggestion list
     *
     * @param mContext is the context App
     * @return a suggestion list
     ***/
    public static synchronized List<Suggestion> getSuggestions(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Suggestion> mSuggestionList = new ArrayList<>();

                String[] projection = {
                        SuggestionTable.COLUMN_ID_SUGGESTION,
                        SuggestionTable.COLUMN_TITLE,
                        SuggestionTable.COLUMN_COLOR
                };

                //String orderby = SuggestionTable.COLUMN_TITLE + " ASC";
                cursor = db.query(SuggestionTable.TABLE_NAME, projection, null, null, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    Suggestion mSuggestion = new Suggestion();
                    mSuggestion.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SuggestionTable.COLUMN_ID_SUGGESTION)));
                    mSuggestion.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(SuggestionTable.COLUMN_TITLE)));
                    mSuggestion.setColor(cursor.getString(cursor.getColumnIndexOrThrow(SuggestionTable.COLUMN_COLOR)));

                    //here setup items
                    mSuggestion.setItems(getItems(mContext, mSuggestion.getId()));

                    mSuggestionList.add(mSuggestion);
                }

                return mSuggestionList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getSuggestions", "Error getSuggestions: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return delete a assignment
     *
     * @param mContext           is the context App
     ***/
    public static synchronized boolean deleteAllSuggestions(Context mContext) {
        if (mContext != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                int deleteRow = db.delete(SuggestionTable.TABLE_NAME, null, null);

                return deleteRow != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "deleteSuggestions", "Error get deleteSuggestions: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }


    /***********************************************************************************************
     * ************************************ SOLUTIONS **********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of solutions
     *
     * @param mContext is the context app
     * @param mSections are the solution section list
     * ***/
    public static synchronized void saveSolutionSections(Context mContext, List<SolutionSection> mSections) {
        if (mContext != null && mSections != null) {
            for (SolutionSection mSection : mSections) {
                saveSolutionSection(mContext, mSection);
            }
        }
    }

    /***
     * This method save a Solutiom
     *
     * @param mContext is the context App
     * @param mSection    is the Solution section to save
     ***/
    private static synchronized boolean saveSolutionSection(Context mContext, SolutionSection mSection) {
        if (mContext != null && mSection != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(SolutionSectionTable.COLUMN_ID_SECTION, mSection.getId());
                values.put(SolutionSectionTable.COLUMN_TITLE, mSection.getTitle());

                long newRowId = db.insertWithOnConflict(SolutionSectionTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save solution items
                saveSolutions(mContext, mSection.getItems(), mSection.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveSolutionSection", "Error saveSolutionSection: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method save a list of solutions
     *
     * @param mContext is the context app
     * @param mSolutions are the solution list
     * @param sectionId is the current sectionID of solution
     *
     * ***/
    public static synchronized void saveSolutions(Context mContext, List<Solution> mSolutions, int sectionId) {
        if (mContext != null && mSolutions != null && sectionId > 0) {
            for (Solution mSolution : mSolutions) {
                saveSolution(mContext, mSolution, sectionId);
            }
        }
    }

    /***
     * This method save a Solutiom
     *
     * @param mContext is the context App
     * @param mSolution    is the Solution to save
     * @param sectionId is the current sectionID of solution
     ***/
    private static synchronized boolean saveSolution(Context mContext, Solution mSolution, int sectionId) {
        if (mContext != null && mSolution != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(SolutionTable.COLUMN_ID_SOLUTION, mSolution.getId());
                values.put(SolutionTable.COLUMN_TITLE, mSolution.getTitle());
                values.put(SolutionTable.COLUMN_IMAGE, mSolution.getImage());

                values.put(SolutionTable.COLUMN_ID_SECTION, sectionId);

                long newRowId = db.insertWithOnConflict(SolutionTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save items
                saveItems(mContext, mSolution.getItems(), mSolution.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveSolution", "Error saveSolution: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the solution section list
     *
     * @param mContext is the context App
     * @return a solution list
     ***/
    public static synchronized List<SolutionSection> getSolutionSections(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<SolutionSection> mSectionList = new ArrayList<>();

                String[] projection = {
                        SolutionSectionTable.COLUMN_ID_SECTION,
                        SolutionSectionTable.COLUMN_TITLE
                };

                //String orderby = SuggestionTable.COLUMN_TITLE + " ASC";
                cursor = db.query(SolutionSectionTable.TABLE_NAME, projection, null, null, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    SolutionSection mSection = new SolutionSection();
                    mSection.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SolutionSectionTable.COLUMN_ID_SECTION)));
                    mSection.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(SolutionSectionTable.COLUMN_TITLE)));

                    //here setup items
                    mSection.setItems(getSolutions(mContext, mSection.getId()));

                    mSectionList.add(mSection);
                }

                return mSectionList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getSolutionSections", "Error getSolutionSections: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return the solution list
     *
     * @param mContext is the context App
     * @param sectionId is the current section Id to get
     * @return a solution list
     ***/
    private static synchronized List<Solution> getSolutions(Context mContext, int sectionId) {
        if (mContext != null && sectionId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Solution> mSolutionList = new ArrayList<>();

                String[] projection = {
                        SolutionTable.COLUMN_ID_SOLUTION,
                        SolutionTable.COLUMN_TITLE,
                        SolutionTable.COLUMN_IMAGE
                };

                //String orderby = SuggestionTable.COLUMN_TITLE + " ASC";
                String selection = SolutionTable.COLUMN_ID_SECTION + " = ?";
                String[] selectionArgs = new String[]{String.valueOf(sectionId)};
                cursor = db.query(SolutionTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    Solution mSolution = new Solution();
                    mSolution.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SolutionTable.COLUMN_ID_SOLUTION)));
                    mSolution.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(SolutionTable.COLUMN_TITLE)));
                    mSolution.setImage(cursor.getString(cursor.getColumnIndexOrThrow(SolutionTable.COLUMN_IMAGE)));

                    //here setup items
                    mSolution.setItems(getItems(mContext, mSolution.getId()));

                    mSolutionList.add(mSolution);
                }

                return mSolutionList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getSolutions", "Error getSolutions: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return delete a assignment
     *
     * @param mContext           is the context App
     ***/
    public static synchronized boolean deleteAllSolution(Context mContext) {
        if (mContext != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                int deleteRow = db.delete(SolutionTable.TABLE_NAME, null, null);

                return deleteRow != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "deleteAllSolution", "Error get deleteAllSolution: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return delete a assignment
     *
     * @param mContext           is the context App
     ***/
    public static synchronized boolean deleteAllSolutionSections(Context mContext) {
        if (mContext != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                int deleteRow = db.delete(SolutionSectionTable.TABLE_NAME, null, null);

                return deleteRow != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "deleteAllSolutionSections", "Error get deleteAllSolutionSections: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***********************************************************************************************
     * ************************************ ITEMS **************************************************
     * ********************************************************************************************/

    /***
     * This method save a list of items
     *
     * @param mContext is the context app
     * @param mItems are the item list
     * @param idSolution is the current id suggestion
     * ***/
    public static synchronized void saveItems(Context mContext, List<Item> mItems, int idSolution) {
        if (mContext != null && mItems != null && idSolution > -1) {
            for (Item mItem : mItems) {
                saveItem(mContext, mItem, idSolution);
            }
        }
    }

    /***
     * This method save an Item
     *
     * @param mContext is the context App
     * @param mItem    is the Item to save
     * @param idSolution is the current id solution
     ***/
    private static synchronized boolean saveItem(Context mContext, Item mItem, int idSolution) {
        if (mContext != null && mItem != null && idSolution > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                String idItem = getIdItem(idSolution, mItem.getId());
                values.put(ItemTable.COLUMN_ID, idItem);
                values.put(ItemTable.COLUMN_ID_ITEM, mItem.getId());
                values.put(ItemTable.COLUMN_TITLE, mItem.getTitle());
                values.put(ItemTable.COLUMN_IMAGE, mItem.getImage());
                values.put(ItemTable.COLUMN_CAPTION, mItem.getCaption());
                //values.put(ItemTable.COLUMN_CAUSE, mItem.getCause());
                values.put(ItemTable.COLUMN_GLOSSARY, mItem.getGlossary());
                values.put(ItemTable.COLUMN_DEMO, mItem.getDemo());
                values.put(ItemTable.COLUMN_TYPE, mItem.getType());

                values.put(ItemTable.COLUMN_ID_SOLUTION, idSolution);

                long newRowId = db.insertWithOnConflict(ItemTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save products and stgeps
                saveProductItems(mContext, mItem.getProducts(), idItem);
                saveSteps(mContext, mItem.getSteps(), idItem);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveItem", "Error saveItem: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the item list by id suggestion
     *
     * @param mContext is the context App
     * @param idSolution is the current id solution
     * @return an Item list
     ***/
    public static synchronized List<Item> getItems(Context mContext, int idSolution) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Item> mItemList = new ArrayList<>();

                String[] projection = {
                        ItemTable.COLUMN_ID_ITEM,
                        ItemTable.COLUMN_TITLE,
                        ItemTable.COLUMN_IMAGE,
                        ItemTable.COLUMN_CAPTION,
                        //ItemTable.COLUMN_CAUSE,
                        ItemTable.COLUMN_GLOSSARY,
                        ItemTable.COLUMN_DEMO,
                        ItemTable.COLUMN_TYPE
                };

                String selection = ItemTable.COLUMN_ID_SOLUTION + " = ?";
                String[] selectionArgs = new String[]{String.valueOf(idSolution)};
                String orderby = ItemTable.COLUMN_TITLE + " ASC";
                cursor = db.query(ItemTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderby);

                while (cursor != null && cursor.moveToNext()) {
                    Item mItem = new Item();
                    mItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_ID_ITEM)));
                    mItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_TITLE)));
                    mItem.setImage(cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_IMAGE)));
                    mItem.setCaption(cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_CAPTION)));
                    //mItem.setCause(cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_CAUSE)));
                    mItem.setGlossary(cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_GLOSSARY)));
                    mItem.setDemo(cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_DEMO)));
                    mItem.setType(cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_TYPE)));

                    //here setup product and items
                    mItem.setProducts(getProductItems(mContext, getIdItem(idSolution, mItem.getId())));
                    mItem.setSteps(getSteps(mContext, getIdItem(idSolution, mItem.getId())));

                    mItemList.add(mItem);
                }

                return mItemList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getItems", "Error getItems: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    private static String getIdItem(int idSuggestion, int idItem) {
        return PRE_ITEM + idSuggestion + "_" + idItem;
    }


    /***********************************************************************************************
     * ************************************ PRODUCT ITEMS ******************************************
     * ********************************************************************************************/

    /***
     * This method save a list of Products
     *
     * @param mContext is the context app
     * @param mProductItems is the Product list
     * @param idItem id of the current item
     * ***/
    public static synchronized void saveProductItems(Context mContext, List<ProductItem> mProductItems, String idItem) {
        if (mContext != null && mProductItems != null && !TextUtils.isEmpty(idItem)) {
            for (ProductItem mProduct : mProductItems) {
                saveProductItem(mContext, mProduct, idItem);
            }
        }
    }

    /***
     * This method save a Product Item
     *
     * @param mContext is the context App
     * @param mProduct    is the Product to save
     * @param idItem is the current id item
     ***/
    private static synchronized boolean saveProductItem(Context mContext, ProductItem mProduct, String idItem) {
        if (mContext != null && !TextUtils.isEmpty(idItem)) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ProductItemTable.COLUMN_ID_PRODUCT, idItem + "_" + mProduct.getId());
                values.put(ProductItemTable.COLUMN_ID_PROD, mProduct.getId());
                values.put(ProductItemTable.COLUMN_TITLE, mProduct.getTitle());
                values.put(ProductItemTable.COLUMN_IMAGE, mProduct.getImage());

                values.put(ProductItemTable.COLUMN_ID, idItem);

                long newRowId = db.insertWithOnConflict(ProductItemTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveProductItem", "Error saveProductItem: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the Product Item list
     *
     * @param mContext is the context App
     * @param idItem is the current id item
     * @return a product item list
     ***/
    private static synchronized List<ProductItem> getProductItems(Context mContext, String idItem) {
        if (mContext != null && !TextUtils.isEmpty(idItem)) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<ProductItem> mProductList = new ArrayList<>();

                String[] projection = {
                        ProductItemTable.COLUMN_ID_PROD,
                        ProductItemTable.COLUMN_TITLE,
                        ProductItemTable.COLUMN_IMAGE
                };

                String selection = ProductItemTable.COLUMN_ID + " = ?";
                String[] selectionArgs = new String[]{idItem};
                //String orderby = ProductItemTable.COLUMN_NAME + " ASC";
                cursor = db.query(ProductItemTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                while (cursor.moveToNext()) {
                    ProductItem mProductItem = new ProductItem();
                    mProductItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ProductItemTable.COLUMN_ID_PROD)));
                    mProductItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(ProductItemTable.COLUMN_TITLE)));
                    mProductItem.setImage(cursor.getString(cursor.getColumnIndexOrThrow(ProductItemTable.COLUMN_IMAGE)));

                    mProductList.add(mProductItem);
                }

                return mProductList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getProductItems", "Error getProductItems: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    private static String getIdProductItem(int idProduct, int idItem) {
        return PRE_PRODUCT_ITEM + idProduct + "_" + idItem;
    }

    /***********************************************************************************************
     * ************************************ STEPS **************************************************
     * ********************************************************************************************/

    /***
     * This method save a list of stpes
     *
     * @param mContext is the context app
     * @param mSteps is the Step list
     * @param idItem id of the current item
     * ***/
    public static synchronized void saveSteps(Context mContext, List<Step> mSteps, String idItem) {
        if (mContext != null && mSteps != null && !TextUtils.isEmpty(idItem)) {
            for (Step mStep : mSteps) {
                saveStep(mContext, mStep, idItem);
            }
        }
    }

    /***
     * This method save a Step
     *
     * @param mContext is the context App
     * @param mStep    is the Step to save
     * @param idItem is the current id item
     ***/
    private static synchronized boolean saveStep(Context mContext, Step mStep, String idItem) {
        if (mContext != null && mStep != null && !TextUtils.isEmpty(idItem)) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(StepTable.COLUMN_IMAGE, mStep.getImage());
                values.put(StepTable.COLUMN_CAPTION, mStep.getCaption());

                values.put(StepTable.COLUMN_ID, idItem);

                long newRowId = db.insertWithOnConflict(StepTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveStep", "Error saveStep: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the Product Item list
     *
     * @param mContext is the context App
     * @param idItem is the current id item
     * @return a step list
     ***/
    public static synchronized List<Step> getSteps(Context mContext, String idItem) {
        if (mContext != null && !TextUtils.isEmpty(idItem)) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Step> mStepList = new ArrayList<>();

                String[] projection = {
                        StepTable.COLUMN_IMAGE,
                        StepTable.COLUMN_CAPTION
                };

                String selection = StepTable.COLUMN_ID + " = ?";
                String[] selectionArgs = new String[]{idItem};
                cursor = db.query(StepTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    Step mStep = new Step();
                    mStep.setImage(cursor.getString(cursor.getColumnIndexOrThrow(StepTable.COLUMN_IMAGE)));
                    mStep.setCaption(cursor.getString(cursor.getColumnIndexOrThrow(StepTable.COLUMN_CAPTION)));

                    mStepList.add(mStep);
                }

                return mStepList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getSteps", "Error getSteps: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }


    /***********************************************************************************************
     * ************************************ PROMOTIONS *********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of solutions
     *
     * @param mContext is the context app
     * @param mPromotions are the mPromotions list
     * ***/
    public static synchronized void savePromotions(Context mContext, List<Promotion> mPromotions) {
        if (mContext != null && mPromotions != null) {
            for (Promotion mPromotion : mPromotions) {
                savePromotion(mContext, mPromotion);
            }
        }
    }

    /***
     * This method save a Solutiom
     *
     * @param mContext is the context App
     * @param mPromotion    is the Solution to save
     ***/
    private static synchronized boolean savePromotion(Context mContext, Promotion mPromotion) {
        if (mContext != null && mPromotion != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(PromotionTable.COLUMN_IMAGE, mPromotion.getImage());
                values.put(PromotionTable.COLUMN_DESCRIPTION, mPromotion.getDescription());
                values.put(PromotionTable.COLUMN_LINK, mPromotion.getLink());

                long newRowId = db.insertWithOnConflict(PromotionTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "savePromotion", "Error savePromotion: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the solution list
     *
     * @param mContext is the context App
     * @return a solution list
     ***/
    public static synchronized List<Promotion> getPromotions(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Promotion> mPromotionList = new ArrayList<>();

                String[] projection = {
                        PromotionTable.COLUMN_IMAGE,
                        PromotionTable.COLUMN_DESCRIPTION,
                        PromotionTable.COLUMN_LINK
                };

                //String orderby = SuggestionTable.COLUMN_TITLE + " ASC";
                cursor = db.query(PromotionTable.TABLE_NAME, projection, null, null, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    Promotion mPromotion = new Promotion();
                    mPromotion.setImage(cursor.getString(cursor.getColumnIndexOrThrow(PromotionTable.COLUMN_IMAGE)));
                    mPromotion.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(PromotionTable.COLUMN_DESCRIPTION)));
                    mPromotion.setLink(cursor.getString(cursor.getColumnIndexOrThrow(PromotionTable.COLUMN_LINK)));

                    mPromotionList.add(mPromotion);
                }

                return mPromotionList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getPromotions", "Error getPromotions: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return delete a assignment
     *
     * @param mContext           is the context App
     ***/
    public static synchronized boolean deleteAllPromotion(Context mContext) {
        if (mContext != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                int deleteRow = db.delete(PromotionTable.TABLE_NAME, null, null);

                return deleteRow != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "deleteAllPromotion", "Error get deleteAllPromotion: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***********************************************************************************************
     * ************************************ SLIDER ITEMS *******************************************
     * ********************************************************************************************/

    /***
     * This method save a list of solutions
     *
     * @param mContext is the context app
     * @param mItems are the Slider items list
     * ***/
    public static synchronized void saveSliderItems(Context mContext, List<SliderItem> mItems) {
        if (mContext != null && mItems != null) {
            for (SliderItem mItem : mItems) {
                saveSliderItem(mContext, mItem);
            }
        }
    }

    /***
     * This method save a SliderItem
     *
     * @param mContext is the context App
     * @param mItem    is the Solution to save
     ***/
    private static synchronized boolean saveSliderItem(Context mContext, SliderItem mItem) {
        if (mContext != null && mItem != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(SliderItemTable.COLUMN_ID, mItem.getId());
                values.put(SliderItemTable.COLUMN_IMAGE, mItem.getImage());
                values.put(SliderItemTable.COLUMN_TYPE, mItem.getType());

                long newRowId = db.insertWithOnConflict(SliderItemTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save de metadata
                saveSliderMetadata(mContext, mItem.getMetadata(), mItem.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveSliderItem", "Error saveSliderItem: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the slider item list
     *
     * @param mContext is the context App
     * @return a sliderItem list
     ***/
    public static synchronized List<SliderItem> getSliderItems(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<SliderItem> mItems = new ArrayList<>();

                String[] projection = {
                        SliderItemTable.COLUMN_ID,
                        SliderItemTable.COLUMN_IMAGE,
                        SliderItemTable.COLUMN_TYPE
                };

                cursor = db.query(SliderItemTable.TABLE_NAME, projection, null, null, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    SliderItem item = new SliderItem();
                    item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SliderItemTable.COLUMN_ID)));
                    item.setImage(cursor.getString(cursor.getColumnIndexOrThrow(SliderItemTable.COLUMN_IMAGE)));
                    item.setType(cursor.getString(cursor.getColumnIndexOrThrow(SliderItemTable.COLUMN_TYPE)));

                    //Here setup metadata
                    item.setMetadata(getSliderMetadata(mContext, item.getId()));

                    mItems.add(item);
                }

                return mItems;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getSliderItems", "Error getSliderItems: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return delete a assignment
     *
     * @param mContext           is the context App
     ***/
    public static synchronized boolean deleteAllSliderItems(Context mContext) {
        if (mContext != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                int deleteRow = db.delete(SliderItemTable.TABLE_NAME, null, null);

                return deleteRow != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "deleteAllItems", "Error get deleteAllItems: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***********************************************************************************************
     * ************************************ METADATA ITEM ******************************************
     * ********************************************************************************************/

    /***
     * This method save a SliderItem
     *
     * @param mContext is the context App
     * @param mMetadata    is the Metadata to save
     ***/
    private static synchronized boolean saveSliderMetadata(Context mContext, SliderMetadata mMetadata, int idSliderItem) {
        if (mContext != null && mMetadata != null && idSliderItem > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(SliderMetadataTable.COLUMN_LINK, mMetadata.getLink());
                values.put(SliderMetadataTable.COLUMN_DESCRIPTION, mMetadata.getDescription());
                values.put(SliderMetadataTable.COLUMN_ID_PRODUCT, mMetadata.getId());
                values.put(SliderMetadataTable.COLUMN_NAME, mMetadata.getName());

                values.put(SliderMetadataTable.COLUMN_ID, idSliderItem);

                long newRowId = db.insertWithOnConflict(SliderMetadataTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveSliderMetadata", "Error saveSliderMetadata: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the slider item list
     *
     * @param mContext is the context App
     * @return a sliderItem list
     ***/
    public static synchronized SliderMetadata getSliderMetadata(Context mContext, int idSliderItem) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                SliderMetadata metadata = null;

                String[] projection = {
                        SliderMetadataTable.COLUMN_LINK,
                        SliderMetadataTable.COLUMN_DESCRIPTION,
                        SliderMetadataTable.COLUMN_ID,
                        SliderMetadataTable.COLUMN_NAME
                };

                String selection = SliderMetadataTable.COLUMN_ID + " = ?";
                String[] selectionArgs = {String.valueOf(idSliderItem)};
                cursor = db.query(SliderMetadataTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    metadata = new SliderMetadata();
                    metadata.setLink(cursor.getString(cursor.getColumnIndexOrThrow(SliderMetadataTable.COLUMN_LINK)));
                    metadata.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(SliderMetadataTable.COLUMN_DESCRIPTION)));
                    metadata.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SliderMetadataTable.COLUMN_ID)));
                    metadata.setName(cursor.getString(cursor.getColumnIndexOrThrow(SliderMetadataTable.COLUMN_NAME)));

                } else {
                    LoggerUtils.logError(TAG, "getSliderMetadata", "Error empty metadata");
                }

                return metadata;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getSliderMetadata", "Error getSliderMetadata: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ PHOTO **************************************************
     * ********************************************************************************************/

    /***
     * This method save a PHOTO
     *
     * @param mContext is the context App
     * @param mPhoto    is the PHOTO to save
     ***/
    private static synchronized boolean savePhoto(Context mContext, Photo mPhoto, int productId) {
        if (mContext != null && mPhoto != null && productId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(PhotoTable.COLUMN_ID_PHOTO, mPhoto.getId());
                values.put(PhotoTable.COLUMN_NAME, mPhoto.getName());
                values.put(PhotoTable.COLUMN_SIZE, mPhoto.getSize());
                values.put(PhotoTable.COLUMN_MIME, mPhoto.getMime());
                values.put(PhotoTable.COLUMN_GROUP, mPhoto.getGroup());
                values.put(PhotoTable.COLUMN_WIDTH, mPhoto.getWidth());
                values.put(PhotoTable.COLUMN_HEIGHT, mPhoto.getHeight());
                values.put(PhotoTable.COLUMN_CREATED_AT, mPhoto.getCreatedAt());
                values.put(PhotoTable.COLUMN_UPDATED_AT, mPhoto.getUpdatedAt());
                values.put(PhotoTable.COLUMN_URL, mPhoto.getUrl());

                values.put(PhotoTable.COLUMN_ID_PRODUCT, productId);

                long newRowId = db.insertWithOnConflict(PhotoTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                //Here save thumbnails
                saveThumbnails(mContext, mPhoto.getThumbnails(), mPhoto.getId());

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "savePhoto", "Error savePhoto: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the photo by id product
     *
     * @param mContext is the context App
     * @return a sliderItem list
     ***/
    public static synchronized Photo getPhoto(Context mContext, int productId) {
        if (mContext != null && productId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                Photo mPhoto = null;

                String[] projection = {
                        PhotoTable.COLUMN_ID_PHOTO,
                        PhotoTable.COLUMN_NAME,
                        PhotoTable.COLUMN_SIZE,
                        PhotoTable.COLUMN_MIME,
                        PhotoTable.COLUMN_GROUP,
                        PhotoTable.COLUMN_WIDTH,
                        PhotoTable.COLUMN_HEIGHT,
                        PhotoTable.COLUMN_CREATED_AT,
                        PhotoTable.COLUMN_UPDATED_AT,
                        PhotoTable.COLUMN_URL
                };

                String selection = PhotoTable.COLUMN_ID_PRODUCT + " = ?";
                String[] selectionArgs = {String.valueOf(productId)};
                cursor = db.query(PhotoTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    mPhoto = new Photo();
                    mPhoto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_ID_PHOTO)));
                    mPhoto.setName(cursor.getString(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_NAME)));
                    mPhoto.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_SIZE)));
                    mPhoto.setMime(cursor.getString(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_MIME)));
                    mPhoto.setGroup(cursor.getString(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_GROUP)));
                    mPhoto.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_WIDTH)));
                    mPhoto.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_HEIGHT)));
                    mPhoto.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_CREATED_AT)));
                    mPhoto.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_UPDATED_AT)));
                    mPhoto.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(PhotoTable.COLUMN_URL)));

                    //Here set thumbnails
                    mPhoto.setThumbnails(getThumbnails(mContext, mPhoto.getId()));
                } else {
                    LoggerUtils.logError(TAG, "getPhoto", "Error empty photo");
                }

                return mPhoto;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getPhoto", "Error getPhoto: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ THUMBNAILS *********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of thumbnails
     *
     * @param mContext is the context app
     * @param mThumbnails are the thumbnail list
     * @param photoId is the current id photo
     * ***/
    public static synchronized void saveThumbnails(Context mContext, List<Thumbnail> mThumbnails, int photoId) {
        if (mContext != null && mThumbnails != null && photoId > -1) {
            for (Thumbnail mThumbnail : mThumbnails) {
                saveThumbnail(mContext, mThumbnail, photoId);
            }
        }
    }

    /***
     * This method save a SliderItem
     *
     * @param mContext is the context App
     * @param mThumbnail    is the Solution to save
     * @param photoId is the current id photo
     ***/
    private static synchronized boolean saveThumbnail(Context mContext, Thumbnail mThumbnail, int photoId) {
        if (mContext != null && mThumbnail != null && photoId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ThumbnailTable.COLUMN_ID_THUMBNAIL, mThumbnail.getId());
                values.put(ThumbnailTable.COLUMN_NAME, mThumbnail.getName());
                values.put(ThumbnailTable.COLUMN_SIZE, mThumbnail.getSize());
                values.put(ThumbnailTable.COLUMN_MIME, mThumbnail.getMime());
                values.put(ThumbnailTable.COLUMN_GROUP, mThumbnail.getGroup());
                values.put(ThumbnailTable.COLUMN_WIDTH, mThumbnail.getWidth());
                values.put(ThumbnailTable.COLUMN_HEIGHT, mThumbnail.getHeight());
                values.put(ThumbnailTable.COLUMN_CREATED_AT, mThumbnail.getCreatedAt());
                values.put(ThumbnailTable.COLUMN_UPDATED_AT, mThumbnail.getUpdatedAt());
                values.put(ThumbnailTable.COLUMN_URL, mThumbnail.getUrl());

                values.put(ThumbnailTable.COLUMN_ID_PHOTO, photoId);

                long newRowId = db.insertWithOnConflict(ThumbnailTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveThumbnail", "Error saveThumbnail: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the thumbnails list
     *
     * @param mContext is the context App
     * @param photoId is the current id photo
     * @return a Thumbnail list
     ***/
    public static synchronized List<Thumbnail> getThumbnails(Context mContext, int photoId) {
        if (mContext != null && photoId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Thumbnail> mThumbnailList = new ArrayList<>();

                String[] projection = {
                        ThumbnailTable.COLUMN_ID_PHOTO,
                        ThumbnailTable.COLUMN_NAME,
                        ThumbnailTable.COLUMN_SIZE,
                        ThumbnailTable.COLUMN_MIME,
                        ThumbnailTable.COLUMN_GROUP,
                        ThumbnailTable.COLUMN_WIDTH,
                        ThumbnailTable.COLUMN_HEIGHT,
                        ThumbnailTable.COLUMN_CREATED_AT,
                        ThumbnailTable.COLUMN_UPDATED_AT,
                        ThumbnailTable.COLUMN_URL
                };

                String selection = ThumbnailTable.COLUMN_ID_PHOTO + " = ?";
                String[] selectionArgs = {String.valueOf(photoId)};
                cursor = db.query(ThumbnailTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    Thumbnail mThumbnail = new Thumbnail();
                    mThumbnail.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_ID_PHOTO)));
                    mThumbnail.setName(cursor.getString(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_NAME)));
                    mThumbnail.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_SIZE)));
                    mThumbnail.setMime(cursor.getString(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_MIME)));
                    mThumbnail.setGroup(cursor.getString(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_GROUP)));
                    mThumbnail.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_WIDTH)));
                    mThumbnail.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_HEIGHT)));
                    mThumbnail.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_CREATED_AT)));
                    mThumbnail.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_UPDATED_AT)));
                    mThumbnail.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(ThumbnailTable.COLUMN_URL)));

                    mThumbnailList.add(mThumbnail);
                }

                return mThumbnailList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getThumbnails", "Error getThumbnails: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ PRODUCT RELATED ****************************************
     * ********************************************************************************************/

    /***
     * This method save a list of product related
     *
     * @param mContext is the context app
     * @param mProducts are the product related list
     * @param productId is the current id photo
     * ***/
    public static synchronized void saveRelatedProducts(Context mContext, List<ProductRelated> mProducts, int productId) {
        if (mContext != null && mProducts != null && productId > -1) {
            for (ProductRelated mProduct : mProducts) {
                saveProductRelated(mContext, mProduct, productId);
            }
        }
    }

    /***
     * This method save a SliderItem
     *
     * @param mContext is the context App
     * @param mProduct    is the Product to save
     * @param productId is the current id product id
     ***/
    private static synchronized boolean saveProductRelated(Context mContext, ProductRelated mProduct, int productId) {
        if (mContext != null && mProduct != null && productId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ProductRelatedTable.COLUMN_ID_RELATED, productId + "_" + mProduct.getId());
                values.put(ProductRelatedTable.COLUMN_ID, mProduct.getId());
                values.put(ProductRelatedTable.COLUMN_NAME, mProduct.getName());
                values.put(ProductRelatedTable.COLUMN_PHOTO, mProduct.getPhoto());

                values.put(ProductRelatedTable.COLUMN_ID_PRODUCT, productId);

                long newRowId = db.insertWithOnConflict(ProductRelatedTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveProductRelated", "Error saveProductRelated: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the related products list
     *
     * @param mContext is the context App
     * @param productId is the current id photo
     * @return a product related list
     ***/
    public static synchronized List<ProductRelated> getRelatedProducts(Context mContext, int productId) {
        if (mContext != null && productId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<ProductRelated> mProductList = new ArrayList<>();

                String[] projection = {
                        ProductRelatedTable.COLUMN_ID,
                        ProductRelatedTable.COLUMN_NAME,
                        ProductRelatedTable.COLUMN_PHOTO
                };

                String selection = ProductRelatedTable.COLUMN_ID_PRODUCT + " = ?";
                String[] selectionArgs = {String.valueOf(productId)};
                cursor = db.query(ProductRelatedTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    ProductRelated mProduct = new ProductRelated();
                    mProduct.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ProductRelatedTable.COLUMN_ID)));
                    mProduct.setName(cursor.getString(cursor.getColumnIndexOrThrow(ProductRelatedTable.COLUMN_NAME)));
                    mProduct.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(ProductRelatedTable.COLUMN_PHOTO)));

                    mProductList.add(mProduct);
                }

                return mProductList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getRelatedProducts", "Error getRelatedProducts: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***********************************************************************************************
     * ************************************ TOOLS **************************************************
     * ********************************************************************************************/

    /***
     * This method save a list of tools
     *
     * @param mContext is the context app
     * @param mTools are the tool list
     * @param productId is the current id photo
     * ***/
    private static synchronized void saveTools(Context mContext, List<String> mTools, int productId) {
        if (mContext != null && mTools != null && productId > -1) {
            int i = 0;
            for (String tool : mTools) {
                saveTool(mContext, tool, productId, i);
            }
        }
    }

    /***
     * This method save a tool
     *
     * @param mContext is the context App
     * @param mTool    is the tool to save
     * @param productId is the current id product id
     ***/
    private static synchronized boolean saveTool(Context mContext, String mTool, int productId, int index) {
        if (mContext != null && !TextUtils.isEmpty(mTool) && productId > -1) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(ToolTable.COLUMN_ID_TOOL, productId + "_" + index);
                values.put(ToolTable.COLUMN_NAME, mTool);

                values.put(ToolTable.COLUMN_ID_PRODUCT, productId);

                long newRowId = db.insertWithOnConflict(ToolTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveTool", "Error saveTool: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the related products list
     *
     * @param mContext is the context App
     * @param productId is the current id photo
     * @return a product related list
     ***/
    private static synchronized List<String> getTools(Context mContext, int productId) {
        if (mContext != null && productId > -1) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<String> mProductList = new ArrayList<>();

                String[] projection = {
                        ToolTable.COLUMN_NAME
                };

                String selection = ToolTable.COLUMN_ID_PRODUCT + " = ?";
                String[] selectionArgs = {String.valueOf(productId)};
                cursor = db.query(ToolTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ToolTable.COLUMN_NAME));
                    mProductList.add(name);
                }

                return mProductList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getTools", "Error getTools: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }


    /***********************************************************************************************
     * ************************************ GLOSSARY ***********************************************
     * ********************************************************************************************/

    /***
     * This method save a list of glossary
     *
     * @param mContext is the context app
     * @param mGlossary are the glossary list
     * ***/
    public static synchronized void saveGlossaries(Context mContext, List<Glossary> mGlossary) {
        if (mContext != null && mGlossary != null) {
            for (Glossary word : mGlossary) {
                saveGlossary(mContext, word);
            }
        }
    }

    /***
     * This method save a Glossary
     *
     * @param mContext is the context App
     * @param mGlossary    is the Glossary to save
     ***/
    private static synchronized boolean saveGlossary(Context mContext, Glossary mGlossary) {
        if (mContext != null && mGlossary != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                ContentValues values = new ContentValues();
                values.put(GlossaryTable.COLUMN_TITLE, mGlossary.getTitle());
                values.put(GlossaryTable.COLUMN_DESCRIPTION, mGlossary.getDescription());

                long newRowId = db.insertWithOnConflict(GlossaryTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                return newRowId != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "saveGlossary", "Error saveGlossary: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }

    /***
     * This method return the glossary list
     *
     * @param mContext is the context App
     * @return a glossary list
     ***/
    public static synchronized List<Glossary> getGlossary(Context mContext) {
        if (mContext != null) {
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDb(mContext);
            try {
                List<Glossary> mGlossaryList = new ArrayList<>();

                String[] projection = {
                        GlossaryTable.COLUMN_TITLE,
                        GlossaryTable.COLUMN_DESCRIPTION
                };

                cursor = db.query(GlossaryTable.TABLE_NAME, projection, null, null, null, null, null);

                while (cursor != null && cursor.moveToNext()) {
                    Glossary mGlossary = new Glossary();
                    mGlossary.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(GlossaryTable.COLUMN_TITLE)));
                    mGlossary.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(GlossaryTable.COLUMN_DESCRIPTION)));

                    mGlossaryList.add(mGlossary);
                }

                return mGlossaryList;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "getGlossary", "Error getGlossary: " + e.getMessage());
            } finally {
                closeCursor(cursor);
                closeDb(db);
            }
        }
        return null;
    }

    /***
     * This method return delete all glossary
     *
     * @param mContext           is the context App
     ***/
    public static synchronized boolean deleteAllGlossary(Context mContext) {
        if (mContext != null) {
            SQLiteDatabase db = getWritableDb(mContext);
            try {
                int deleteRow = db.delete(GlossaryTable.TABLE_NAME, null, null);

                return deleteRow != -1;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "deleteAllGlossary", "Error get deleteAllGlossary: " + e.getMessage());
            } finally {
                closeDb(db);
            }
        }
        return false;
    }
}
