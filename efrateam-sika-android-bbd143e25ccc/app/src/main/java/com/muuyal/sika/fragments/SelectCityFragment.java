package com.muuyal.sika.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.DistrictAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.District;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.model.State;
import com.muuyal.sika.model.Zipcode;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.DistrictsRequest;
import com.muuyal.sika.webclient.request.ZipcodesRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by Isra on 5/24/17.
 */

public class SelectCityFragment extends BaseFragment {

    public static final String TAG = SelectCityFragment.class.getSimpleName();
    private static final String BUNDLE_STATE_SELECTED = "BUNDLE_STATE_SELECTED";
    private static final String BUNDLE_ID_COUNTRY = "BUNDLE_ID_COUNTRY";

    private State mStateSelected;
    private View rootView;
    private RecyclerView rvCities;
    private CustomTextView tvEmptyZipcodes;

    private int idCountry;

    public static Fragment newInstance(State stateSelected, int idCountry) {
        SelectCityFragment mFragment = new SelectCityFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_STATE_SELECTED, stateSelected);
        mBundle.putInt(BUNDLE_ID_COUNTRY, idCountry);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStateSelected = getArguments().getParcelable(BUNDLE_STATE_SELECTED);
            idCountry = getArguments().getInt(BUNDLE_ID_COUNTRY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)mActivity).setTitle(mStateSelected != null ? mStateSelected.getName() : " ");
        ((MainActivity)mActivity).showToolbar(true, true, false, false);

        if (NetworkUtils.isNetworkEnabled(mContext)) {
            callDistrictService(mContext, mStateSelected.getId(), idCountry);
        } else {
            updateUI(mStateSelected.getId());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_select_city, null);

        tvEmptyZipcodes = rootView.findViewById(R.id.tv_empty_zipcodes);
        rvCities = rootView.findViewById(R.id.rv_cities);
        rvCities.setLayoutManager(new LinearLayoutManager(mContext));
        rvCities.setHasFixedSize(true);

        return rootView;
    }

    /***
     * This method call zipcodes service
     * @param mContext is the context app
     * @param idState is the ID state to search
     *
     * ***/
    private void callZipcodesService(final Context mContext, final int idState) {
        Dispacher.sendRequest(mContext, getString(R.string.load_zipcodes), new ZipcodesRequest(idState, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Zipcode> mZipcodeList = Zipcode.parseZipcodes(new JSONArray(((ResponseWs) response).getResponse()));
                    //Save in db
                    SikaDbHelper.saveZipcodes(mContext, mZipcodeList, idState);
                    updateUI(idState);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callZipcodesService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_zipcodes)
                );
            }
        }));
    }

    /***
     * This method call zipcodes service
     * @param mContext is the context app
     * @param idState is the ID state to search
     * @param idCountry is the id of country
     *
     * ***/
    private void callDistrictService(final Context mContext, final int idState, final int idCountry) {
        Dispacher.sendRequest(mContext, getString(R.string.load_zipcodes), new DistrictsRequest(idState, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    State mState = State.parseState(new JSONObject(((ResponseWs) response).getResponse()));
                    //Save in db
                    SikaDbHelper.saveState(mContext, mState, idCountry);
                    updateUI(idState);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callDistrictService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                updateUI(idState);
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_zipcodes)
                );
            }
        }));
    }

    /***
     * This method updateUI Zipcodes
     *
     * @param idState is the Id state
     * ***/
    private void updateUI(int idState) {
        State mState = SikaDbHelper.getStateById(mContext, idState);
        List<District> mDistricts = mState.getDistricts();
        if (mDistricts != null && !mDistricts.isEmpty()) {
            tvEmptyZipcodes.setVisibility(View.GONE);
            rvCities.setAdapter(new DistrictAdapter(mContext, mDistricts, new ICallbackResponse<District>() {

                @Override
                public void onCallback(District districtSelected) {
                    ((MainActivity)mActivity).changeFragment(NearbyStoresFragment.newInstance(false, districtSelected, null), NearbyStoresFragment.TAG, true);
                }
            }));
        } else {
            tvEmptyZipcodes.setVisibility(View.VISIBLE);
        }
    }
}
