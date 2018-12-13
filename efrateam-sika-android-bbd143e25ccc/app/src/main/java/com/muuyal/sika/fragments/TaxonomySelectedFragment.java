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
import com.muuyal.sika.adapters.ProductAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.model.Taxonomy;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.ProductsByTaxonomyIdRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by Isra on 5/24/17.
 */

public class TaxonomySelectedFragment extends BaseFragment {

    public static final String TAG = TaxonomySelectedFragment.class.getSimpleName();
    private static final String BUNDLE_TAXONOMY_SELECTED = "BUNDLE_TAXONOMY_SELECTED";

    private Taxonomy mTaxonomySelected;
    private View rootView;
    private RecyclerView rvProducts;
    private CustomTextView tvEmptyProducts;

    public static Fragment newInstance(Taxonomy mTaxonomySelected) {
        TaxonomySelectedFragment mFragment = new TaxonomySelectedFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_TAXONOMY_SELECTED, mTaxonomySelected);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTaxonomySelected = getArguments().getParcelable(BUNDLE_TAXONOMY_SELECTED);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle(getString(R.string.title_common_problems));
        ((MainActivity) mActivity).showToolbar(true, true, false, false);

        ((MainActivity) mActivity).getToolbar().setBackgroundColor(getResources().getColor(R.color.black_bar));

        if (NetworkUtils.isNetworkEnabled(mContext)) {
            callProductsByTaxonomyIdService(mContext, mTaxonomySelected.getId());
        } else {
            updateUI(mTaxonomySelected.getId());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_taxonomy_selected, null);

        rvProducts = rootView.findViewById(R.id.rv_products);
        rvProducts.setLayoutManager(new LinearLayoutManager(mContext));
        rvProducts.setHasFixedSize(true);

        tvEmptyProducts = rootView.findViewById(R.id.tv_empty_products);

        return rootView;
    }

    /***
     * This method call Products service
     *
     * @param mContext is the context app
     * ***/
    private void callProductsByTaxonomyIdService(final Context mContext, final int taxonomyId) {
        Dispacher.sendRequest(mContext, getString(R.string.load_products), new ProductsByTaxonomyIdRequest(taxonomyId, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Product> mProducts = Product.parseProducts(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.saveProductsByTaxonomyId(mContext, mProducts, taxonomyId);
                    updateUI(taxonomyId);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callTaxonomyService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_products)
                );
            }
        }));
    }

    /***
     * This method update UI of products by taxonomy Id
     *
     * @param taxonomyId is the Id of taxonomy
     * ***/
    private void updateUI(int taxonomyId) {
        List<Product> mProducts = SikaDbHelper.getProductsByTaxonomyId(mContext, taxonomyId);
        if (mProducts != null && !mProducts.isEmpty()) {
            rvProducts.setAdapter(new ProductAdapter(mContext, mProducts, mTaxonomySelected, new ICallbackResponse<Product>() {

                @Override
                public void onCallback(Product productSelected) {
                    productSelected.setColor(mTaxonomySelected.getColor());
                    ((MainActivity) mActivity).changeFragment(ProductCommonDetailFragment.newInstance(null, 0), ProductCommonDetailFragment.TAG, true);
                }
            }));
        } else {
            tvEmptyProducts.setVisibility(View.VISIBLE);
        }
    }
}
