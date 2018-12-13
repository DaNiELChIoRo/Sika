package com.muuyal.sika.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.muuyal.sika.Constants;
import com.muuyal.sika.R;
import com.muuyal.sika.activities.BaseMenuActivity;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.adapters.SearchProductsAdapter;
import com.muuyal.sika.adapters.TaxonomyAdapter;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.helpers.SikaDbHelper;
import com.muuyal.sika.helpers.TypefaceHelper;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.ResponseWs;
import com.muuyal.sika.model.Taxonomy;
import com.muuyal.sika.utils.DialogUtils;
import com.muuyal.sika.utils.LoggerUtils;
import com.muuyal.sika.utils.NetworkUtils;
import com.muuyal.sika.utils.TextUtils;
import com.muuyal.sika.webclient.Dispacher;
import com.muuyal.sika.webclient.WebClient;
import com.muuyal.sika.webclient.request.ProductsByTaxonomyIdRequest;
import com.muuyal.sika.webclient.request.ProductsRequest;
import com.muuyal.sika.webclient.request.TaxonomyByTypeRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by Isra on 5/24/17.
 */

public class SearchProductsFragment extends BaseFragment {

    public static final String TAG = SearchProductsFragment.class.getSimpleName();

    private static final int SCREEN_TAXONOMIES = 0;
    private static final int SCREEN_SUBTAXONOMIES = 1;
    private static final int SCREEN_PRODUCTS = 2;

    private View rootView;
    private SearchView searchView;
    private RecyclerView rvCurrentScreen;
    private RecyclerView rvAllProducts;
    private CustomTextView tvEmptyScreen;
    private CustomTextView tvEmptyProducts;

    private ViewGroup containerCurrentScreen;
    private ViewGroup containerAllProducts;

    private List<Product> mProducts, mFilterProducts;
    private List<Product> mProductsByTaxonomy;
    private List<Taxonomy> mTaxonomies;
    private List<Taxonomy> mSubTaxonomies;
    private int currentScreen = SCREEN_TAXONOMIES;

