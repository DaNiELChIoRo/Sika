package com.muuyal.sika.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.muuyal.sika.fragments.HomeFragment;
import com.muuyal.sika.fragments.NearbyStoresFragment;
import com.muuyal.sika.fragments.TabsSelectionFragment;

import static com.muuyal.sika.utils.LocationUtils.REQUEST_CODE_LOCATION;

public class MainActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //disableDragging();
        changeFragment(new HomeFragment(), HomeFragment.TAG);
        //changeFragment(new TabsSelectionFragment(), TabsSelectionFragment.TAG, true);
        //changeFragment(SelectCityFragment.newInstance(new State()), SelectCityFragment.TAG);
        //changeFragment(NearbyStoresFragment.newInstance(Constants.DEFAULT_STATE_ID), NearbyStoresFragment.TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOCATION) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment instanceof TabsSelectionFragment) {
                ((TabsSelectionFragment)currentFragment).loadViewPager();
            } else if (currentFragment instanceof NearbyStoresFragment) {
                ((NearbyStoresFragment)currentFragment).getUserLocation();
            }
        }
    }
}
