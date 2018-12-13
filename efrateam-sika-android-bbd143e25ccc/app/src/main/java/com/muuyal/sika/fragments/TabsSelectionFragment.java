package com.muuyal.sika.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.muuyal.sika.MyApplication;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.TabsSelectionAdapter;
import com.muuyal.sika.customs.sliding.SlidingTabLayout;
import com.muuyal.sika.model.UserLocation;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LocationUtils;
import com.muuyal.sika.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.muuyal.sika.Constants.REQUEST_PERMISSIONS;

/**
 * Created by isra on 6/7/17.
 */

public class TabsSelectionFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    public static final String TAG = TabsSelectionFragment.class.getSimpleName();
    private static final String BUNDLE_POSITION_SELECTED = "bundle_position_selected";

    private View rootView;
    private ViewPager pager;
    private TabsSelectionAdapter mAdapter;
    private SlidingTabLayout tabs;
    private static int positionSelected;

    private CharSequence Titles[] = {
            //MyApplication.getInstance().getString(R.string.tab_my_location),
            MyApplication.getInstance().getString(R.string.tab_map),
            MyApplication.getInstance().getString(R.string.tab_state)
    };

    public static TabsSelectionFragment newInstance(int positionSelected) {
        TabsSelectionFragment mFragment = new TabsSelectionFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt(BUNDLE_POSITION_SELECTED, positionSelected);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            positionSelected = getArguments().getInt(BUNDLE_POSITION_SELECTED, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_tabs_selection, null);

        pager = (ViewPager) rootView.findViewById(R.id.pager);

        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        //tabs.setBackgroundColor(ColorUtils.setAlphaComponent(getResources().getColor(R.color.colorPrimary), 200));
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        tabs.setCustomTabView(R.layout.view_custom_tab, R.id.tv_tab_title);

        hideKeyboardIfShowing(mContext, rootView);

        requestPermissions(mContext);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)mActivity).setTitle(getString(R.string.title_search_store));
        ((MainActivity)mActivity).showToolbar(true, true, false, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        positionSelected = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /***
     * This sequence of methods are responsible of the marshmallow permission
     *
     * @param mContext is the context of the app
     ***/
    private void requestPermissions(final Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String accessCoarsePermission = Manifest.permission.ACCESS_COARSE_LOCATION;
            String accessFinePermission = Manifest.permission.ACCESS_FINE_LOCATION;
            String accessPhonePermission = Manifest.permission.CALL_PHONE;

            int hasAccessCoarsePermission = ActivityCompat.checkSelfPermission(mContext, accessCoarsePermission);
            int hasAccessFinePermission = ActivityCompat.checkSelfPermission(mContext, accessFinePermission);
            int hasPhonePermission = ActivityCompat.checkSelfPermission(mContext, accessPhonePermission);

            List<String> permissions = new ArrayList<>();

            if (hasAccessCoarsePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(accessCoarsePermission);
            }
            if (hasAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(accessFinePermission);
            }
            if (hasPhonePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(accessPhonePermission);
            }

            if (!permissions.isEmpty()) {
                final String[] params = permissions.toArray(new String[permissions.size()]);

                DialogUtils.showAlertYesNo(mContext, getString(R.string.app_name), getString(R.string.message_permission), new MaterialDialog.SingleButtonCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestPermissions(params, REQUEST_PERMISSIONS);
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DialogUtils.showAlertYesNo(mContext, getString(R.string.app_name), getString(R.string.error_deneg_permission), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                requestPermissions(mContext);
                            }
                        }, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mActivity.onBackPressed();
                            }
                        });
                    }
                });

            } else {
                loadViewPager();
            }
        } else {
            loadViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                boolean isGranted = PermissionUtils.verifyPermissions(grantResults);
                if (isGranted) {
                    loadViewPager();
                } else {
                    DialogUtils.showAlertYesNo(mContext, getString(R.string.app_name), getString(R.string.error_deneg_permission), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestPermissions(mContext);
                        }
                    }, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mActivity.onBackPressed();
                        }
                    });
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void loadViewPager() {
        ((MainActivity)mActivity).showLoading(mActivity);
        //showLoading(mContext);
        LocationUtils.getInstance().requestLocation(mActivity, new LocationUtils.CallbackLocationListener() {
            @Override
            public void onLocationFound(Location mLocation) {
                if (mLocation != null) {
                    UserLocation mUserLocation = new UserLocation(mLocation.getLatitude(), mLocation.getLongitude());

                    mAdapter = new TabsSelectionAdapter(getChildFragmentManager(), Titles, mUserLocation);
                    pager.setAdapter(mAdapter);
                    pager.setOffscreenPageLimit(Titles.length);
                    pager.addOnPageChangeListener(TabsSelectionFragment.this);
                    pager.setCurrentItem(positionSelected);
                    tabs.setViewPager(pager);
                }
                //dismissLoaging();
            }
        });
    }
}
