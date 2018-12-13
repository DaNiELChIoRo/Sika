package com.muuyal.sika.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Store;

/**
 * Created by isra on 7/3/17.
 */

public class DialogInfoMapFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = DialogInfoMapFragment.class.getSimpleName();
    private static final String BUNDLE_STORE_SELECTED = "BUNDLE_STORE_SELECTED";

    private View rootView;
    private Store mStoreSelected;
    private CustomTextView tvStoreTitle;
    private CustomTextView tvStoreAddress;
    private static ICallbackResponse<Store> mCallback;

    public static Fragment newInstance(Store mStoreSelected, ICallbackResponse<Store> mCallbackResponse) {
        mCallback = mCallbackResponse;
        DialogInfoMapFragment mFragment = new DialogInfoMapFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_STORE_SELECTED, mStoreSelected);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mStoreSelected = getArguments().getParcelable(BUNDLE_STORE_SELECTED);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_dialog_info_map, null);

        tvStoreTitle = rootView.findViewById(R.id.tv_store_title);
        tvStoreAddress = rootView.findViewById(R.id.tv_store_address);

        rootView.findViewById(R.id.btn_see_details).setOnClickListener(this);
        rootView.findViewById(R.id.btn_how_to_go).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mStoreSelected != null) {
            tvStoreTitle.setText(mStoreSelected.getName());
            tvStoreAddress.setText(mStoreSelected.getStreet());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_see_details:
                ((MainActivity)mActivity).changeFragment(StoreDetailFragment.newInstance(mStoreSelected), StoreDetailFragment.TAG, true);
                break;
            case R.id.btn_how_to_go:
                mCallback.onCallback(mStoreSelected);
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
