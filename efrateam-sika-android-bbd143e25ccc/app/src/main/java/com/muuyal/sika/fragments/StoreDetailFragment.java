package com.muuyal.sika.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.customs.CustomButton;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.TypefaceHelper;
import com.muuyal.sika.model.Store;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.utils.TextUtils;

/**
 * Created by isra on 7/29/17.
 */

public class StoreDetailFragment extends BaseFragment {

    public static final String TAG = StoreDetailFragment.class.getSimpleName();
    private static final String BUNDLE_STORE = "BUNDLE_STORE";

    private View rootView;
    private CustomTextView tvStoreTitle;
    private CustomTextView tvStoreAddress;
    private CustomTextView tvStorePhone;

    private SupportMapFragment mMapView;
    private FragmentManager fmChild;
    private CustomTextView tvErrorMap;

    private Store mStoreSelected;
    private CustomButton btnCallToStore;

    private GoogleMap mGoogleMap;
    private int typeMap = GoogleMap.MAP_TYPE_NORMAL;
    private Menu menu;

    public static StoreDetailFragment newInstance(Store mStoreSelected) {
        StoreDetailFragment mFragment = new StoreDetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_STORE, mStoreSelected);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        this.fmChild = getChildFragmentManager();

        if (getArguments() != null) {
            mStoreSelected = getArguments().getParcelable(BUNDLE_STORE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle(" ");
        ((MainActivity) mActivity).showToolbar(true, true, false, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        this.menu = menu;
        inflater.inflate(R.menu.map_menu, menu);
        //Setup font to menu
        TypefaceHelper.applyFontToMenu(menu, TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        switch (item.getItemId()) {
            case R.id.action_normal:
                typeMap = GoogleMap.MAP_TYPE_NORMAL;
                break;
            case R.id.action_satellite:
                typeMap = GoogleMap.MAP_TYPE_SATELLITE;
                break;
            case R.id.action_terrain:
                typeMap = GoogleMap.MAP_TYPE_TERRAIN;
                break;
            case R.id.action_hybrid:
                typeMap = GoogleMap.MAP_TYPE_HYBRID;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        if (mGoogleMap != null)
            mGoogleMap.setMapType(typeMap);

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_store_detail, null);

        tvStoreTitle = rootView.findViewById(R.id.tv_store_title);
        tvStoreAddress = rootView.findViewById(R.id.tv_store_address);
        tvStorePhone = rootView.findViewById(R.id.tv_store_phone);
        tvErrorMap = rootView.findViewById(R.id.tv_error_map);

        btnCallToStore = rootView.findViewById(R.id.btn_call_to_store);
        btnCallToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStoreSelected != null && !TextUtils.isEmpty(mStoreSelected.getPhone())) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + mStoreSelected.getPhone()));
                    startActivity(intent);
                }
            }
        });

        //Map
        final FrameLayout flMap = (FrameLayout) rootView.findViewById(R.id.fl_map);
        final GoogleMap[] googleMap = new GoogleMap[1];

        if (NetworkUtils.isNetworkEnabled(mContext)) {
            mMapView = SupportMapFragment.newInstance();
            MapsInitializer.initialize(mContext);
            if (mActivity != null && !mActivity.isFinishing())
                fmChild.beginTransaction().add(R.id.fl_map, mMapView).commitAllowingStateLoss();
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    if (map != null) {
                        tvErrorMap.setVisibility(View.GONE);
                        if (menu != null)
                            menu.setGroupVisible(R.id.group_type, true);
                        mGoogleMap = map;
                        map.setMapType(typeMap);
                        googleMap[0] = map;
                        map.getUiSettings().setZoomControlsEnabled(true);
                        map.getUiSettings().setZoomGesturesEnabled(true);
                        map.getUiSettings().setAllGesturesEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                        googleMap[0].setMyLocationEnabled(true);

                        if (mStoreSelected != null) {
                            LatLng mLatLng = new LatLng(mStoreSelected.getLat(), mStoreSelected.getLng());
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(12.0f).build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            googleMap[0].moveCamera(cameraUpdate);
                            googleMap[0].addMarker(new MarkerOptions()
                                    .position(mLatLng));
                        }
                    }
                }
            });
        } else {
            tvErrorMap.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mStoreSelected != null) {
            tvStoreTitle.setText(!TextUtils.isEmpty(mStoreSelected.getName()) ? mStoreSelected.getName() : "");
            tvStoreAddress.setText(!TextUtils.isEmpty(mStoreSelected.getAddress()) ? mStoreSelected.getAddress() : "");
            tvStorePhone.setText(!TextUtils.isEmpty(mStoreSelected.getPhone()) ? "Tel: " + mStoreSelected.getPhone() : "");
            btnCallToStore.setVisibility(!TextUtils.isEmpty(mStoreSelected.getPhone()) ? View.VISIBLE : View.GONE);
        }
    }
}
