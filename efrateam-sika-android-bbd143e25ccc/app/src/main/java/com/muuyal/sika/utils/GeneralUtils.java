package com.muuyal.sika.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.muuyal.sika.model.Store;
import com.muuyal.sika.model.UserLocation;

import java.net.URL;
import java.util.List;

/**
 * Created by isra on 7/27/17.
 */

public class GeneralUtils {

    /***
     * This method return the width of screen device
     *
     * @return width screen
     * ***/
    public static int getWidthScreen(Activity mActivity) {
        if (mActivity != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }

        return 0;
    }

    /***
     * This method return the height of screen device
     *
     * @return height screen
     * ***/
    public static int getHeightScreen(Activity mActivity) {
        if (mActivity != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }

        return 0;
    }

    /***
     * This method return the nearest store
     *
     * @param mLocation is the current location user
     * @param mStores are the list of stores
     * @return the nearest store
     * ***/
    public static Store getNearestStore(UserLocation mLocation, List<Store> mStores) {
        if (mLocation != null && mStores != null) {
            Store nearStore = null;
            double nearDist = 0.0;
            double currentDist = 0.0;

            int count = 0;
            for (Store mStore : mStores) {
                currentDist = getDistance(mLocation.getLatitude(), mLocation.getLongitude(), mStore.getLat(), mStore.getLng());

                if (count == 0) {
                    nearDist = currentDist;
                    nearStore = mStore;
                    count++;
                }

                if (currentDist < nearDist) {
                    nearDist = currentDist;
                    nearStore = mStore;
                }
            }

            return nearStore;
        }
        return null;
    }

    /***
     * This method calculates the distance between two points
     *
     * @param lat1 is the lat user
     * @param lon1 is the lng user
     * @param lat2 is the latitude store
     * @param lon2 is the lng store
     * @return the distance beetween two points
     * ***/
    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        //convert miles to Kilometers
        dist = dist * 1.609344;

        return (dist);
    }

    /***
     * This method converts decimal degrees to radians
     *
     * @param deg is the decimal degrees
     * ***/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /***
     * This method converts radians to decimal degrees
     *
     * @param rad is the radians
     * ***/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    /***
     * This method get the centroid between LatLong points list
     *
     * @param points is the LatLong points
     * ***/
    public static LatLng getCentroid(List<LatLng> points) {
        if (points != null && !points.isEmpty()) {
            double latitude = 0;
            double longitude = 0;
            int n = points.size();

            for (LatLng point : points) {
                latitude += point.latitude;
                longitude += point.longitude;
            }

            return new LatLng(latitude / n, longitude / n);
        }

        return new LatLng(-1, -1);
    }

    /***
     * This method return the relative layout params from url
     *
     * @param mContext  is the context app
     * @param url is the url image
     * @param padding is the padding of image
     * ***/
    public static RelativeLayout.LayoutParams getRelativeSizeFromUrl(Activity mContext, String url, int padding) {
        if (mContext != null && !TextUtils.isEmpty(url)) {
            try {
                URL urlObject = new URL(url);
                Bitmap bmp = BitmapFactory.decodeStream(urlObject.openConnection().getInputStream());
                int realHeight = bmp.getHeight();
                int realWidth = bmp.getWidth();

                double razon = realWidth / (double) getWidthScreen(mContext);
                int relativeHeight = (int) ((double) realHeight / razon);

                return new RelativeLayout.LayoutParams(getWidthScreen(mContext) - padding, relativeHeight - padding);

            } catch (Exception e) {
                LoggerUtils.logError(GeneralUtils.class.getSimpleName(), "getRelativeSizeFromUrl", "Error layout params from url");

                return new RelativeLayout.LayoutParams(getWidthScreen(mContext), getWidthScreen(mContext));
            }
        }
        return new RelativeLayout.LayoutParams(getWidthScreen(mContext), getWidthScreen(mContext));
    }

    /***
     * This method return the relative layout params from url
     *
     * @param mContext  is the context app
     * @param url is the url image
     * ***/
    public static RelativeLayout.LayoutParams getRelativeSizeFromUrl(Activity mContext, String url) {
        return getRelativeSizeFromUrl(mContext, url, 0);
    }

    public static String getDestinationUrl(UserLocation mUserLocation, Store mDestinationStore) {
        String destination = "";
        if (mUserLocation != null && mDestinationStore != null) {
            destination = "http://maps.google.com/maps?saddr=" + mUserLocation.getLatitude() + "," + mUserLocation.getLongitude() +
                    "&daddr=" + mDestinationStore.getLat() + "," + mDestinationStore.getLng();
        }

        return destination;
    }
}
