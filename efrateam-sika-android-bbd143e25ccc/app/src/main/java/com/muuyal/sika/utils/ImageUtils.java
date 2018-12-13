package com.muuyal.sika.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.muuyal.sika.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by isra on 8/29/17.
 */

public class ImageUtils {

    /***
     * This method load a image from url
     *
     * @param mContext is the context app
     * @param urlLogo is the url to load
     * @param ivLogo is the current ImageView
     * ***/
    public static void loadImageFromUrl(Context mContext, String urlLogo, ImageView ivLogo) {
        if (mContext != null && ivLogo != null) {
            String url = !TextUtils.isEmpty(urlLogo) ? urlLogo : "-";
            Picasso.with(mContext)
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(ivLogo);
        }
    }

    /***
     * This method load a image from url
     *
     * @param mContext is the context app
     * @param urlLogo is the url to load
     * @param ivLogo is the current ImageView
     * @param mProgressBar is the progress bar to show when image is loading
     * ***/
    public static void loadImageFromUrl(Context mContext, String urlLogo, ImageView ivLogo, final ProgressBar mProgressBar) {
        if (mContext != null && ivLogo != null) {
            String url = !TextUtils.isEmpty(urlLogo) ? urlLogo : "-";
            Picasso.with(mContext)
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(ivLogo, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (mProgressBar != null)
                                mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            if (mProgressBar != null)
                                mProgressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
