package com.muuyal.sika.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.Constants;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.StatesAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.model.State;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.StatesRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by Isra on 5/24/17.
 */

public class SearchStoreFragment extends BaseFragment {

    public static final String TAG = SearchStoreFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView rvStates;
    private CustomTextView tvEmptyStates;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_search_store, null);

        tvEmptyStates = rootView.findViewById(R.id.tv_empty_states);
        rvStates = rootView.findViewById(R.id.rv_states);
        rvStates.setLayoutManager(new LinearLayoutManager(mContext));
        rvStates.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkUtils.isNetworkEnabled(mContext)) {
            callStatesService(mContext, Constants.DEFAULT_COUNTRY_ID);
        } else {
            updateUI(Constants.DEFAULT_COUNTRY_ID);
        }
    }

    /***
     * This method call countries service
     * @param mContext is the context app
     * @param idCountry is the ID country to search
     *
     * ***/
    private void callStatesService(final Context mContext, final int idCountry) {
        Dispacher.sendRequest(mContext, null /*getString(R.string.load_states)*/, new StatesRequest(idCountry, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    //Country mCountry = Country.parseCountry(new JSONObject(((ResponseWs) response).getResponse()));
                    List<State> mStates = State.parseStates(new JSONArray(((ResponseWs) response).getResponse()));
                    //save data
                    //SikaDbHelper.saveCountry(mContext, mCountry);
                    SikaDbHelper.saveStates(mContext, mStates, idCountry);

                    updateUI(idCountry);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callStatesService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                updateUI(Constants.DEFAULT_COUNTRY_ID);
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_states)
                );
            }
        }));
    }

    /***
     * This method updateUI states
     *
     * @param idCountry is the Id of the corresponding country
     * ***/
    private void updateUI(final int idCountry) {
        List<State> states = SikaDbHelper.getStatesByCountryId(mContext, idCountry);
        if (states != null && !states.isEmpty()) {
            tvEmptyStates.setVisibility(View.GONE);
            rvStates.setAdapter(new StatesAdapter(mContext, states, new ICallbackResponse<State>() {

                @Override
                public void onCallback(State stateSelected) {
                    ((MainActivity) mActivity).changeFragment(SelectCityFragment.newInstance(stateSelected, idCountry), SelectCityFragment.TAG, true);
                }
            }));
        } else {
            tvEmptyStates.setVisibility(View.VISIBLE);
        }
    }
}
