package com.muuyal.sika.fragments;

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
import com.muuyal.sika.adapters.ItemAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Item;
import com.muuyal.sika.model.Solution;

import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class SuggestionSelectedFragment extends BaseFragment {

    public static final String TAG = SuggestionSelectedFragment.class.getSimpleName();
    private static final String BUNDLE_SUGGESTION_SELECTED = "BUNDLE_SUGGESTION_SELECTED";

    private Solution mSuggestionSelected;
    private View rootView;
    private RecyclerView rvItems;
    private CustomTextView tvEmptyItems;

    public static Fragment newInstance(Solution mSugestionSelected) {
        SuggestionSelectedFragment mFragment = new SuggestionSelectedFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_SUGGESTION_SELECTED, mSugestionSelected);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSuggestionSelected = getArguments().getParcelable(BUNDLE_SUGGESTION_SELECTED);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle(" ");
        ((MainActivity) mActivity).showToolbar(true, true, false, false);

        ((MainActivity) mActivity).getToolbar().setBackgroundColor(getResources().getColor(R.color.black_bar));

        if (mSuggestionSelected != null) {
            updateUI(mSuggestionSelected.getItems());
        } else {
            tvEmptyItems.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_suggestion_selected, null);

        rvItems = rootView.findViewById(R.id.rv_items);
        rvItems.setLayoutManager(new LinearLayoutManager(mContext));
        rvItems.setHasFixedSize(true);

        tvEmptyItems = rootView.findViewById(R.id.tv_empty_items);

        return rootView;
    }

    /***
     * This method update UI of item of suggestion
     * ***/
    private void updateUI(List<Item> mItems) {
        if (mItems != null && !mItems.isEmpty()) {
            rvItems.setAdapter(new ItemAdapter(mActivity, mItems, mSuggestionSelected, new ICallbackResponse<Item>() {

                @Override
                public void onCallback(Item itemSelected) {
                    ((MainActivity) mActivity).changeFragment(ProductCommonDetailFragment.newInstance(itemSelected, mSuggestionSelected.getColor()), ProductCommonDetailFragment.TAG, true);
                }
            }));
        } else {
            tvEmptyItems.setVisibility(View.VISIBLE);
        }
    }
}
