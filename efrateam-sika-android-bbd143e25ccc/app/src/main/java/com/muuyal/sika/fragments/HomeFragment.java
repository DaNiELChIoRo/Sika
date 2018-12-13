package com.muuyal.sika.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.muuyal.sika.Constants;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.CustomPagerHomeAdapter;
import com.muuyal.sika.adapters.HomeAdapter;
import com.muuyal.sika.adapters.SuggestionAdapter;
import com.muuyal.sika.helpers.CacheDataHelper;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.interfaces.ICallbackAdapterResponse;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Company;
import com.muuyal.sika.model.Country;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.model.SliderItem;
import com.muuyal.sika.model.SliderMetadata;
import com.muuyal.sika.model.Solution;
import com.muuyal.sika.model.SolutionSection;
import com.muuyal.sika.model.Suggestion;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.utils.SerializerUtils;
import com.muuyal.sika.webclient.API;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.CompaniesRequest;
import com.muuyal.sika.webclient.request.CountriesRequest;
import com.muuyal.sika.webclient.request.SliderRequest;
import com.muuyal.sika.webclient.request.SuggestionsRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

import static com.muuyal.sika.Constants.DELAY_SWIFT;

/**
 * Created by Isra on 5/23/17.
 */

public class HomeFragment extends BaseFragment implements ICallbackAdapterResponse, ViewPager.OnPageChangeListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView rvHome;
    private HomeAdapter homeAdapter;

    int[] mResources = {R.drawable.ic_jeringa, R.drawable.ic_jeringa, R.drawable.ic_jeringa, R.drawable.ic_jeringa,
            R.drawable.ic_jeringa, R.drawable.ic_jeringa
    };

    private View mHeaderView;
    private ViewPager mViewPager;
    private CustomPagerHomeAdapter mAdapter;
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
    public void onStart() {
        super.onStart();
        //((MainActivity) mActivity).setTitle(getString(R.string.title_home));
        ((MainActivity) mActivity).showToolbar(true, false, true, false);

        //if (NetworkUtils.isNetworkEnabled(mContext)) {
        if (NetworkUtils.isNetworkEnabled(mContext))
            showLoading(mContext);

        callSolutionService(mContext);
        callSliderService(mContext);

        //} else {
        //    updateUI();
        //}
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_home, null);

        rvHome = (RecyclerView) rootView.findViewById(R.id.rv_home);
        rvHome.setLayoutManager(new LinearLayoutManager(mContext));
        rvHome.setHasFixedSize(true);

        //Setup ViewPager
        mHeaderView = View.inflate(mContext, R.layout.view_home_header, null);
        mViewPager = (ViewPager) mHeaderView.findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) mHeaderView.findViewById(R.id.viewPagerCountDots);

        hideKeyboardIfShowing(mContext, rootView);

        return rootView;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);

        //hide menu home
        ((MainActivity)mActivity).hideMenuHome(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            ((MainActivity) mActivity).changeFragment(new SearchProductsFragment(), SearchProductsFragment.TAG, true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCallback(int type, Object object) {
        switch (type) {
            case HomeAdapter.TYPE_HEADER:
                // TODO: 7/26/17 here setup a listener
                break;
            case SuggestionAdapter.TYPE_ITEM:
                //((MainActivity) mActivity).changeFragment(TaxonomySelectedFragment.newInstance((Taxonomy) object), TaxonomySelectedFragment.TAG, true);
                ((MainActivity) mActivity).changeFragment(SuggestionSelectedFragment.newInstance((Solution) object), SuggestionSelectedFragment.TAG, true);
                break;
        }
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
     * This method call countries service
     *
     * @param mContext is the context app
     * ***/
    private void callCountriesService(final Context mContext) {
        Dispacher.sendRequest(mContext, getString(R.string.load_countries), new CountriesRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Country> mCountryList = Country.parseCountries(new JSONArray(((ResponseWs) response).getResponse()));
                    //save countries in db
                    SikaDbHelper.saveCountries(mContext, mCountryList);
                    LoggerUtils.logInfo(TAG, "callCountriesService", "countries saved");
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callCountriesService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_countries)
                );
            }
        }));
    }

    /***
     * This method updates UI
     *
     * ***/
    private void updateSolutions(List<SolutionSection> mSolutions) {
        if (mSolutions == null)
            mSolutions = SikaDbHelper.getSolutionSections(mContext);
        homeAdapter = new HomeAdapter(mActivity, mSolutions, mHeaderView, this);
        rvHome.setAdapter(homeAdapter);
    }

    /***
     * This method updates UI
     *
     * ***/
    private void updateSlider(List<SliderItem> mSliders) {
        if (mSliders == null)
            mSliders = SikaDbHelper.getSliderItems(mContext);

        mAdapter = new CustomPagerHomeAdapter(mContext, mSliders, new ICallbackResponse<SliderItem>() {
            @Override
            public void onCallback(SliderItem mItem) {
                SliderMetadata metadata = mItem.getMetadata();
                if (Constants.TYPE_PRODUCT.equalsIgnoreCase(mItem.getType())) {
                    if (metadata != null) {
                        Product productSelected = new Product();
                        productSelected.setId(metadata.getId());
                        productSelected.setName(metadata.getName());
                        ((MainActivity) mActivity).changeFragment(ProductDetailFragment.newInstance(productSelected), ProductDetailFragment.TAG, true);
                    }
                } else if (Constants.TYPE_PROMO.equalsIgnoreCase(mItem.getType())) {
                    ((MainActivity) mActivity).changeFragment(new PromotionsFragment(), PromotionsFragment.TAG, true);
                }
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);

        setPageViewIndicator();

        dismissLoaging();
    }

    /***
     * This method call companies service
     *
     * @param mContext is the context app
     * ***/
    private void callCompanyService(final Context mContext) {
        Dispacher.sendRequest(mContext, getString(R.string.load_companies), new CompaniesRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Company> mCompanies = Company.parseCompanies(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.saveCompanies(mContext, mCompanies);
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
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_companies)
                );
            }
        }));
    }

    /***
     * This method call suggestions service
     *
     * @param mContext is the context app
     * ***/
    private void callSuggestionService(final Context mContext) {
        Dispacher.sendRequest(mContext, getString(R.string.load_suggestions), new SuggestionsRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    List<Suggestion> mSuggestions = Suggestion.parseSuggestions(new JSONArray(((ResponseWs) response).getResponse()));

                    //Save data in db
                    SikaDbHelper.deleteAllSuggestions(mContext);
                    SikaDbHelper.saveSuggestions(mContext, mSuggestions);

                    LoggerUtils.logInfo(TAG, "callSuggestionService", "save suggestions");

                    //updateUI();
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callSuggestionService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_suggestions)
                );
            }
        }));
    }

    /***
     * This method call suggestions service
     *
     * @param mContext is the context app
     * ***/
    private void callSolutionService(final Context mContext) {
        Dispacher.sendRequest(mContext, /*getString(R.string.load_solutions)*/"", new SuggestionsRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {

                    SerializerUtils.saveObject(mContext, (ResponseWs) response, SuggestionsRequest.TAG);
                    final List<SolutionSection> mSolutions = SolutionSection.parseSolutionSections(new JSONArray(((ResponseWs) response).getResponse()));
                    if (CacheDataHelper.getInstance().expiredUrl(API.URL_SUGGESTIONS)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //Save data in db
                                SikaDbHelper.deleteAllSolutionSections(mContext);
                                SikaDbHelper.saveSolutionSections(mContext, mSolutions);
                            }
                        }).start();
                    }

                    LoggerUtils.logInfo(TAG, "callSolutionService", "save solutions");

                    updateSolutions(mSolutions);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callSolutionService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                updateSolutions(null);
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_solutions)
                );
            }
        }), SuggestionsRequest.TAG);
    }

    /***
     * This method call slider service
     *
     * @param mContext is the context app
     * ***/
    private void callSliderService(final Context mContext) {
        Dispacher.sendRequest(mContext, /*getString(R.string.load_slider)*/"", new SliderRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {

                    SerializerUtils.saveObject(mContext, (ResponseWs) response, SliderRequest.TAG);
                    final List<SliderItem> mSliders = SliderItem.parseSliders(new JSONArray(((ResponseWs) response).getResponse()));
                    if (CacheDataHelper.getInstance().expiredUrl(API.URL_SLIDER)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //Save data in db
                                SikaDbHelper.deleteAllSliderItems(mContext);
                                SikaDbHelper.saveSliderItems(mContext, mSliders);
                            }
                        }).start();
                    }

                    LoggerUtils.logInfo(TAG, "callSliderService", "save solutions");

                    updateSlider(mSliders);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callSliderService", "Error parse: " + e);
                    onError(e, null);
                }
            }

            @Override
            public void onError(Exception e, String message) {
                updateSlider(null);
                DialogUtils.showAlert(
                        mContext,
                        getString(R.string.title_error),
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_slider)
                );
            }
        }), SliderRequest.TAG);
    }

    @Override
    public void onStop() {
        ((MainActivity)mActivity).hideMenuHome(false);
        super.onStop();
    }
}
