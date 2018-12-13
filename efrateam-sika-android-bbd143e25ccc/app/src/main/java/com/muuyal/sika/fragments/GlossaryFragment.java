package com.muuyal.sika.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.GlossaryAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.model.Glossary;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.GlossaryRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by isra on 7/27/17.
 */

public class GlossaryFragment extends BaseFragment {

    public static final String TAG = GlossaryFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView rvGlossary;
    private CustomTextView tvEmptyGlossary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_glossary, null);

        rvGlossary = rootView.findViewById(R.id.rv_glossary);
        rvGlossary.setLayoutManager(new LinearLayoutManager(mContext));
        rvGlossary.setHasFixedSize(true);

        tvEmptyGlossary = rootView.findViewById(R.id.tv_empty_glossary);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle(getString(R.string.title_glossary));
        ((MainActivity) mActivity).showToolbar(true, true, false, false);

        if (NetworkUtils.isNetworkEnabled(mContext)) {
            callGlossaryService(mContext);
        } else {
            updateUI();
        }
    }


    /***
     * This method call glossary service
     *
     * @param mContext is the context app
     * ***/
    private void callGlossaryService(final Context mContext) {
        Dispacher.sendRequest(mContext, getString(R.string.load_glossary), new GlossaryRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Glossary> mGlossary = Glossary.parseGlossary(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.deleteAllGlossary(mContext);
                    SikaDbHelper.saveGlossaries(mContext, mGlossary);

                    LoggerUtils.logInfo(TAG, "callGlossaryService", "save glossary");

                    updateUI();
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callGlossaryService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_glossary)
                );
            }
        }));
    }

    private void updateUI() {
        final List<Glossary> mGlossary = SikaDbHelper.getGlossary(mContext);
        if (mGlossary != null && !mGlossary.isEmpty()) {
            tvEmptyGlossary.setVisibility(View.GONE);
            rvGlossary.setAdapter(new GlossaryAdapter(mContext, mGlossary));
        } else {
            tvEmptyGlossary.setVisibility(View.VISIBLE);
        }
    }
}
