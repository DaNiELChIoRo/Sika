package com.muuyal.sika.utils;

import android.util.Log;

import com.muuyal.sika.BuildConfig;

/**
 * Created by Isra on 5/22/2017.
 */

public class LoggerUtils {

    private static final String TAG = LoggerUtils.class.getSimpleName();
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final boolean LOG_TO_FILE = false;

    private static final String FILENAME = "log_check.txt";

    public static void logInfo(String text) {
        if (DEBUG) {
            Log.i(TAG, String.valueOf(text));
            logToFile("-", "-", "INFO: " + text);
        }
    }

    public static void logInfo(String method, String text) {
        if (DEBUG) {
            Log.i(String.valueOf(method), String.valueOf(text));
            logToFile("-", method, "INFO: " + text);
        }
    }

    public static void logInfo(String className, String method, String text) {
        if (DEBUG) {
            Log.i(className + " :: " + String.valueOf(method), String.valueOf(text));
            logToFile(className, method, "INFO: " + text);
        }
    }

    public static void logError(String text) {
        if (DEBUG) {
            Log.e(TAG, String.valueOf(text));
            logToFile("-", "-", "ERROR: " + text);
        }
    }

    public static void logError(String method, String text) {
        if (DEBUG) {
            Log.e(String.valueOf(method), String.valueOf(text));
            logToFile("-", method, "ERROR: " + text);
        }
    }

    public static void logError(String className, String method, String text) {
        if (DEBUG) {
            Log.e(className + " :: " + String.valueOf(method), String.valueOf(text));
            logToFile(className, method, "ERROR: " + text);
        }
    }

    public static void logDebug(String text) {
        if (DEBUG) {
            Log.d(TAG, String.valueOf(text));
            logToFile("-", "-", "DEBUG: " + text);
        }
    }

    public static void logDebug(String method, String text) {
        if (DEBUG) {
            Log.d(String.valueOf(method), String.valueOf(text));
            logToFile("-", method, "DEBUG: " + text);
        }
    }

    public static void logDebug(String className, String method, String text) {
        if (DEBUG) {
            Log.d(className + " :: " + String.valueOf(method), String.valueOf(text));
            logToFile(className, method, "DEBUG: " + text);
        }
    }

    public static void logThrowable(Throwable e) {
        if (DEBUG) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            if (LOG_TO_FILE) {
                logToFile(e);
            }
        }
    }

    public static void logToFile(String className, String method, String text) {
        /*try {
            StringBuffer sb = new StringBuffer();
            sb.append("LOG - Class: " + className + " :: Method: " + String.valueOf(method) + " :: Log: " + String.valueOf(text) + "\n");

            final int saveLocalType = StorageUtils.getSaveLocalType();
            final File file = StorageUtils.getSaveFile(MyApplication.getAppContext(), "LOG", saveLocalType, FILENAME);
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter ow = new OutputStreamWriter(fOut);
            ow.append(sb.toString());
            ow.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static void logToFile(Throwable e) {
        try {
            StringBuffer sb = new StringBuffer(e.toString() + "\n");
            StackTraceElement[] stElements = e.getStackTrace();
            String newLine = "";

            for (StackTraceElement stElement : stElements) {
                sb.append(newLine);
                sb.append("\tat ");
                sb.append(stElement.toString());
                newLine = "\n";
            }
        } catch (Exception ee) {
            e.printStackTrace();
        }
    }
}
