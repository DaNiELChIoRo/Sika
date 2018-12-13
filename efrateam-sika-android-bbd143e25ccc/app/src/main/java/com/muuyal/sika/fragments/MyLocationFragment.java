package com.muuyal.sika.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.maps.android.PolyUtil;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.model.UserLocation;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LocationUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.utils.PermissionUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.GetRouteRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Headers;

import static com.muuyal.sika.Constants.REQUEST_PERMISSIONS;

/**
 * Created by Isra on 5/25/17.
 */

public class MyLocationFragment extends BaseFragment {

    public static final String TAG = MyLocationFragment.class.getSimpleName();
    private static final String BUNDLE_USER_LOCATION = "bundle_user_location";

    private View rootView;
    private SupportMapFragment mMapView;
    private FragmentManager fmChild;
    private UserLocation mUserLocation;
    private CustomTextView tvErrorMap;

    public static MyLocationFragment newInstance(UserLocation mUserLocation) {
        MyLocationFragment mFragment = new MyLocationFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_USER_LOCATION, mUserLocation);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fmChild = getChildFragmentManager();

        if (getArguments() != null) {
            mUserLocation = getArguments().getParcelable(BUNDLE_USER_LOCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_my_location, null);

        tvErrorMap = rootView.findViewById(R.id.tv_error_map);
        rootView.findViewById(R.id.btn_correct_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 6/7/17 do something here
            }
        });

        loadMyLocation();

        return rootView;
    }

    private void loadMyLocation() {
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
                        googleMap[0] = map;
                        //map.getUiSettings().setAllGesturesEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                        googleMap[0].setMyLocationEnabled(true);

                        if (mUserLocation != null) {
                            LatLng mLatLng = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(15.0f).build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            googleMap[0].moveCamera(cameraUpdate);
                            //googleMap[0].addMarker(new MarkerOptions()
                            //        .position(mLatLng)
                            //        .title(getString(R.string.label_my_location)));


                            //callRouteService(googleMap[0], mLocation);
                        }
                    }
                }
            });
        } else {
            tvErrorMap.setVisibility(View.VISIBLE);
        }
    }
/*
    private void callRouteService(final GoogleMap googleMap, Location mLocation) {
        Dispacher.sendRequest(mContext, "get routes", new GetRouteRequest(mLocation, null, new WebClient.RequestInterface() {
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
                        try{
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


                            googleMap.addCircle(new CircleOptions()
                                    .center(new LatLng(src.latitude, src.longitude))
                                    .radius(5)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.BLUE));

                        }catch(NullPointerException e){
                            Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                        }catch (Exception e2) {
                            Log.e("Error", "Exception onPostExecute: " + e2.toString());
                        }

                    }
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callRouteService", "Error: " + e);
                }

            }

            @Override
            public void onError(Exception e, String message) {
                super.onError(e, message);
            }
        }));
    }
*/

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
