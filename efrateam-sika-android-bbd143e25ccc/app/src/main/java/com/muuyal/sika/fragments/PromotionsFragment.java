package com.muuyal.sika.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.CustomPagerPromotionAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Promotion;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.GeneralUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.PromotionRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

import static com.muuyal.sika.Constants.DELAY_SWIFT;

/**
 * Created by isra on 7/27/17.
 */

public class PromotionsFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    public static final String TAG = PromotionsFragment.class.getSimpleName();

    private View rootView;
    private CustomTextView tvEmptyPromotions;
    private ViewGroup containerViewpager;

    private ViewPager mViewPager;
    private CustomPagerPromotionAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    private Handler mHandler;
    private int mPage = 0;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (mAdapter != null) {
                if (mAdapter.getCount() == mPage) {
                    mPage = 0;
                } else {
                    mPage++;
                }
                mViewPager.setCurrentItem(mPage, true);
            }
            mHandler.postDelayed(this, DELAY_SWIFT);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_promotions, null);

        mViewPager = rootView.findViewById(R.id.viewpager);
        pager_indicator = rootView.findViewById(R.id.viewPagerCountDots);
        tvEmptyPromotions = rootView.findViewById(R.id.tv_empty_promotions);
        containerViewpager = rootView.findViewById(R.id.container_viewpager);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GeneralUtils.getWidthScreen(mActivity),
                GeneralUtils.getHeightScreen(mActivity) - 2 * ((MainActivity) mActivity).getActionBarHeight() - 50);
        params.setMargins(10,10,10,10);
        mViewPager.setLayoutParams(params);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle(getString(R.string.title_promotions));
        ((MainActivity) mActivity).showToolbar(true, true, false, false);

        if (NetworkUtils.isNetworkEnabled(mContext)) {
            callPromotionsService(mContext);
        } else {
            updateUI();
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

    private void setPageViewIndicator() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        if (dotsCount > 0) {
            pager_indicator.removeAllViews();
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(mContext);
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item_dot));

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

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_item_dot));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPage = position;

        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_item_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /***
     * This method call suggestions service
     *
     * @param mContext is the context app
     * ***/
    private void callPromotionsService(final Context mContext) {
        Dispacher.sendRequest(mContext, getString(R.string.load_promotions), new PromotionRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Promotion> mPromotion = Promotion.parsePromotions(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.deleteAllPromotion(mContext);
                    SikaDbHelper.savePromotions(mContext, mPromotion);

                    LoggerUtils.logInfo(TAG, "callPromotionsService", "save promotions");

                    updateUI();
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callPromotionsService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_promotions)
                );
            }
        }));
    }

    private void updateUI() {
        final List<Promotion> mPromotions = SikaDbHelper.getPromotions(mContext);
        if (mPromotions != null && !mPromotions.isEmpty()) {
            tvEmptyPromotions.setVisibility(View.GONE);
            containerViewpager.setVisibility(View.VISIBLE);
            mAdapter = new CustomPagerPromotionAdapter(mActivity, mPromotions, new ICallbackResponse<Promotion>() {
                @Override
                public void onCallback(Promotion mPromotionSelected) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mPromotionSelected.getLink())));
                }
            });
            mViewPager.setAdapter(mAdapter);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(0);
            mViewPager.addOnPageChangeListener(this);

            setPageViewIndicator();
        } else {
            containerViewpager.setVisibility(View.GONE);
            tvEmptyPromotions.setVisibility(View.VISIBLE);
        }
    }
}
