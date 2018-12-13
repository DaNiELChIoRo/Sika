package com.muuyal.sika.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.muuyal.sika.Constants;
import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.customs.views.DisableableAppBarLayoutBehavior;
import com.muuyal.sika.fragments.GlossaryFragment;
import com.muuyal.sika.fragments.HomeFragment;
import com.muuyal.sika.fragments.PromotionsFragment;
import com.muuyal.sika.fragments.SearchProductsFragment;
import com.muuyal.sika.fragments.TabsSelectionFragment;
import com.muuyal.sika.helpers.TypefaceHelper;
import com.muuyal.sika.utils.AppPreferenceUtils;
import com.muuyal.sika.utils.DialogUtils;

/**
 * Created by Isra on 5/22/2017.
 */

public class BaseMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BaseMenuActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private AppBarLayout mAppbar;
    //private NestedScrollView nsv;
    private CustomTextView tvHeader;
    private boolean isShowBackBtn;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navView;
    private ViewGroup containerImage;
    private String currentTAG = "";
    private Dialog mDialog;
    private MenuItem menuHome, navHome;

    //private ImageView navHeaderImage;
    //private TextView tvClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_menu);

        mToolbar = findViewById(R.id.toolbar);
        mAppbar = findViewById(R.id.appbar);
        //nsv = (NestedScrollView) findViewById(R.id.nsv);
        setSupportActionBar(mToolbar);

        tvHeader = mToolbar.findViewById(R.id.tv_header);
        containerImage = mToolbar.findViewById(R.id.container_image);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerToggle = getDrawerToggle(this, mDrawerLayout, mToolbar);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navHome = navView.getMenu().findItem(R.id.nav_home);

        View header = navView.getHeaderView(0);
        //navHeaderImage = (ImageView) header.findViewById(R.id.nav_header_image);
        //tvClient = (TextView) header.findViewById(R.id.tv_client);


        if (getSupportActionBar() != null) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_action_overflow);
        mToolbar.setOverflowIcon(drawable);

        //setup back btn
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowBackBtn) {
                    onBackPressed();
                } else {//click menu hamburger
                    mDrawerLayout.openDrawer(Gravity.LEFT, true);
                }
            }
        });

        //Setup font to menu
        TypefaceHelper.applyFontToMenu(navView.getMenu(), TypefaceHelper.getInstance().getTypeface(TypefaceHelper.FONT_OPEN_SANS));

        navView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        menuHome = menu.findItem(R.id.action_home);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isShowBackBtn) {
                    onBackPressed();
                }
                break;
            case R.id.action_home:
                onNavigationItemSelected(navHome);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /***
     * This method show a Dialog loading
     *
     * @param mContext is the context of the App
     ***/
    public void showLoading(Context mContext) {
        dismissLoaging();
        if (mContext != null) {
            mDialog = DialogUtils.showLoader(mContext, getString(R.string.message_loading));
            mDialog.show();
        }
    }

    /***
     * This method dismiss a dialog loading if it exist
     ***/
    public void dismissLoaging() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /***
     * This method change actual fragment for a new
     *
     * @param fragment is the new fragment
     * @param tag      is the tag of the fragment
     * @param isBack   is has return to last state
     ***/
    public void changeFragment(Fragment fragment, String tag, boolean isBack) {
        if (currentTAG.equalsIgnoreCase(tag)) return;
        currentTAG = tag;

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
        if (currentTAG.equalsIgnoreCase(tag)) return;
        currentTAG = tag;

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
    public void setTitle(String mTitle, int color) {
        if (tvHeader != null && !TextUtils.isEmpty(mTitle)) {
            tvHeader.setVisibility(View.VISIBLE);
            showToolbarImage(false);
            tvHeader.setText(mTitle);
            tvHeader.setTextColor(color);
        }
    }

    /***
     * This method change actual fragment for a new
     *
     * @param mTitle is the title to setup in toolbar
     ***/
    public void setTitle(String mTitle) {
        if (tvHeader != null && !TextUtils.isEmpty(mTitle)) {
            tvHeader.setVisibility(View.VISIBLE);
            showToolbarImage(false);
            tvHeader.setText(mTitle);
        }
    }

    /***
     * This method change actual fragment for a new
     *
     * @param mTitle is the title to setup in toolbar
     * @param hide indicate if hide or show title
     ***/
    public void setTitle(String mTitle, boolean hide) {
        if (tvHeader != null && !TextUtils.isEmpty(mTitle)) {
            tvHeader.setVisibility(hide ? View.GONE : View.VISIBLE);
            //showToolbarImage(false);
            tvHeader.setText(mTitle);
        }
    }

    /***
     * This method hide or not the title in toolbar
     *
     * @param isHide indicates if hide the title
     ***/
    public void hideTitle(boolean isHide) {
        if (tvHeader != null) {
            tvHeader.setVisibility(isHide ? View.GONE : View.VISIBLE);
        }
    }

    public void showToolbarImage(boolean showImageHeader) {
        if (containerImage != null) {
            containerImage.setVisibility(showImageHeader ? View.VISIBLE : View.GONE);
        }

        if (tvHeader != null) {
            tvHeader.setVisibility(!showImageHeader ? View.VISIBLE : View.GONE);
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
    public void showToolbar(final boolean isShowToolbar, final boolean showBackBtn) {
        if (mToolbar != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToolbar.setVisibility(isShowToolbar ? View.VISIBLE : View.GONE);
                    isShowBackBtn = showBackBtn;
                    if (getSupportActionBar() != null) {
                        if (showBackBtn) {
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        } else {
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }
                    }
                }
            });
        }
    }

    /***
     * This method show or hide toolbar
     *
     * @param isShowToolbar boolean indicate if show or not the actual toolbar
     ***/
    public void showToolbar(final boolean isShowToolbar, final boolean showBackBtn, final boolean showImageHeader) {
        if (mToolbar != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToolbar(isShowToolbar, showBackBtn);
                    showToolbarImage(showImageHeader);
                }
            });
        }
    }

    /***
     * This method show or hide toolbar
     *
     * @param isShowToolbar boolean indicate if show or not the actual toolbar
     ***/
    public void showToolbar(final boolean isShowToolbar, final boolean showBackBtn, final boolean showImageHeader, final boolean disableAppBar) {
        if (mToolbar != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToolbar(isShowToolbar, showBackBtn, showImageHeader);
                    disableAppBar(disableAppBar);
                }
            });
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
     * This method return the drawer toggle
     *
     * @param mActivity     is the activity of the App
     * @param mDrawerLayout is the actual drawer layout
     * @param mToolbar      is the avtual toolbar
     ***/
    private ActionBarDrawerToggle getDrawerToggle(Activity mActivity, DrawerLayout mDrawerLayout, Toolbar mToolbar) {
        return new ActionBarDrawerToggle(
                mActivity,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
    }

    /*Public void changeBakgroundColor(int color) {
        if (flContainer != null) {
            flContainer.setBackgroundColor(color);
        }
    }*/

    /***
     * This method logut user of the app
     *
     * @param mContext is the context of the App
     ***/
    private void logout(final Context mContext) {
        DialogUtils.showAlertYesNo(mContext, getString(R.string.title_notification), getString(R.string.message_logout), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                AppPreferenceUtils.clearDisk(mContext);
                //GestorDbHelper.clearDatabase(mContext);
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                navView.setCheckedItem(R.id.nav_home);
                DialogUtils.dismissDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment mFragment = getCurrentFragment();
        navHome.setChecked(true);
        if (mFragment instanceof SearchProductsFragment) {
            ((SearchProductsFragment) mFragment).onBackPressed();
        } else {
            currentTAG = "";
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.nav_home:
                changeFragment(new HomeFragment(), HomeFragment.TAG, true);
                break;
            case R.id.nav_search_store:
                changeFragment(TabsSelectionFragment.newInstance(Constants.TAB_MAP), TabsSelectionFragment.TAG, true);
                break;
            case R.id.nav_search_product:
                changeFragment(new SearchProductsFragment(), SearchProductsFragment.TAG, true);
                break;
            case R.id.nav_contact_us:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_contact));
                //intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivityForResult(Intent.createChooser(intent, getString(R.string.label_send_email)), Constants.REQUEST_SEND_EMAIL);
                break;
            case R.id.nav_promotions:
                changeFragment(new PromotionsFragment(), PromotionsFragment.TAG, true);
                break;
            case R.id.nav_glossary:
                changeFragment(new GlossaryFragment(), GlossaryFragment.TAG, true);
                break;
            //case R.id.nav_logout:
            //    logout(this);
            //    break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SEND_EMAIL) {
            navHome.setChecked(true);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void expandAppBar(boolean expand) {
        if (mAppbar != null) {
            mAppbar.setExpanded(expand, true);
        }
    }

    /**
     * @param height Set AppBar height to 0.2->0.5 weight of screen height
     */
    public void setAppBarLayoutHeight(int height) {
        if (mAppbar != null) {
            ViewGroup.LayoutParams params = mAppbar.getLayoutParams();
            params.height = height;
            mAppbar.setLayoutParams(params);
        }
    }

    public void showAppbar(boolean showAppbar) {
        if (mAppbar != null) {
            //mAppbar.setVisibility(showAppbar ? View.VISIBLE : View.GONE);
            setAppBarLayoutHeight(showAppbar ? getActionBarHeight() : 0);
        }
    }


    public int getActionBarHeight() {
        int actionBarHeight = 0;

        if (getSupportActionBar() != null)
            actionBarHeight = getSupportActionBar().getHeight();

        if (actionBarHeight != 0)
            return actionBarHeight;
        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        } //else if (getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize, tv, true))
        // actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        return actionBarHeight;
    }

    public void disableDragging() {
        // Disable "Drag" for AppBarLayout (i.e. User can't scroll appBarLayout by directly touching appBarLayout - User can only scroll appBarLayout by only using scrollContent)
        if (mAppbar != null && mAppbar.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppbar.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }
    }

    public void disableAppBar(boolean disable) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppbar.getLayoutParams();
        ((DisableableAppBarLayoutBehavior) layoutParams.getBehavior()).setEnabled(!disable);
        //nsv.requestDisallowInterceptTouchEvent(!disable);
    }

    /***
     * This method show or hide menu home from toolbar
     *
     * @param hide is boolean to indicate of show or not the item menu home
     * ***/
    public void hideMenuHome(boolean hide) {
        if (menuHome != null) {
            menuHome.setVisible(!hide);
        }
    }

    /***
     * This method reset current Tag navigation
     * ***/
    public void resetCurrentTAG() {
        this.currentTAG = "";
    }

    public CustomTextView getTvHeader() {
        return tvHeader;
    }
}
