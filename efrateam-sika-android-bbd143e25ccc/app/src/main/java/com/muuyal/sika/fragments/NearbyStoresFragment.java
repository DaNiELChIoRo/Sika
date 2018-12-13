package com.muuyal.sika.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.NearbyStoresAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.helpers.TypefaceHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.District;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.model.Store;
import com.muuyal.sika.model.UserLocation;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LocationUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.NearestStoresByLocationRequest;
import com.muuyal.sika.webclient.request.StoresByDistrictRequest;
import com.muuyal.sika.webclient.request.StoresByProductIdRequest;
import com.muuyal.sika.webclient.request.StoresByStateIdRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by Isra on 5/24/17.
 */

public class NearbyStoresFragment extends BaseFragment {

    public static final String TAG = NearbyStoresFragment.class.getSimpleName();
    private static final String BUNDLE_DISTRICT = "BUNDLE_DISTRICT";
    private static final String BUNDLE_PRODUCT = "BUNDLE_PRODUCT";
    private static final String BUNDLE_IS_BY_PRODUCT = "BUNDLE_IS_BY_PRODUCT";

    private boolean isByProduct;
    private District mDistrict;
    private Product mProduct;
    private View rootView;
    private CustomTextView tvEmptyStores;
    private RecyclerView rvNearbyStores;
    private SupportMapFragment mMapView;
    private FragmentManager fmChild;
    private CustomTextView tvErrorMap;

    private UserLocation mUserLocation;
    private GoogleMap mGoogleMap;
    private int typeMap = GoogleMap.MAP_TYPE_NORMAL;
    private Menu menu;

    public static Fragment newInstance(boolean isByProduct, District mDistrict, Product mProduct) {
        NearbyStoresFragment mFragment = new NearbyStoresFragment();
        Bundle mBundle = new Bundle();
        mBundle.putBoolean(BUNDLE_IS_BY_PRODUCT, isByProduct);
        mBundle.putParcelable(BUNDLE_DISTRICT, mDistrict);
        mBundle.putParcelable(BUNDLE_PRODUCT, mProduct);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            isByProduct = getArguments().getBoolean(BUNDLE_IS_BY_PRODUCT);
            mDistrict = getArguments().getParcelable(BUNDLE_DISTRICT);
            mProduct = getArguments().getParcelable(BUNDLE_PRODUCT);
        }

