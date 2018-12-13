package com.muuyal.sika;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.muuyal.sika.helpers.TypefaceHelper;
import com.muuyal.sika.utils.AnalyticsUtils;

/**
 * Created by SOFTCO on 1/3/2017.
 */

public class MyApplication extends Application {

    private static Context mContext;
    private static MyApplication _instance;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        _instance = this;

        //Init Analytics
        AnalyticsUtils.getInstance().init(this);
        //Init Typeface
        TypefaceHelper.initialize(this, R.xml.fonts);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static MyApplication getInstance() {
        return _instance;
    }

    public Handler getHandler() {
        return mHandler;
    }
}
