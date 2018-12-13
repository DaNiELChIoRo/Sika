package com.muuyal.sika.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.muuyal.sika.BuildConfig;
import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.utils.AnalyticsUtils;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.muuyal.sika.Constants.REQUEST_PERMISSIONS;

/**
 * Created by Isra on 5/22/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final String VALUE_NAME = "millisUntilFinished";
    private CountDownTimer timer = null;
    private long totalMillis = 2000;
    private CustomTextView tvAppVersion;
    private Intent intentPush;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int versionDb = SikaDbHelper.createDatabase(this);
        LoggerUtils.logInfo(TAG, "@@@versionDb: " + versionDb);

        tvAppVersion = findViewById(R.id.tv_app_version);
        tvAppVersion.setText(String.format(getString(R.string.label_version), BuildConfig.VERSION_NAME));

        requestPermissions(this);

        //Send start app analytics
        AnalyticsUtils.getInstance().startAppEvent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (timer != null)
            timer.cancel();

        outState.putLong(VALUE_NAME, totalMillis);
        super.onSaveInstanceState(outState);
    }

    /***
     * This sequence of methods are responsible of the marshmallow permission
     *
     * @param mContext is the context of the app
     ***/
    private void requestPermissions(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String accessCoarsePermission = Manifest.permission.ACCESS_COARSE_LOCATION;
            String accessFinePermission = Manifest.permission.ACCESS_FINE_LOCATION;
            String phonePermission = Manifest.permission.CALL_PHONE;

            int hasAccessCoarsePermission = ActivityCompat.checkSelfPermission(this, accessCoarsePermission);
            int hasAccessFinePermission = ActivityCompat.checkSelfPermission(this, accessFinePermission);
            int hasPhonePermission = ActivityCompat.checkSelfPermission(this, phonePermission);

            List<String> permissions = new ArrayList<>();

            if (hasAccessCoarsePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(accessCoarsePermission);
            }
            if (hasAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(accessFinePermission);
            }
            if (hasPhonePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(phonePermission);
            }

            if (!permissions.isEmpty()) {
                final String[] params = permissions.toArray(new String[permissions.size()]);

                DialogUtils.showAlertYesNo(SplashActivity.this, getString(R.string.app_name), getString(R.string.message_permission), new MaterialDialog.SingleButtonCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestPermissions(params, REQUEST_PERMISSIONS);
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DialogUtils.showAlertYesNo(SplashActivity.this, getString(R.string.app_name), getString(R.string.error_deneg_permission), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                requestPermissions(SplashActivity.this);
                            }
                        }, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        });
                    }
                });

            } else {
                startCountDownTimer();
            }
        } else {
            startCountDownTimer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                boolean isGranted = PermissionUtils.verifyPermissions(grantResults);
                if (isGranted) {
                    startCountDownTimer();
                } else {
                    DialogUtils.showAlertYesNo(SplashActivity.this, getString(R.string.app_name), getString(R.string.error_deneg_permission), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestPermissions(SplashActivity.this);
                        }
                    }, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    });
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void startCountDownTimer() {
        timer = new CountDownTimer(totalMillis, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                totalMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                goNextScreen();
            }
        };
        timer.start();
    }

    private void goNextScreen() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

