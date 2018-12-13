package com.muuyal.sika.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Isra on 5/22/2017.
 */

public class StorageUtils {
    public static final int INTERNAL_STORAGE = 1;
    public static final int EXTERNAL_STORAGE = 2;
    public static final int SD_STORAGE = 3;

    public static int getSaveLocalType() {
        int response = INTERNAL_STORAGE;
        if (writeExternal())
           response = EXTERNAL_STORAGE;

        return response;
    }

    public static boolean writeExternal() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    public static File getSaveFile(Context c, String folder, int saveLocalType, String fileName) {
        File response;

        switch (saveLocalType) {
            case INTERNAL_STORAGE :
                response = new File(getInternalFolder(c, folder), fileName);
                break;
            case EXTERNAL_STORAGE :
                response = new File(getExternalFolder(c, folder), fileName);
                break;
            case SD_STORAGE :
                response = new File(getExternalFolder(c, folder), fileName);
                break;
            default :
                response = new File(getInternalFolder(c, folder), fileName);
                break;
        }

        return response;
    }

    public static File getInternalFolder(Context c, String folder) {
        return c.getDir(folder, Context.MODE_PRIVATE);
    }

    public static File getExternalFolder(Context c, String folder) {
        return c.getExternalFilesDir(folder);
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return 0l;
        }
    }

    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return 0l;
        }
    }

    /***
     *  Get correct path
     * ***/

    public static String getPathFromURI(Context context, Uri contentUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(context, contentUri)) {
            return getPathForV19AndUp(context, contentUri);
        } else {
            return getPathForPreV19(context, contentUri);
        }
    }

    private static String getPathForPreV19(Context context, Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            } finally {
                cursor.close();
            }
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getPathForV19AndUp(Context context, Uri contentUri) {
        String documentId = DocumentsContract.getDocumentId(contentUri);
        String id = documentId.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex);
                }
            } finally {
                cursor.close();
            }
        }

        return null;
    }
}