    private boolean isOnBack = false;
    private boolean isHideTitle = false;
    private String currentSearch = "";
    private int currentTaxonomoyId;
    private AppCompatImageView searchButton;

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) mActivity).setTitle(" ", getResources().getColor(R.color.black_bar));
        ((MainActivity) mActivity).showToolbar(true, true, false, false);
        ((MainActivity) mActivity).setTitle(getString(R.string.hint_search_products), isHideTitle);

        ((MainActivity) mActivity).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_dark);
        ((MainActivity) mActivity).getToolbar().setBackgroundColor(getResources().getColor(R.color.gray_bar));

        if (NetworkUtils.isNetworkEnabled(mContext)) {
            //callProductsService(mContext);
            if (!isOnBack)
                callTaxonomyByCategoryService(mContext);
            else {
                updateUI();
                updateProductsUI(currentSearch);
            }
        } else {
            mTaxonomies = SikaDbHelper.getTaxonomies(mContext, Constants.TAXONOMY_PARENT);
            mSubTaxonomies = new ArrayList<>();
            mProducts = SikaDbHelper.getProducts(mContext);
            updateUI();
            updateProductsUI("");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        //this.mProducts = FactoryTest.getProducts();
        this.mFilterProducts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(mContext, R.layout.fragment_search_products, null);

        //searchView=(SearchView) rootView.findViewById(R.id.search_view);

        rvCurrentScreen = rootView.findViewById(R.id.rv_current_screen);
        rvCurrentScreen.setLayoutManager(new LinearLayoutManager(mContext));
        rvCurrentScreen.setHasFixedSize(true);

        rvAllProducts = rootView.findViewById(R.id.rv_all_products);
        rvAllProducts.setLayoutManager(new LinearLayoutManager(mContext));
        rvAllProducts.setHasFixedSize(true);

        tvEmptyScreen = rootView.findViewById(R.id.tv_empty_screen);
        tvEmptyProducts = rootView.findViewById(R.id.tv_empty_products);

        containerCurrentScreen = rootView.findViewById(R.id.container_current_screen);
        containerAllProducts = rootView.findViewById(R.id.container_all_products);

        ((BaseMenuActivity)mActivity).getTvHeader().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchButton != null)
                    searchButton.performClick();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_product_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(mActivity.getComponentName()));

            EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchEditText.setTextColor(getResources().getColor(R.color.black_bar));
            searchEditText.setHintTextColor(getResources().getColor(R.color.black_bar));
            searchEditText.setTypeface(TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS));

            searchButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
            searchButton.setImageResource(R.drawable.ic_action_search_dark);

            AppCompatImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            searchClose.setImageResource(R.drawable.ic_close_dark);

            //searchView.setIconifiedByDefault(false);
            ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
            searchIcon.setImageDrawable(null);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView.setIconified(true);
        searchView.clearFocus();
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.hint_search_products));
            ((MainActivity) mActivity).setTitle(getString(R.string.hint_search_products), isHideTitle);
            //searchView.clearFocus();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearch = newText;
                updateProductsUI(newText);
                return true;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isHideTitle = true;
                ((MainActivity) mActivity).hideTitle(true);
                containerCurrentScreen.setVisibility(View.GONE);
                containerAllProducts.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                isHideTitle = false;
                ((MainActivity) mActivity).hideTitle(false);
                containerAllProducts.setVisibility(View.GONE);
                containerCurrentScreen.setVisibility(View.VISIBLE);
                currentSearch = "";

                return false;
            }
        });

        if (isHideTitle) {
            searchView.setQuery(currentSearch, true);
            searchView.setIconified(false);
            containerCurrentScreen.setVisibility(View.GONE);
            containerAllProducts.setVisibility(View.VISIBLE);
        } else {
            ((MainActivity) mActivity).hideTitle(false);
            containerAllProducts.setVisibility(View.GONE);
            containerCurrentScreen.setVisibility(View.VISIBLE);
        }
    }

    /***
     * This method call Products service
     * @param mContext is the context app
     *
     * ***/
    private void callProductsService(final Context mContext) {
        Dispacher.sendRequest(mContext, getString(R.string.load_products), new ProductsRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    mProducts = Product.parseProducts(new JSONArray(((ResponseWs) response).getResponse()));

                    //Order by name ASC
                    Collections.sort(mProducts, new Comparator<Product>() {
                        @Override
                        public int compare(Product o1, Product o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    //Save data in db
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SikaDbHelper.saveProducts(mContext, mProducts);
                        }
                    }).start();

                    updateProductsUI(currentSearch);
                } catch (JSONException e) {
                    LoggerUtils.logError(TAG, "callProductsService", "Error parse: " + e);
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
     * This method call taxonomies service
     *
     * @param mContext is the context app
     * ***/
    private void callTaxonomyByCategoryService(final Context mContext) {
        Dispacher.sendRequest(mContext, getString(R.string.load_taxonomy), new TaxonomyByTypeRequest(new LinkedHashMap<String, String>(), new WebClient.WebClientListener() {
            @Override
            public void onComplete(Headers headers, int code, Object response) {
                try {
                    mTaxonomies = Taxonomy.parseTaxonomies(new JSONArray(((ResponseWs) response).getResponse()), Constants.TAXONOMY_PARENT);

                    //order by name ASC
                    Collections.sort(mTaxonomies, new Comparator<Taxonomy>() {
                        @Override
                        public int compare(Taxonomy o1, Taxonomy o2) {
                            return o1.getValue().compareTo(o2.getValue());
                        }
                    });

                    //Save data in db
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SikaDbHelper.saveTaxonomies(mContext, mTaxonomies);
                        }
                    }).start();

                    currentScreen = SCREEN_TAXONOMIES;
                    callProductsService(mContext);
                    updateUI();
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
                        !com.muuyal.sika.utils.TextUtils.isEmpty(message) ? message : getString(R.string.error_load_taxonomy)
                );
            }
        }));
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
                    mProductsByTaxonomy = Product.parseProducts(new JSONArray(((ResponseWs) response).getResponse()));

                    //Order by name ASC
                    Collections.sort(mProductsByTaxonomy, new Comparator<Product>() {
                        @Override
                        public int compare(Product o1, Product o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    //Save data in db
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SikaDbHelper.saveProductsByTaxonomyId(mContext, mProductsByTaxonomy, taxonomyId);
                        }
                    }).start();

                    currentScreen = SCREEN_PRODUCTS;
                    updateUI();
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
     * This method update UI of currentScreen
     * ***/
    private void updateUI() {
        ((MainActivity)mActivity).expandAppBar(true);

        switch (currentScreen) {
            case SCREEN_TAXONOMIES:

                tvEmptyScreen.setText(getString(R.string.label_empty_categories));
                tvEmptyScreen.setVisibility(mTaxonomies.isEmpty() ? View.VISIBLE : View.GONE);
                rvCurrentScreen.setAdapter(new TaxonomyAdapter(mContext, mTaxonomies, new ICallbackResponse<Taxonomy>() {

                    @Override
                    public void onCallback(Taxonomy taxomySelected) {

                        if (taxomySelected.getTaxonomies() != null && !taxomySelected.getTaxonomies().isEmpty()) {
                            currentScreen = SCREEN_SUBTAXONOMIES;
                            mSubTaxonomies = taxomySelected.getTaxonomies();
                            updateUI();
                        } else {
                            mSubTaxonomies = new ArrayList<>();
                            currentTaxonomoyId = taxomySelected.getId();
                            if (NetworkUtils.isNetworkEnabled(mContext)) {
                                callProductsByTaxonomyIdService(mContext, taxomySelected.getId());
                            } else {
                                currentScreen = SCREEN_PRODUCTS;
                                mTaxonomies = SikaDbHelper.getTaxonomies(mContext, Constants.TAXONOMY_PARENT);
                                mProductsByTaxonomy = SikaDbHelper.getProductsByTaxonomyId(mContext, taxomySelected.getId());
                                updateUI();
                            }
                        }
                    }
                }));
                break;
            case SCREEN_SUBTAXONOMIES:

                tvEmptyScreen.setText(getString(R.string.label_empty_subcategories));
                tvEmptyScreen.setVisibility(mSubTaxonomies.isEmpty() ? View.VISIBLE : View.GONE);
                rvCurrentScreen.setAdapter(new TaxonomyAdapter(mContext, mSubTaxonomies, new ICallbackResponse<Taxonomy>() {

                    @Override
                    public void onCallback(Taxonomy taxomySelected) {

                        currentTaxonomoyId = taxomySelected.getId();
                        if (NetworkUtils.isNetworkEnabled(mContext)) {
                            callProductsByTaxonomyIdService(mContext, taxomySelected.getId());
                        } else {
                            currentScreen = SCREEN_PRODUCTS;
                            mTaxonomies = SikaDbHelper.getTaxonomies(mContext, Constants.TAXONOMY_PARENT);
                            mProductsByTaxonomy = SikaDbHelper.getProductsByTaxonomyId(mContext, taxomySelected.getId());
                            updateUI();
                        }
                    }
                }));
                break;
            case SCREEN_PRODUCTS:

                tvEmptyScreen.setText(getString(R.string.label_empty_products));
                tvEmptyScreen.setVisibility(mProductsByTaxonomy.isEmpty() ? View.VISIBLE : View.GONE);
                rvCurrentScreen.setAdapter(new SearchProductsAdapter(mContext, mProductsByTaxonomy, new ICallbackResponse<Product>() {

                    @Override
                    public void onCallback(Product productSelected) {
                        isOnBack = true;
                        ((MainActivity) mActivity).changeFragment(ProductDetailFragment.newInstance(productSelected), ProductDetailFragment.TAG, true);
                    }
                }));
                break;
        }
    }

    /***
     * This method update UI search
     *
     * @param newText is the text to search in products
     * ***/
    private void updateProductsUI(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            mFilterProducts = new ArrayList<>();
            for (Product product : mProducts) {
                if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                    mFilterProducts.add(product);
                }
            }
        } else {
            mFilterProducts = mProducts;
        }

        tvEmptyProducts.setText(getString(R.string.label_empty_products));
        tvEmptyProducts.setVisibility(mFilterProducts.isEmpty() ? View.VISIBLE : View.GONE);
        rvAllProducts.setAdapter(new SearchProductsAdapter(mContext, mFilterProducts, new ICallbackResponse<Product>() {

            @Override
            public void onCallback(Product productSelected) {
                isOnBack = true;
                ((MainActivity) mActivity).changeFragment(ProductDetailFragment.newInstance(productSelected), ProductDetailFragment.TAG, true);
            }
        }));
    }

    public void onBackPressed() {
        if (!isHideTitle) {
            if (currentScreen == SCREEN_TAXONOMIES) {
                ((MainActivity) mActivity).resetCurrentTAG();
                getFragmentManager().popBackStack();
            } else if (currentScreen == SCREEN_SUBTAXONOMIES) {
                searchView.setQuery("", true);
                searchView.setIconified(true);
                searchView.clearFocus();

                if (NetworkUtils.isNetworkEnabled(mContext)) {
                    callTaxonomyByCategoryService(mContext);
                } else {
                    currentScreen = SCREEN_TAXONOMIES;
                    mTaxonomies = SikaDbHelper.getTaxonomies(mContext, Constants.TAXONOMY_PARENT);
                    mProducts = SikaDbHelper.getProducts(mContext);
                    updateUI();
                    updateProductsUI("");
                }
            } else if (currentScreen == SCREEN_PRODUCTS) {
                searchView.setQuery("", true);
                searchView.setIconified(true);
                searchView.clearFocus();

                if (mSubTaxonomies != null && !mSubTaxonomies.isEmpty()) {
                    currentScreen = SCREEN_SUBTAXONOMIES;
                    updateUI();
                    updateProductsUI("");
                } else {
                    if (NetworkUtils.isNetworkEnabled(mContext)) {
                        callTaxonomyByCategoryService(mContext);
                    } else {
                        currentScreen = SCREEN_TAXONOMIES;
                        mTaxonomies = SikaDbHelper.getTaxonomies(mContext, Constants.TAXONOMY_PARENT);
                        mProductsByTaxonomy = SikaDbHelper.getProductsByTaxonomyId(mContext, currentTaxonomoyId);
                        updateUI();
                        updateProductsUI("");
                    }
                }
            }
        } else {
            isHideTitle = false;
            ((MainActivity) mActivity).hideTitle(false);
            containerAllProducts.setVisibility(View.GONE);
            containerCurrentScreen.setVisibility(View.VISIBLE);
            currentSearch = "";
            searchView.setQuery("", true);
            searchView.setIconified(true);
            searchView.clearFocus();
        }
    }

    @Override
    public void onStop() {
        ((MainActivity) mActivity).setTitle(" ", getResources().getColor(R.color.white));
        ((MainActivity) mActivity).hideTitle(false);
        super.onStop();
    }
}
