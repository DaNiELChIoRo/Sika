package com.muuyal.sika.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;

/**
 * Created by Isra on 5/22/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CustomTextView tvHeader;
    private boolean isShowBackBtn;
    private FrameLayout flContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        tvHeader = mToolbar.findViewById(R.id.tv_header);
        flContainer = findViewById(R.id.fl_container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isShowBackBtn) {
                    onBackPressed();
                }
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    /***
     * This method change actual fragment for a new
     *
     * @param fragment is the new fragment
     * @param tag      is the tag of the fragment
     * @param isBack   is has return to last state
     ***/
    public void changeFragment(Fragment fragment, String tag, boolean isBack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, fragment, tag);
        if (isBack)
            ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    /***
     * This method change actual fragment for a new
     *
     * @param fragment is the new fragment
     * @param tag      is the tag of the fragment
     ***/
    public void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    /***
     * This method add a new fragment
     *
     * @param fragment is the new fragment
     * @param tag      is the tag of the fragment
     ***/
    public void addFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    /***
     * This method change actual fragment for a new
     *
     * @param mTitle is the title to setup in toolbar
     ***/
    public void setTitle(String mTitle) {
        if (tvHeader != null && !TextUtils.isEmpty(mTitle)) {
            tvHeader.setText(mTitle);
        }
    }

    /***
     * This method return the current fragment in activity
     ***/
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fl_container);
    }

    /***
     * This method clear actual menu
     ***/
    public void clearMenu() {
        if (mToolbar != null) {
            mToolbar.getMenu().clear();
        }
    }

    /***
     * This method show or hide toolbar
     *
     * @param isShowToolbar boolean indicate if show or not the actual toolbar
     ***/
    public void showToolbar(boolean isShowToolbar, boolean showBackBtn) {
        if (mToolbar != null) {
            mToolbar.setVisibility(isShowToolbar ? View.VISIBLE : View.GONE);
            this.isShowBackBtn = showBackBtn;
            if (showBackBtn) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
    }

    /***
     * This method show or hide toolbar
     *
     * @param isShowToolbar boolean indicate if show or not the actual toolbar
     ***/
    public void showToolbar(boolean isShowToolbar) {
        showToolbar(isShowToolbar, false);
    }

    /***
     * This method return actual toolbar
     ***/
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /***
     * This method change color background
     *
     * @param color is the color to set background
     ***/
    public void changeBakgroundColor(int color) {
        if (flContainer != null) {
            flContainer.setBackgroundColor(color);
        }
    }
}