        this.fmChild = getChildFragmentManager();
    }

    @Override
    public void onStart() {
        super.onStart();
        //((MainActivity)mActivity).setTitle(mCitySelected);
        ((MainActivity) mActivity).showToolbar(true, true, false, true);

        getUserLocation();
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

    public void getUserLocation() {
        showLoading(mContext);
        LocationUtils.getInstance().requestLocation(mActivity, new LocationUtils.CallbackLocationListener() {
            @Override
            public void onLocationFound(Location mLocation) {
                dismissLoaging();
                if (mLocation != null) {
                    mUserLocation = new UserLocation(mLocation.getLatitude(), mLocation.getLongitude());

                    loadMapView();
                }
            }
        });
    }

    private void loadMapView() {
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

                        if (isByProduct) {
                            if (mProduct != null) {
                                callStoresByProductIdService(mContext, mProduct.getId(), googleMap[0], mUserLocation);
                            }
                        } else { // case by stateId
                            //callStoresByStateIdService(mContext, idSelected);
                            callStoresByDistrictService(mContext, mDistrict, googleMap[0], mUserLocation);
                        }
                    }
                }
            });
        } else {
            tvErrorMap.setVisibility(View.VISIBLE);
            if (isByProduct) {
                if (mProduct != null)
                    updateUI(String.valueOf(mProduct.getId()), null, mUserLocation);
            } else {
                if (mDistrict != null)
                    updateUI(mDistrict.getId(), null, mUserLocation);
            }
            dismissLoaging();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_nearby_stores, null);

        tvEmptyStores = rootView.findViewById(R.id.tv_empty_stores);
        tvErrorMap = rootView.findViewById(R.id.tv_error_map);
        rvNearbyStores = rootView.findViewById(R.id.rv_nearby_stores);
        rvNearbyStores.setLayoutManager(new LinearLayoutManager(mContext));
        rvNearbyStores.setHasFixedSize(true);

        return rootView;
    }

    /***
     * This method call stores service
     *
     * @param mContext is the context app
     * @param idState is the ID state to search
     * ***/
    private void callStoresByStateIdService(final Context mContext, final int idState) {
        Dispacher.sendRequest(mContext, getString(R.string.load_stores), new StoresByStateIdRequest(idState, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Store> mStores = Store.parseStores(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.saveStoresByState(mContext, mStores, idState);
                    //updateUI("", null, mUserLocation);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callStoresService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_stores)
                );
            }
        }));
    }

    /***
     * This method call stores service
     *
     * @param mContext is the context app
     * @param idProduct is the ID state to search
     * ***/
    private void callStoresByProductIdService(final Context mContext, final int idProduct, final GoogleMap googleMap, final UserLocation mUserLocation) {
        Dispacher.sendRequest(mContext, getString(R.string.load_stores), new StoresByProductIdRequest(idProduct, mUserLocation, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Store> mStores = Store.parseStores(new JSONArray(((ResponseWs) response).getResponse()));

                    if (mStores != null && !mStores.isEmpty()) {
                        //Save data in db
                        SikaDbHelper.saveStoresByProduct(mContext, mStores, idProduct);
                        updateUI(String.valueOf(idProduct), googleMap, mUserLocation);
                    } else {
                        //callNearestStoresService(mContext, idProduct, googleMap, mUserLocation);
                        onError(null, getString(R.string.label_empty_stores_by_product));
                        updateUI("-1", googleMap, mUserLocation);
                    }
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callStoresService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_stores)
                );
            }
        }));
    }


    /***
     * This method call stores service
     *
     * @param mContext is the context app
     * @param idProduct is the current id product
     * @param googleMap is the actual google map
     * @param mUserLocation is the current location user
     * ***/
    private void callNearestStoresService(final Context mContext, final int idProduct, final GoogleMap googleMap, final UserLocation mUserLocation) {
        Dispacher.sendRequest(mContext, getString(R.string.load_stores), new NearestStoresByLocationRequest(mUserLocation, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Store> mStores = Store.parseStores(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.saveStoresByProduct(mContext, mStores, idProduct);
                    updateUI(String.valueOf(idProduct), googleMap, mUserLocation);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callNearestStoresService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_stores)
                );
            }
        }));
    }

    /***
     * This method call stores service
     *
     * @param mContext is the context app
     * @param mDistrict is the currect district to search
     * ***/
    private void callStoresByDistrictService(final Context mContext, final District mDistrict, final GoogleMap googleMap, final UserLocation mUserLocation) {
        Dispacher.sendRequest(mContext, getString(R.string.load_stores), new StoresByDistrictRequest(mDistrict.getName(), new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Store> mStores = Store.parseStores(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.saveStoresByDistrict(mContext, mStores, mDistrict.getId());
                    updateUI(mDistrict.getId(), googleMap, mUserLocation);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callStoresByDistrictService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_stores)
                );
            }
        }));
    }

    /***
     * This method updateUI Stores
     * @param idSelected is the current district ID or Product ID
     *
     * ***/
    private void updateUI(String idSelected, final GoogleMap googleMap, final UserLocation mUserLocation) {
        List<Store> stores = isByProduct ?
                SikaDbHelper.getStoresByProductId(mContext, Integer.valueOf(idSelected)) :
                SikaDbHelper.getStoresByDistrictId(mContext, idSelected);

        if (stores != null && !stores.isEmpty()) {
            tvEmptyStores.setVisibility(View.GONE);
            rvNearbyStores.setVisibility(View.VISIBLE);
            rvNearbyStores.setAdapter(new NearbyStoresAdapter(mContext, stores, new ICallbackResponse<Store>() {

                @Override
                public void onCallback(Store mStoreSelected) {
                    ((MainActivity) mActivity).changeFragment(StoreDetailFragment.newInstance(mStoreSelected), StoreDetailFragment.TAG, true);
                }
            }));

            if (googleMap != null) {
                //LatLngBounds.Builder builder = new LatLngBounds.Builder();
                //builder.include(new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude()));
                //Load Map markers
                for (final Store mStore : stores) {
                    if (mStore.getLat() != -1 && mStore.getLng() != -1) {
                        LatLng mLatLng = new LatLng(mStore.getLat(), mStore.getLng());
                        //builder.include(mLatLng);
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(mLatLng));
                        //.title(getString(R.string.label_my_location)));
                        marker.setTag(mStore);
                    }
                }

                /*
                Store nearStore = GeneralUtils.getNearestStore(mUserLocation, stores);
                LatLng mLatLngNear = null;
                if (nearStore.getLat() != -1 && nearStore.getLng() != -1) {
                    mLatLngNear = new LatLng(nearStore.getLat(), nearStore.getLng());
                } else {
                    mLatLngNear = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
                }
                */

                //builder.include(mLatLngNear);
                //LatLngBounds bounds = builder.build();
                //int padding = (int) (GeneralUtils.getWidthScreen(mActivity) * 0.30);
                //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, GeneralUtils.getWidthScreen(mActivity), GeneralUtils.getHeightScreen(mActivity), padding);

                LatLng mLatLng = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(12.0f).build();
                CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.moveCamera(cu);
            }
        } else {
            tvEmptyStores.setVisibility(View.VISIBLE);
            rvNearbyStores.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        LocationUtils.getInstance().stopLocationUpdates();
        /*if (mMapView != null) {
            if (mActivity != null && !mActivity.isFinishing())
                fmChild.beginTransaction().remove(mMapView).commitAllowingStateLoss();
        }*/
    }
}
