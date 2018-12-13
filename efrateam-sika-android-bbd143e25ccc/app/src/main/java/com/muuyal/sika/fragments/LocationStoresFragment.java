package com.muuyal.sika.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muuyal.sika.MyApplication;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.NearbyStoresAdapter;
import com.muuyal.sika.customs.CustomButton;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.helpers.TypefaceHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.model.Store;
import com.muuyal.sika.model.UserLocation;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.GeneralUtils;
import com.muuyal.sika.utils.LocationUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.NearestStoresRequest;
import com.muuyal.sika.webclient.request.StoresRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by Isra on 5/24/17.
 */

public class LocationStoresFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = LocationStoresFragment.class.getSimpleName();
    private static final String BUNDLE_USER_LOCATION = "bundle_user_location";

    private View rootView;
    private MapInfoWindowFragment mMapView;
    private FragmentManager fmChild;
    private UserLocation mUserLocation;
    private CustomTextView tvErrorMap;

    private InfoWindowManager infoWindowManager;
    private CustomButton btnSeeNearStores;

    private CustomTextView tvEmptyStores;
    private RecyclerView rvNearbyStores;
    private List<Store> stores;
    private GoogleMap mGoogleMap;
    private int typeMap = GoogleMap.MAP_TYPE_NORMAL;

    private Menu menu;

    public static LocationStoresFragment newInstance(UserLocation mUserLocation) {
        LocationStoresFragment mFragment = new LocationStoresFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_USER_LOCATION, mUserLocation);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        //showLoading(mContext);
        this.fmChild = getChildFragmentManager();

        if (getArguments() != null) {
            mUserLocation = getArguments().getParcelable(BUNDLE_USER_LOCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_location_stores, null);

        tvErrorMap = rootView.findViewById(R.id.tv_error_map);
        btnSeeNearStores = rootView.findViewById(R.id.btn_see_near_stores);

        tvEmptyStores = rootView.findViewById(R.id.tv_empty_stores);
        rvNearbyStores = rootView.findViewById(R.id.rv_nearby_stores);
        rvNearbyStores.setLayoutManager(new LinearLayoutManager(mContext));
        rvNearbyStores.setHasFixedSize(true);

        btnSeeNearStores.setOnClickListener(this);

        //loadMapView();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMapView();
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

    @Override
    public void onStop() {
        super.onStop();
        if (mMapView != null) {
            if (mActivity != null && !mActivity.isFinishing())
                fmChild.beginTransaction().remove(mMapView).commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_see_near_stores:
                showNearestStore();
                break;
        }
    }

    private void loadMapView() {
        if (NetworkUtils.isNetworkEnabled(mContext)) {
            mMapView = new MapInfoWindowFragment();
            MapsInitializer.initialize(mContext);
            if (mActivity != null && !mActivity.isFinishing())
                fmChild.beginTransaction().add(R.id.fl_map, mMapView).commitAllowingStateLoss();


            //Map
            final FrameLayout flMap = (FrameLayout) rootView.findViewById(R.id.fl_map);
            final GoogleMap[] googleMap = new GoogleMap[1];

            MyApplication.getInstance().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    infoWindowManager = mMapView.infoWindowManager();
                    infoWindowManager.setHideOnFling(true);

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

                            /*if (mUserLocation != null) {
                                LatLng mLatLng = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(15.0f).build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap[0].moveCamera(cameraUpdate);
                                googleMap[0].addMarker(new MarkerOptions()
                                        .position(mLatLng)
                                        .title(getString(R.string.label_my_location)));
                            }*/

                                //Here call stores services
                                //callStoresService(mContext, googleMap[0], mUserLocation);
                                callNearestStoresService(mContext, googleMap[0], mUserLocation);
                            }
                        }
                    });
                }
            }, 500);
        } else {
            //dismissLoaging();
            tvErrorMap.setVisibility(View.VISIBLE);
            LoggerUtils.logInfo(TAG, "@@@dismmiss");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) mActivity).dismissLoaging();
                }
            }, 1000);
        }
    }

    /***
     * This method call stores service
     *
     * @param mContext is the context app
     * @param googleMap is the actual google map
     * @param mUserLocation is the current location user
     * ***/
    private void callStoresService(final Context mContext, final GoogleMap googleMap, final UserLocation mUserLocation) {
        Dispacher.sendRequest(mContext, getString(R.string.load_stores), new StoresRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Store> mStores = Store.parseStores(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.saveStores(mContext, mStores);
                    updateUI(googleMap, mUserLocation);
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
     * This method updateUI states
     *
     * @param googleMap is the actual google map
     * @param mUserLocation is the current location user
     * ***/
    private void updateUI(final GoogleMap googleMap, final UserLocation mUserLocation) {
        stores = SikaDbHelper.getStores(mContext);
        //LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //builder.include(new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude()));

        if (stores != null && !stores.isEmpty()) {
            for (final Store mStore : stores) {
                if (mStore.getLat() != -1 && mStore.getLng() != -1) {
                    LatLng mLatLng = new LatLng(mStore.getLat(), mStore.getLng());
                    //builder.include(mLatLng);
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(mLatLng));
                    //.title(getString(R.string.label_my_location)));
                    marker.setTag(mStore);

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Store mStoreSelected = (Store) marker.getTag();
                            if (mStoreSelected != null) {
                                final InfoWindow[] mInfoWindow = new InfoWindow[1];
                                mInfoWindow[0] = new InfoWindow(marker, new InfoWindow.MarkerSpecification(0, 50), DialogInfoMapFragment.newInstance(mStoreSelected, new ICallbackResponse<Store>() {
                                    @Override
                                    public void onCallback(Store mStore) {
                                        if (mStore != null) {
                                            infoWindowManager.hide(mInfoWindow[0], true);
                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                    Uri.parse(GeneralUtils.getDestinationUrl(mUserLocation, mStore)));
                                            startActivity(intent);
                                            //callRouteService(googleMap, mUserLocation, new UserLocation(mStore.getLat(), mStore.getLng()));
                                        }
                                    }
                                }));
                                infoWindowManager.toggle(mInfoWindow[0], true);
                            }
                            return true;
                        }
                    });
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
            //int padding = (int) (GeneralUtils.getWidthScreen(mActivity) * 0.20);
            //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, GeneralUtils.getWidthScreen(mActivity), GeneralUtils.getHeightScreen(mActivity), padding);

            LatLng mLatLng = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(12.0f).build();
            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cu);

            //Here setup list of stores
            tvEmptyStores.setVisibility(View.GONE);
            rvNearbyStores.setVisibility(View.VISIBLE);
            rvNearbyStores.setAdapter(new NearbyStoresAdapter(mContext, stores, new ICallbackResponse<Store>() {

                @Override
                public void onCallback(Store mStoreSelected) {
                    ((MainActivity) mActivity).changeFragment(StoreDetailFragment.newInstance(mStoreSelected), StoreDetailFragment.TAG, true);
                }
            }));

            ((MainActivity) mActivity).dismissLoaging();
        } else {
            tvEmptyStores.setVisibility(View.VISIBLE);
            rvNearbyStores.setVisibility(View.GONE);
            ((MainActivity) mActivity).dismissLoaging();
        }
    }

    private void showNearestStore() {
        showLoading(mContext);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude()));

        if (stores != null && !stores.isEmpty()) {
            Store nearStore = GeneralUtils.getNearestStore(mUserLocation, stores);
            LatLng mLatLngNear = null;
            if (nearStore.getLat() != -1 && nearStore.getLng() != -1) {
                mLatLngNear = new LatLng(nearStore.getLat(), nearStore.getLng());
            } else {
                mLatLngNear = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
            }

            builder.include(mLatLngNear);
            LatLngBounds bounds = builder.build();
            int padding = (int) (GeneralUtils.getWidthScreen(mActivity) * 0.30);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, GeneralUtils.getWidthScreen(mActivity), GeneralUtils.getHeightScreen(mActivity), padding);

            /*
            LatLng mLatLng = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(12.0f).build();
            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
            */
            mGoogleMap.moveCamera(cu);

            //Here setup list of stores
            tvEmptyStores.setVisibility(View.GONE);
            rvNearbyStores.setVisibility(View.VISIBLE);
            rvNearbyStores.setAdapter(new NearbyStoresAdapter(mContext, stores, new ICallbackResponse<Store>() {

                @Override
                public void onCallback(Store mStoreSelected) {
                    ((MainActivity) mActivity).changeFragment(StoreDetailFragment.newInstance(mStoreSelected), StoreDetailFragment.TAG, true);
                }
            }));

            dismissLoaging();
        } else {
            tvEmptyStores.setVisibility(View.VISIBLE);
            rvNearbyStores.setVisibility(View.GONE);
            dismissLoaging();
        }
    }

    /***
     * This method call the rout service google
     *
     * @param googleMap is the actual google map
     * @param origin is the start location
     * @param destination is the final location
     * ***/
    /*private void callRouteService(final GoogleMap googleMap, UserLocation origin, UserLocation destination) {
        Dispacher.sendRequest(mContext, getString(R.string.load_route), new GetRouteRequest(origin, destination, new WebClient.RequestInterface() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    // routesArray contains ALL routes
                    JSONArray routesArray = jsonObject.getJSONArray("routes");
                    // Grab the first route
                    JSONObject route = routesArray.getJSONObject(0);

                    JSONObject poly = route.getJSONObject("overview_polyline");
                    String polyline = poly.getString("points");
                    List<LatLng> points = PolyUtil.decode(polyline);

                    for (int i = 0; i < points.size() - 1; i++) {
                        LatLng src = points.get(i);
                        LatLng dest = points.get(i + 1);
                        try {
                            //here is where it will draw the polyline in your map
                            List<PatternItem> pattern = Arrays.<PatternItem>asList(
                                    new Dot(), new Gap(20), new Dash(30), new Gap(20));

                            Polyline mPolyline = googleMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                                    .width(5)
                                    .color(Color.BLUE)
                                    .geodesic(true));

                            //mPolyline.setPattern(pattern);
                            //mPolyline.setJointType(JointType.ROUND);
                            //mPolyline.setStartCap(new RoundCap());
                            //mPolyline.setEndCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow_right),16));

                        } catch (NullPointerException e) {
                            Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                        } catch (Exception e2) {
                            Log.e("Error", "Exception onPostExecute: " + e2.toString());
                        }

                    }
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callRouteService", "Error: " + e);
                }

            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_routes)
                );
            }
        }));
    }*/

    /***
     * This method call stores service
     *
     * @param mContext is the context app
     * @param googleMap is the actual google map
     * @param mUserLocation is the current location user
     * ***/
    private void callNearestStoresService(final Context mContext, final GoogleMap googleMap, final UserLocation mUserLocation) {
        Dispacher.sendRequest(mContext, null/*getString(R.string.load_stores)*/, new NearestStoresRequest(mUserLocation, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Store> mStores = Store.parseStores(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.saveStores(mContext, mStores);
                    updateUI(googleMap, mUserLocation);
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
