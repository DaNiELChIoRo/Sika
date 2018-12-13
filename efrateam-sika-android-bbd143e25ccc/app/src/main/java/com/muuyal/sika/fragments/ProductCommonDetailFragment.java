package com.muuyal.sika.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.ProductCommonDetailAdapter;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Item;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.ProductItem;
import com.muuyal.sika.utils.LoggerUtils;

/**
 * Created by isra on 6/8/17.
 */

public class ProductCommonDetailFragment extends BaseFragment {

    public static final String TAG = ProductCommonDetailFragment.class.getSimpleName();
    private static final String BUNDLE_PRODUCT = "bundle_product";
    private static final String BUNDLE_COLOR = "bundle_color";

    private View rootView;
    private RecyclerView rvProductDetail;
    private GridLayoutManager mLayoutManager;
    private ProductCommonDetailAdapter mAdapter;

    private Item mItemSelected;
    private int color;

    public static ProductCommonDetailFragment newInstance(Item productSelected, int color) {
        ProductCommonDetailFragment mFragment = new ProductCommonDetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_PRODUCT, productSelected);
        mBundle.putInt(BUNDLE_COLOR, color);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItemSelected = getArguments().getParcelable(BUNDLE_PRODUCT);
            try {
                color = getArguments().getInt(BUNDLE_COLOR);
            } catch (Exception e) {
                LoggerUtils.logError(TAG, "Error parse color: " + e);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_product_common_detail, null);
        rvProductDetail = (RecyclerView) rootView.findViewById(R.id.rv_product_detail);

        mLayoutManager = new GridLayoutManager(mContext, 2);
        rvProductDetail.setLayoutManager(mLayoutManager);
        rvProductDetail.setHasFixedSize(true);

        hideKeyboardIfShowing(mContext, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ProductCommonDetailAdapter(mActivity, mItemSelected, mItemSelected.getSteps(), color, new ICallbackResponse<ProductItem>() {
            @Override
            public void onCallback(ProductItem mProduct) {
                Product productSelected = new Product();
                productSelected.setId(mProduct.getId());
                productSelected.setName(mProduct.getTitle());
                ((MainActivity) mActivity).changeFragment(ProductDetailFragment.newInstance(productSelected), ProductDetailFragment.TAG, true);
            }
        });

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case ProductCommonDetailAdapter.COMMON_HEADER:
                        return 2;
                    case ProductCommonDetailAdapter.COMMON_NEEDED:
                        return 2;
                    case ProductCommonDetailAdapter.COMMON_STEP:
                        return 1;
                    case ProductCommonDetailAdapter.COMMON_LAST_STEP:
                        return 2;
                    case ProductCommonDetailAdapter.COMMON_FOOTER:
                        return 2;
                    default:
                        return -1;
                }
            }
        });

        rvProductDetail.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle("");
        ((MainActivity) mActivity).showToolbar(true, true, false, false);

        ((MainActivity) mActivity).getToolbar().setBackgroundColor(getResources().getColor(R.color.black_bar));
    }
}
