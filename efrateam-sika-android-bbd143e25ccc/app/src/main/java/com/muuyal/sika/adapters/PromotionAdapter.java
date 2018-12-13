package com.muuyal.sika.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muuyal.sika.R;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Promotion;
import com.muuyal.sika.utils.GeneralUtils;
import com.muuyal.sika.utils.ImageUtils;

import java.util.List;

/**
 * Created by isra on 7/27/17.
 */

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionVH> {

    private Activity mActivity;
    private List<Promotion> mPromotions;
    private ICallbackResponse<Promotion> mCallback;

    public PromotionAdapter(Activity mActivity, List<Promotion> mPromotions, ICallbackResponse<Promotion> mCallback) {
        this.mActivity = mActivity;
        this.mPromotions = mPromotions;
        this.mCallback = mCallback;
    }

    @Override
    public PromotionVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PromotionVH(LayoutInflater.from(mActivity).inflate(R.layout.item_promotion, parent, false));
    }

    @Override
    public void onBindViewHolder(PromotionVH holder, int position) {

        final Promotion mPromotion = mPromotions.get(position);
        ImageUtils.loadImageFromUrl(mActivity, mPromotion.getImage(), holder.ivPromotionLogo, holder.progressLoader);

        holder.ivPromotionLogo.setLayoutParams(GeneralUtils.getRelativeSizeFromUrl(mActivity, mPromotion.getImage()));
        holder.tvPromotionDesc.setText(Html.fromHtml(mPromotion.getDescription()));

        holder.containerSelector.setLayoutParams(GeneralUtils.getRelativeSizeFromUrl(mActivity, mPromotion.getImage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onCallback(mPromotion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPromotions != null ? mPromotions.size() : 0;
    }

    public class PromotionVH extends RecyclerView.ViewHolder {

        private ImageView ivPromotionLogo;
        private TextView tvPromotionDesc;
        private ProgressBar progressLoader;
        private ViewGroup containerSelector;

        public PromotionVH(View itemView) {
            super(itemView);

            ivPromotionLogo = (ImageView) itemView.findViewById(R.id.iv_promotion_logo);
            tvPromotionDesc = (TextView) itemView.findViewById(R.id.tv_promotion_desc);
            progressLoader = (ProgressBar) itemView.findViewById(R.id.progress_loader);
            containerSelector = (ViewGroup) itemView.findViewById(R.id.container_selector);
        }
    }
}
