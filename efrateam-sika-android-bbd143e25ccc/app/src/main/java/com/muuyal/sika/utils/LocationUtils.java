package com.muuyal.sika.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by SOFTCO on 1/19/2017.
 */

public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();
    private static LocationUtils _instance;

    private static final int LOCATION_INTERVAL = 1000;
    private static final int MAX_INTERVAL_WAIT = 10000;
    public static final int REQUEST_CODE_LOCATION = 100;

    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks;
    private GoogleApiClient.OnConnectionFailedListener mConnectionFailedListener;
    private com.google.android.gms.location.LocationListener mLocationListener;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;
    public CallbackLocationListener callbackLocationListener;
    private Handler mHandler;

    public static LocationUtils getInstance() {
        if (_instance == null)
            _instance = new LocationUtils();

        return _instance;
    }

    /***
     * This method initialize Location feature
     *
     * @param mActivity is the Context App
     ***/
    public void init(Activity mActivity) {
        this.mActivity = mActivity;
        this.mConnectionCallbacks = getConnectionCallbacks();
        this.mConnectionFailedListener = getConnectionFailedListener();
        this.mLocationListener = getLocationListener();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addConnectionCallbacks(mConnectionCallbacks)
                    .addOnConnectionFailedListener(mConnectionFailedListener)
                    .addApi(LocationServices.API)
                    .build();
        }

        this.mLocationRequest = LocationRequest.create();
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.mLocationRequest.setInterval(LOCATION_INTERVAL);
        this.mLocationRequest.setFastestInterval(LOCATION_INTERVAL);
        this.mLocationRequest.setNumUpdates(1);
    }

    /***
     * This method do a request location of user
     *
     * @param callbackLocationListener is tha callback App
     * @param mActivity                is the Context App
     ***/
    public synchronized void requestLocation(final Activity mActivity, CallbackLocationListener callbackLocationListener) {
        this.callbackLocationListener = callbackLocationListener;
        if (isLocationEnabled(mActivity)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLocationRequest != null && mLocationListener != null) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    LoggerUtils.logError(TAG, "Permission deneged");
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
                //call handler delay
                setupTimeoutRequestLocation();
            } else {
                init(mActivity);
                onStart();
            }
        } else {
            //if (callbackLocationListener != null)
            //    callbackLocationListener.onLocationFound(null);
            enableLocationUser(mActivity);
        }
    }

    /***
     * This method call in onStart App
     ***/
    public void onStart() {
        this.mRequestingLocationUpdates = true;
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    /***
     * This method call in onStop App
     ***/
    public void onStop() {
        this.mRequestingLocationUpdates = true;
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    /***
     * This method return a ConnectionCallbacks
     ***/
    private GoogleApiClient.ConnectionCallbacks getConnectionCallbacks() {
        return new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                LoggerUtils.logInfo(TAG, "onConnected");
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    LoggerUtils.logError(TAG, "Permission deneged");
                    return;
                }
                if (mRequestingLocationUpdates) {
                    requestLocation(mActivity, callbackLocationListener);
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                LoggerUtils.logInfo(TAG, "onConnectionSuspended");
            }
        };
    }

    /***
     * This method return a ConnectionFailedListener
     ***/
    private GoogleApiClient.OnConnectionFailedListener getConnectionFailedListener() {
        return new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                LoggerUtils.logError(TAG, "onConnectionFailed");
                if (connectionResult.getErrorCode() == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                    LoggerUtils.logInfo(TAG, "SERVICE_VERSION_UPDATE_REQUIRED");
                    //Here update version of Google Play Services
                    checkGooglePlayServices(mActivity);
                }

                if (callbackLocationListener != null) {
                    callbackLocationListener.onLocationFound(null);
                    callbackLocationListener = null;
                }
            }
        };
    }

    /***
     * This method return a LocationListener
     ***/
    private com.google.android.gms.location.LocationListener getLocationListener() {
        return new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LoggerUtils.logInfo(TAG, "onLocationChanged", "@@@location: " + location);
                if (mActivity != null && callbackLocationListener != null) {
                    callbackLocationListener.onLocationFound(location);
                }
                stopLocationUpdates();
                callbackLocationListener = null;
            }
        };
    }

    /***
     * This method stop the location updates
     ***/
    public void stopLocationUpdates() {
        if (mGoogleApiClient != null && mLocationListener != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, mLocationListener);
        }
        mGoogleApiClient = null;
        mLocationListener = null;
        callbackLocationListener = null;
    }

    /***
     * This method indicates if the location is enable
     *
     * @param context is the context of the app
     ***/
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /***
     * This method check if google play services is available
     *
     * @param mActivity is the context of the App
     ***/
    private boolean checkGooglePlayServices(Activity mActivity) {
        if (mActivity != null) {
            final int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mActivity);
            if (status != ConnectionResult.SUCCESS) {
                Log.e(TAG, GoogleApiAvailability.getInstance().getErrorString(status));
                // ask user to update google play services.
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(
                        mActivity, status, 1);
                dialog.show();
                return false;
            } else {
                Log.i(TAG, GoogleApiAvailability.getInstance().getErrorString(status));
                // google play services is updated.
                return true;
            }
        }
        return false;
    }

    /***
     * This method setup the Timeout of a RequestLocation
     ***/
    private void setupTimeoutRequestLocation() {
        this.mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoggerUtils.logInfo(TAG, "Timeout update");
                if (mActivity != null && callbackLocationListener != null)
                    callbackLocationListener.onLocationFound(null);

                mHandler = null;
            }
        }, MAX_INTERVAL_WAIT);
    }

    /***
     * This method setup the Timeout of a RequestLocation
     ***/
    public static void enableLocationUser(Activity mActivity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mActivity.startActivityForResult(intent, REQUEST_CODE_LOCATION);
    }

    /***
     * This Interface is the Callback to return the location to actual activity
     ***/
    public interface CallbackLocationListener {
        void onLocationFound(Location mLocation);
    }
}
