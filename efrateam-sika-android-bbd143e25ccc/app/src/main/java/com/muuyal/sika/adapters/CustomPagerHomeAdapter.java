package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.muuyal.sika.R;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.SliderItem;
import com.muuyal.sika.model.SliderMetadata;
import com.muuyal.sika.utils.ImageUtils;

import java.util.List;

/**
 * Created by Isra on 6/6/17.
 */

public class CustomPagerHomeAdapter extends PagerAdapter {

    private Context mContext;
    private List<SliderItem> mSliders;
    private ICallbackResponse<SliderItem> mCallback;


    public CustomPagerHomeAdapter(Context context, List<SliderItem> mSliders, ICallbackResponse<SliderItem> mCallback) {
        this.mContext = context;
        this.mSliders = mSliders;
        this.mCallback = mCallback;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final SliderItem mItem = mSliders.get(position);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_pager_home, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_product);
        ProgressBar progressLoader = (ProgressBar) itemView.findViewById(R.id.progress_loader);
        //TextView tvText = (TextView) itemView.findViewById(R.id.tv_text);

        ImageUtils.loadImageFromUrl(mContext, mItem.getImage(), imageView, progressLoader);

        SliderMetadata metadata = mItem.getMetadata();

        /*
        if (Constants.TYPE_PRODUCT.equalsIgnoreCase(mItem.getType())) {

            if (metadata != null)
                tvText.setText(!TextUtils.isEmpty(metadata.getName()) ? metadata.getName() : " - ");

        } else if (Constants.TYPE_PROMO.equalsIgnoreCase(mItem.getType())) {
            if (metadata != null)
                tvText.setText(!TextUtils.isEmpty(metadata.getDescription()) ? Html.fromHtml(metadata.getDescription()) : " - ");
        }
        */
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onCallback(mItem);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mSliders != null ? mSliders.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
