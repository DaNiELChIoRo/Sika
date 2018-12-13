package com.muuyal.sika.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by isra on 11/13/17.
 */

public class AnalyticsUtils {

    private static final String TAG = AnalyticsUtils.class.getSimpleName();

    private static AnalyticsUtils _instance;
    private static FirebaseAnalytics mFirebaseAnalytics;

    public static final AnalyticsUtils getInstance() {
        if (_instance == null) {
            _instance = new AnalyticsUtils();
        }

        return _instance;
    }

    public void init(Context mContext) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
    }

    /**
     * This method send a event start
     */
    public void startAppEvent() {
        // [START app event]
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "StartApp");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
        // [END app event]
    }
}
