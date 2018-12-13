package com.muuyal.sika.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Isra on 5/22/2017.
 */

public class SerializerUtils {

    private static final String TAG = SerializerUtils.class.getSimpleName();
    private static ObjectInputStream ois;

    private static String getStoragePath(Context context) {
        PackageManager m = context.getPackageManager();
        String path = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(path, 0);
            path = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            LoggerUtils.logError(TAG, "getStoragePath", "Error NameNotFoundException: " + e);
        }
        return path;
    }

    public static void saveObject(Context context, Serializable object) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(getStoragePath(context) + "/storage.dat")));
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            LoggerUtils.logError(TAG, "saveObject", "Serialization Save Error : " + e);
        }
    }

    public static Object loadSerializedObject(Context context) {
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(getStoragePath(context) + "/storage.dat")));
            Object o = ois.readObject();
            return o;
        } catch (Exception e) {
            LoggerUtils.logError(TAG, "loadSerializedObject", "Serialization Save Error : " + e);
        }
        return null;
    }

    public static void removeSavedObject(Context context) {
        try {
            File file = new File(getStoragePath(context) + "/storage.dat");
            file.delete();
        } catch (Exception e) {
            LoggerUtils.logError(TAG, "removeSavedObject", "Error: " + e);
        }
    }

    public static void saveObject(Context context, Serializable object, String name) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(getStoragePath(context) + "/" + name + ".dat")));
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            LoggerUtils.logError(TAG, "saveObject", "Serialization Save Error : " + e);
        }
    }

    public static Object loadSerializedObject(Context context, String name) {
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(getStoragePath(context) + "/" + name + ".dat")));
            Object o = ois.readObject();
            return o;
        } catch (Exception e) {
            LoggerUtils.logError(TAG, "loadSerializedObject", "Serialization Save Error : " + e);
        }
        return null;
    }

    public static void removeSavedObject(Context context, String name) {
        try {
            File file = new File(getStoragePath(context) + "/" + name + ".dat");
            file.delete();
        } catch (Exception e) {
            LoggerUtils.logError(TAG, "removeSavedObject", "Error: " + e);
        }
    }
}
