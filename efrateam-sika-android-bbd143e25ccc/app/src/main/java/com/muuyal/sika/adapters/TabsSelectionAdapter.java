package com.muuyal.sika.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.muuyal.sika.Constants;
import com.muuyal.sika.fragments.LocationStoresFragment;
import com.muuyal.sika.fragments.SearchStoreFragment;
import com.muuyal.sika.model.UserLocation;

/**
 * Created by isra on 6/7/17.
 */

public class TabsSelectionAdapter extends FragmentStatePagerAdapter {

    private CharSequence mTitles[];
    private UserLocation mUserLocation;

    public TabsSelectionAdapter(FragmentManager fm, CharSequence mTitles[], UserLocation mUserLocation) {
        super(fm);
        this.mTitles = mTitles;
        this.mUserLocation = mUserLocation;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            //case Constants.TAB_MY_LOCATION:
            //    return MyLocationFragment.newInstance(mUserLocation);
            case Constants.TAB_MAP:
                return LocationStoresFragment.newInstance(mUserLocation);
            case Constants.TAB_STATE:
                return new SearchStoreFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles != null ? mTitles.length : 0;
    }
}

