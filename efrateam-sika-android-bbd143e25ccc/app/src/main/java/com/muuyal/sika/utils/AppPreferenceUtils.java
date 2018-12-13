package com.muuyal.sika.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.muuyal.sika.MyApplication;
import com.muuyal.sika.R;

/**
 * Created by SOFTCO on 12/20/2016.
 */

public class AppPreferenceUtils {

    private static final String FILE_APP_PREFERENCES = "app_preferences_"
            + MyApplication.getAppContext().getString(R.string.app_name);

    private static final String KEY_IS_LOGGED = "is_logged";
    private static final String KEY_ID_USER = "id_user";
    private static final String KEY_COOKIE = "cookie";
    private static final String KEY_LISTA = "lista";
    private static final String KEY_TOKEN_FIREBASE = "token_firebase";
    private static final String KEY_TOTAL_DIAGNOSTICS = "total_diagnostics";
    private static final String KEY_EXPIRE_COOKIE_TIME = "expire_cookie_time";

    /***
     * KEYS SETTINGS
     **/
    private static final String KEY_SETTING_ENABLE_NOTIF = "key_enable_notification";

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(FILE_APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    private static void setValueDataStorage(Context context, String key, int value) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setValueDataStorage(Context context, String key, Boolean value) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setValueDataStorage(Context context, String key, String value) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Boolean getValueDataStorageBoolean(Context context, String key) {
        return getValueDataStorageBoolean(context, key, false);
    }

    private static String getValueDataStorage(Context context, String key) {
        return getValueDataStorage(context, key, null);
    }

    private static int getValueDataStorageInt(Context context, String key) {
        return getValueDataStorageInt(context, key, 0);
    }

    private static String getValueDataStorage(Context context, String key, String defaultVal) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            return sharedPref.getString(key, defaultVal);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean getValueDataStorageBoolean(Context context, String key, boolean defaultVal) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            return sharedPref.getBoolean(key, defaultVal);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getValueDataStorageInt(Context context, String key, int defaultVal) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            return sharedPref.getInt(key, defaultVal);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultVal;
        }
    }

    public static void removeValueDataStorage(Context context, String key) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(key);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearDisk(Context context) {
        SharedPreferences sharedPref = getSharedPreference(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    /***
     * This method setup notifications
     *
     * @param mContext is the context of the app
     ***/
    public static void setSettingNotifEnabled(Context mContext, boolean value) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_SETTING_ENABLE_NOTIF, value);
        }
    }

    /***
     * This method indicate if notifications are enable
     *
     * @param mContext is the context of the app
     ***/
    public static boolean isSettingNotifEnabled(Context mContext) {
        return mContext != null && getValueDataStorageBoolean(mContext, KEY_SETTING_ENABLE_NOTIF, true);
    }

    /***
     * This method setup boolean if user is logged
     *
     * @param mContext is the context of the app
     * @param value    is boolean logeed or not
     ***/
    public static void setLogged(Context mContext, boolean value) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_IS_LOGGED, value);
        }
    }

    /***
     * This method return boolean is user isLogged
     *
     * @param mContext is the context of the app
     ***/
    public static boolean isLogged(Context mContext) {
        return mContext != null && getValueDataStorageBoolean(mContext, KEY_IS_LOGGED, false);
    }

    /***
     * This method save idUser
     *
     * @param mContext is the context of the app
     * @param idUser   is the id of user
     ***/
    public static void saveIdUser(Context mContext, String idUser) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_ID_USER, idUser);
        }
    }

    /***
     * This method return actual idUser
     ***/
    public static String getIdUser(Context mContext) {
        return mContext != null ? getValueDataStorage(mContext, KEY_ID_USER) : null;
    }

    /***
     * This method save cookie
     *
     * @param mContext is the context of the app
     * @param mCookie  is the cokkie
     ***/
    public static void saveCookie(Context mContext, String mCookie) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_COOKIE, mCookie);
        }
    }

    /***
     * This method return actual cookie
     ***/
    public static String getCookie(Context mContext) {
        return mContext != null ? getValueDataStorage(mContext, KEY_COOKIE) : null;
    }


    public static void setFillList(Context mContext, boolean value) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_LISTA, value);
        }
    }

    public static boolean isFillList(Context mContext) {
        return mContext != null && getValueDataStorageBoolean(mContext, KEY_LISTA, false);
    }

    /***
     * This method save keyTokenFirebase
     *
     * @param mContext is the context App
     * @param mToken   is the token to save
     ***/
    public static void setKeyTokenFirebase(Context mContext, String mToken) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_TOKEN_FIREBASE, mToken);
        }
    }

    /***
     * This method return actual cookie
     *
     * @param mContext is the context App
     ***/
    public static String getKeyTokenFirebase(Context mContext) {
        return mContext != null ? getValueDataStorage(mContext, KEY_TOKEN_FIREBASE) : null;
    }

    /***
     * This method setup boolean if user is logged
     *
     * @param mContext is the context of the app
     * @param total    is total of diagnostics
     ***/
    public static void setTotalDiagnostics(Context mContext, int total) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_TOTAL_DIAGNOSTICS, total);
        }
    }

    /***
     * This method return boolean is user isLogged
     *
     * @param mContext is the context of the app
     ***/
    public static int getTotalDiagnostics(Context mContext) {
        return mContext != null ? getValueDataStorageInt(mContext, KEY_TOTAL_DIAGNOSTICS, 0) : 0;
    }

    /***
     * This method save cookie time expire
     *
     * @param mContext is the context of the app
     * @param expireTime   is the time to expire cookie
     ***/
    public static void updateExpireTime(Context mContext, String expireTime) {
        if (mContext != null) {
            setValueDataStorage(mContext, KEY_EXPIRE_COOKIE_TIME, expireTime);
        }
    }

    /***
     * This method return actual expire time cookie
     ***/
    public static String getExpireTime(Context mContext) {
        return mContext != null ? getValueDataStorage(mContext, KEY_EXPIRE_COOKIE_TIME) : null;
    }
}
