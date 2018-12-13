package com.muuyal.sika.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

/**
 * Created by Isra on 5/22/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /***
     * This method return the name of the wifi
     *
     * @param mContext is the context of the app
     ***/
    public static String getWifiName(Context mContext) {
        if (mContext != null) {
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            String ssid = info.getSSID();

            if (!TextUtils.isEmpty(ssid))
                return ssid.replace("\"", "");
        }
        return null;
    }

    /***
     * This method indicate if wifi is available
     *
     * @param mContext is the context of the app
     * @return boolean if exist wifi connection
     ***/
    public static boolean isWifiAvailable(Context mContext) {
        if (mContext != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /***
     * This method indicate if wifi or 3G is available
     *
     * @param mContext is the context of the app
     * @return boolean if exist wifi or 3G connection
     ***/
    public static boolean isNetworkEnabled(Context mContext) {
        if (mContext != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)) {
                return true;
            }
        }
        return false;
    }
}
