package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.muuyal.sika.R;
import com.muuyal.sika.model.Thumbnail;
import com.muuyal.sika.utils.ImageUtils;

import java.util.List;

/**
 * Created by Isra on 6/6/17.
 */

public class CustomPagerDetailAdapter extends PagerAdapter {
    private Context mContext;
    private List<Thumbnail> mThumbnails;

    public CustomPagerDetailAdapter(Context mContext, List<Thumbnail> mThumbnails) {
        this.mContext = mContext;
        this.mThumbnails = mThumbnails;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Thumbnail thumbnail = mThumbnails.get(position);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_pager_detail, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_product);
        ProgressBar progressLoader = (ProgressBar) itemView.findViewById(R.id.progress_loader);

        ImageUtils.loadImageFromUrl(mContext, thumbnail.getUrl(), imageView, progressLoader);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mThumbnails != null ? mThumbnails.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
