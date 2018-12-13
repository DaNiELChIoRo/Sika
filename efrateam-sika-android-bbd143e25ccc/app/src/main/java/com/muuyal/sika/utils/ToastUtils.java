package com.muuyal.sika.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Isra on 5/22/2017.
 */

public class ToastUtils {

    public static void showShortMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShortMessage(Context context, int message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showLongMessage(Context context, int message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
