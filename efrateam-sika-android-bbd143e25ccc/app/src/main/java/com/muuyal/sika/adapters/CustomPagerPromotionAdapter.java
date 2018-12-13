package com.muuyal.sika.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Promotion;
import com.muuyal.sika.utils.GeneralUtils;
import com.muuyal.sika.utils.ImageUtils;

import java.util.List;

/**
 * Created by Isra on 6/6/17.
 */

public class CustomPagerPromotionAdapter extends PagerAdapter {

    private Activity mContext;
    private List<Promotion> mPromotionList;
    private ICallbackResponse<Promotion> mCallback;

    public CustomPagerPromotionAdapter(Activity mContext, List<Promotion> mPromotionList, ICallbackResponse<Promotion> mCallback) {
        this.mContext = mContext;
        this.mPromotionList = mPromotionList;
        this.mCallback = mCallback;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final Promotion mPromotion = mPromotionList.get(position);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_promotion, container, false);

        ImageView ivPromotionLogo = itemView.findViewById(R.id.iv_promotion_logo);
        CustomTextView tvPromotionDesc = itemView.findViewById(R.id.tv_promotion_desc);
        ProgressBar progressLoader = itemView.findViewById(R.id.progress_loader);
        ViewGroup containerSelector = itemView.findViewById(R.id.container_selector);

        ImageUtils.loadImageFromUrl(mContext, mPromotion.getImage(), ivPromotionLogo, progressLoader);

        ivPromotionLogo.setLayoutParams(GeneralUtils.getRelativeSizeFromUrl(mContext, mPromotion.getImage(), 20));
        tvPromotionDesc.setText(Html.fromHtml(mPromotion.getDescription()));

        containerSelector.setLayoutParams(GeneralUtils.getRelativeSizeFromUrl(mContext, mPromotion.getImage(), 20));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onCallback(mPromotion);
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
        return mPromotionList != null ? mPromotionList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
