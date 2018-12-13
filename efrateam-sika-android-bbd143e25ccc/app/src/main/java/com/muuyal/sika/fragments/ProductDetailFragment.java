package com.muuyal.sika.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.CustomPagerDetailAdapter;
import com.muuyal.sika.adapters.ProductDetailAdapter;
import com.muuyal.sika.adapters.RelatedProductsAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.ProductRelated;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.utils.ToastUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.ProductsRelatedRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

import static com.muuyal.sika.Constants.DELAY_SWIFT;

/**
 * Created by isra on 6/8/17.
 */

public class ProductDetailFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String TAG = ProductDetailFragment.class.getSimpleName();
    private static final String BUNDLE_PRODUCT = "bundle_product";

    private View rootView;
    private RecyclerView rvProductDetail;
    private RecyclerView rvRelatedProducts;
    private View productDetailView;
    private CustomTextView tvRelatedProducts;

    private ViewPager mViewPager;
    private CustomPagerDetailAdapter mPagerAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    private Handler mHandler;
    private int mPage = 0;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (mPagerAdapter != null) {
                if (mPagerAdapter.getCount() == mPage) {
                    mPage = 0;
                } else {
                    mPage++;
                }
                mViewPager.setCurrentItem(mPage, true);
            }
            mHandler.postDelayed(this, DELAY_SWIFT);
        }
    };

    private Product mProductSelected;
    private CustomTextView tvProductTitle;
    private CustomTextView tvProductDescr;
    private CustomTextView tvEmptyPhotos;

    public static ProductDetailFragment newInstance(Product productSelected) {
        ProductDetailFragment mFragment = new ProductDetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(BUNDLE_PRODUCT, productSelected);
        mFragment.setArguments(mBundle);

        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        if (getArguments() != null) {
            mProductSelected = getArguments().getParcelable(BUNDLE_PRODUCT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_product_detail, null);
        rvProductDetail = rootView.findViewById(R.id.rv_product_detail);

        rvProductDetail.setLayoutManager(new LinearLayoutManager(mContext));
        rvProductDetail.setHasFixedSize(true);

        productDetailView = View.inflate(mContext, R.layout.view_product_detail, null);
        tvProductTitle = productDetailView.findViewById(R.id.tv_product_title);
        tvProductDescr = productDetailView.findViewById(R.id.tv_product_descr);
        //btns
        productDetailView.findViewById(R.id.btn_uses).setOnClickListener(this);
        productDetailView.findViewById(R.id.btn_performance).setOnClickListener(this);
        productDetailView.findViewById(R.id.btn_nearest_store).setOnClickListener(this);
        //view pager configuration
        mViewPager = productDetailView.findViewById(R.id.pager_products);
        pager_indicator = productDetailView.findViewById(R.id.viewPagerCountDots);

        tvRelatedProducts = productDetailView.findViewById(R.id.tv_related_products);
        tvEmptyPhotos = productDetailView.findViewById(R.id.tv_empty_photos);
        rvRelatedProducts = productDetailView.findViewById(R.id.rv_related_products);
        rvRelatedProducts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rvRelatedProducts.setHasFixedSize(true);

        hideKeyboardIfShowing(mContext, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProductDetail.setAdapter(new ProductDetailAdapter(mContext, productDetailView));
        productDetailView.findViewById(R.id.et_focus).requestFocus();
        productDetailView.findViewById(R.id.et_focus).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle("   ");
        ((MainActivity) mActivity).showToolbar(true, true, false, false);

        if (mProductSelected != null) {
            //Here call products relationated
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                callRelatedProductService(mContext, mProductSelected.getId());
            } else {
                updateRelatedProductsUI(mProductSelected.getId());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, DELAY_SWIFT);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_uses:
                ToastUtils.showLongMessage(mContext, "click on uses");
                break;
            case R.id.btn_performance:
                ((MainActivity)mActivity).changeFragment(new CalculatorFragment(), CalculatorFragment.TAG, true);
                break;
            case R.id.btn_nearest_store:
                ((MainActivity)mActivity).changeFragment(NearbyStoresFragment.newInstance(true, null, mProductSelected), NearbyStoresFragment.TAG, true);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPage = position;

        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item_dot_yellow));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_item_dot_yellow));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setPageViewIndicator() {
        dotsCount = mPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        if (dotsCount > 0) {
            pager_indicator.removeAllViews();
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(mContext);
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item_dot_yellow));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                final int presentPosition = i;
                dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mViewPager.setCurrentItem(presentPosition);
                        return true;
                    }

                });


                pager_indicator.addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_item_dot_yellow));
        }
    }

    /***
     * This method call taxonomies by product id service
     *
     * @param mContext is the context app
     * @param idProduct is the ID of product
     * ***/
    private void callRelatedProductService(final Context mContext, final int idProduct) {
        Dispacher.sendRequest(mContext, getString(R.string.load_product_detail), new ProductsRelatedRequest(idProduct, new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    Product mProduct = Product.parseProduct(new JSONObject(((ResponseWs) response).getResponse()));

                    SikaDbHelper.saveProduct(mContext, mProduct);

                    updateRelatedProductsUI(idProduct);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callRelatedProductService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                updateRelatedProductsUI(idProduct);
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_product_detail),
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //mActivity.onBackPressed();
                            }
                        }
                );
            }
        }));
    }

    /***
     * This method update related products
     *
     * @param idProduct is the id of product
     * ***/
    private void updateRelatedProductsUI(int idProduct) {
        mProductSelected = SikaDbHelper.getProductById(mContext, idProduct);

        if (mProductSelected != null) {
            tvProductTitle.setText(mProductSelected.getName());
            tvProductDescr.setText(Html.fromHtml(mProductSelected.getDetails()));

            if (mProductSelected.getPhoto() != null) {
                mPagerAdapter = new CustomPagerDetailAdapter(mContext, mProductSelected.getPhoto().getThumbnails());
                mViewPager.setAdapter(mPagerAdapter);
                mViewPager.setCurrentItem(0);
                mViewPager.addOnPageChangeListener(this);

                setPageViewIndicator();
            } else {
                tvEmptyPhotos.setVisibility(View.VISIBLE);
            }

            List<ProductRelated> mRelatedProducts = mProductSelected.getRelated();
            //Related Products
            if (mRelatedProducts != null && !mRelatedProducts.isEmpty()) {
                rvRelatedProducts.setAdapter(new RelatedProductsAdapter(mContext, mRelatedProducts, new ICallbackResponse<ProductRelated>() {

                    @Override
                    public void onCallback(ProductRelated mProduct) {
                        Product mProductSelected = SikaDbHelper.getProductById(mContext, mProduct.getId());
                        ((MainActivity) mActivity).changeFragment(ProductDetailFragment.newInstance(mProductSelected), ProductDetailFragment.TAG, true);
                    }
                }));
            } else {
                tvRelatedProducts.setVisibility(View.GONE);
                rvRelatedProducts.setVisibility(View.GONE);
            }
        } else {
            tvEmptyPhotos.setVisibility(View.VISIBLE);
            tvRelatedProducts.setVisibility(View.GONE);
            rvRelatedProducts.setVisibility(View.GONE);
        }
    }
}
