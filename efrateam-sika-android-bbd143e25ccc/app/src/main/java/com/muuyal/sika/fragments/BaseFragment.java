package com.muuyal.sika.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.muuyal.sika.R;
import com.muuyal.sika.activities.MainActivity;
import com.muuyal.sika.utils.DialogUtils;

/**
 * Created by Isra on 5/22/2017.
 */


public class BaseFragment extends Fragment {

    public static final String TAG = BaseFragment.class.getSimpleName();

    private Dialog mDialog;
    public Activity mActivity;
    public Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ((MainActivity)mActivity).expandAppBar(true);
        }
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
     * This method hide keyboard
     *
     * @param mContext is the context of the app
     ***/
    public void hideKeyboard(Context mContext) {
        if (mContext != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }
    }

    /***
     * This method detects if the keyboard is being displayed or not, and hides it in case
     *
     * @param mContext         is the context App
     * @param activityRootView is thre actual activity root view
     ***/
    public void hideKeyboardIfShowing(final Context mContext, final View activityRootView) {
        if (mContext != null && activityRootView != null) {
            activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                private final int DefaultKeyboardDP = 100;
                // From @nathanielwolf answer...  Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
                private final int EstimatedKeyboardDP = DefaultKeyboardDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
                private final Rect r = new Rect();

                @Override
                public void onGlobalLayout() {
                    // Convert the dp to pixels.
                    int estimatedKeyboardHeight = (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());

                    // Conclude whether the keyboard is shown or not.
                    activityRootView.getWindowVisibleDisplayFrame(r);
                    int heightDiff;
                    if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        heightDiff = Math.max(activityRootView.getRootView().getHeight(),
                                activityRootView.getRootView().getWidth()) - (r.bottom - r.top);
                    } else { //landscape
                        heightDiff = Math.min(activityRootView.getRootView().getHeight(),
                                activityRootView.getRootView().getWidth()) - (r.bottom - r.top);
                    }
                    boolean isShown = heightDiff >= estimatedKeyboardHeight;

                    if (isShown) {
                        hideKeyboard(mContext);
                        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }
}
