package com.muuyal.sika.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Isra on 5/22/2017.
 */

public class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();

    public static final SimpleDateFormat SDF1 = new SimpleDateFormat("HH:mm", Locale.US);
    public static final SimpleDateFormat SDF2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
    public static final SimpleDateFormat SDF3 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    public static final SimpleDateFormat SDF4 = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
    public static final SimpleDateFormat SDF_HOUR = new SimpleDateFormat("HH", Locale.US);
    public static final SimpleDateFormat SDF_SQLITE = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat SDF_TIME_API = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    public static final SimpleDateFormat SDF_WS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    public static final SimpleDateFormat SDF_GMT = new SimpleDateFormat("Z", Locale.US);

    /***
     * This method parse a String to Date
     *
     * @param date is the String date ("HH:mm") to parse
     * @return Date
     ***/
    public static Date parseDate(String date, SimpleDateFormat sdf) {
        try {
            return sdf.parse(date);
        } catch (java.text.ParseException e) {
            LoggerUtils.logError(TAG, "parseDate", "Error para date: " + e);
            return new Date(0);
        }
    }

    /***
     * This method try to convert a Date format to other
     *
     * @param mDate is the date String to convert
     * @param sdf   is the Simple date format to convert
     ***/
    public static String convertDateFormat(String mDate, SimpleDateFormat sdf) {
        if (!TextUtils.isEmpty(mDate)) {
            return sdf.format(DateUtils.parseDate(mDate, sdf));
        }
        return mDate;
    }

    /***
     * This method try to convert a Date WS(yyyy-MM-dd'T'HH:mm:ss) format to other
     *
     * @param mDate is the date String to convert
     * @param sdf   is the Simple date format to convert
     ***/
    public static String convertDateFormatWS(String mDate, SimpleDateFormat sdf) {
        if (!TextUtils.isEmpty(mDate)) {
            return sdf.format(DateUtils.parseDate(mDate, SDF_WS));
        }
        return mDate;
    }

    /***
     * This method try to convert a Date format to other
     *
     * @param mDate is the date String to convert
     * @param sdf1  is the Simple date format to convert
     * @param sdf2  is the expected format
     ***/
    public static String convertDateFormat(String mDate, SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
        if (!TextUtils.isEmpty(mDate)) {
            return sdf2.format(DateUtils.parseDate(mDate, sdf1));
        }
        return mDate;
    }

    /***
     * This method returns the actual time Date complete
     * ("dd/MM/yyyy HH:mm:ss")
     *
     * @return date in String format
     ***/
    public static String getActualTimeComplete() {
        return SDF2.format(Calendar.getInstance().getTime());
    }


    /***
     * @param cookieTime is the expire cookie time in format ("MM/dd/yyyy hh:mm:ss")
     * @retun boolean if cookie has expired
     ***/
    public static boolean isExpired(String cookieTime) {
        if (cookieTime != null && !cookieTime.isEmpty()) {
            try {
                //convert cookie time + 8 hours
                Calendar calCookie = Calendar.getInstance();
                calCookie.setTime(SDF2.parse(cookieTime));
                calCookie.add(Calendar.HOUR, 12);

                Date dateCookie = calCookie.getTime();

                //get currentTime
                Date dateCurrent = Calendar.getInstance().getTime();

                if (dateCurrent.compareTo(dateCookie) == 0 || dateCurrent.after(dateCookie))
                    return true;
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "parseDates", "error:" + e);
            }
        }
        return false;
    }
}
