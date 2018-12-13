package com.muuyal.sika.utils;

/**
 * Created by Isra on 5/22/2017.
 */

public class TextUtils {

    public static boolean isEmpty(String text) {
        if (text == null)
            return true;
        if (text.equals(""))
            return true;
        if (text.equals("null"))
            return true;

        return false;
    }
}
