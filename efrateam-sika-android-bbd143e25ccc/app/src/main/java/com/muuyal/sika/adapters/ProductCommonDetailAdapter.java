package com.muuyal.sika.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Item;
import com.muuyal.sika.model.ProductItem;
import com.muuyal.sika.model.Step;
import com.muuyal.sika.utils.GeneralUtils;
import com.muuyal.sika.utils.ImageUtils;
import com.muuyal.sika.utils.TextUtils;

import java.util.List;

/**
 * Created by Isra on 1/20/2017.
 */

public class ProductCommonDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ProductCommonDetailAdapter.class.getSimpleName();

    public static final int COMMON_HEADER = 1;
    public static final int COMMON_NEEDED = 2;
    public static final int COMMON_STEP = 3;
    public static final int COMMON_LAST_STEP = 4;
    public static final int COMMON_FOOTER = 5;

    private Activity mContext;
    private Item mItem;
    private List<Step> mSteps;
    private int color;
    private ICallbackResponse<ProductItem> mCallback;

    public ProductCommonDetailAdapter(Activity mContext, Item mItem, List<Step> mSteps, int color, ICallbackResponse<ProductItem> mCallback) {
        this.mContext = mContext;
        this.mItem = mItem;
        this.mSteps = mSteps;
        this.color = color;
        this.mCallback = mCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == COMMON_HEADER)
            return new HeaderVH(LayoutInflater.from(mContext).inflate(R.layout.item_common_header, parent, false));
        else if (viewType == COMMON_NEEDED)
            return new NeededVH(LayoutInflater.from(mContext).inflate(R.layout.item_common_needed, parent, false));
        else if (viewType == COMMON_STEP)
            return new StepVH(LayoutInflater.from(mContext).inflate(R.layout.item_common_step, parent, false));
        else if (viewType == COMMON_LAST_STEP)
            return new LastStepVH(LayoutInflater.from(mContext).inflate(R.layout.item_common_last_step, parent, false));
        else if (viewType == COMMON_FOOTER)
            return new FooterVH(LayoutInflater.from(mContext).inflate(R.layout.item_common_footer, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderVH) {
            HeaderVH header = ((HeaderVH) holder);
            header.tvHeaderTitle.setText(!TextUtils.isEmpty(mItem.getTitle()) ? mItem.getTitle() : "");
            header.tvHeaderTitle.setBackgroundColor(color);

            header.tvHeaderDescr.setText(!TextUtils.isEmpty(mItem.getCaption()) ? mItem.getCaption() : "");
            header.tvHeaderDescr.setBackgroundColor(color);
            if (TextUtils.isEmpty(mItem.getCaption()))
                header.tvHeaderDescr.setPadding(5,5,5,5);

            ImageUtils.loadImageFromUrl(mContext, mItem.getImage(), header.ivHeaderLogo, header.progressLoader);
            ((HeaderVH) holder).ivHeaderLogo.setLayoutParams(GeneralUtils.getRelativeSizeFromUrl(mContext, mItem.getImage()));

        } else if (holder instanceof NeededVH) {
            ((NeededVH) holder).containerProductsNeeded.removeAllViews();
            List<ProductItem> mProducts = mItem.getProducts();
            if (mProducts != null && !mProducts.isEmpty()) {
                for (final ProductItem mProduct : mItem.getProducts()) {
                    View viewNeeded = View.inflate(mContext, R.layout.view_product_needed, null);
                    ((CustomTextView) viewNeeded.findViewById(R.id.tv_needed_name)).setText(!TextUtils.isEmpty(mProduct.getTitle()) ? mProduct.getTitle() : "");

                    ImageView ivNeededLogo = viewNeeded.findViewById(R.id.iv_needed_logo);
                    ProgressBar progressLoader = viewNeeded.findViewById(R.id.progress_loader);
                    ImageUtils.loadImageFromUrl(mContext, mProduct.getImage(), ivNeededLogo, progressLoader);

                    viewNeeded.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mCallback != null)
                                mCallback.onCallback(mProduct);
                        }
                    });

                    ((NeededVH) holder).containerProductsNeeded.addView(viewNeeded);
                }
            } else {
                ((NeededVH) holder).containerProductsNeeded.setVisibility(View.GONE);
            }
        } else if (holder instanceof StepVH) {
            Step mStep = mSteps.get(position - 2);
            ImageUtils.loadImageFromUrl(mContext, mStep.getImage(), ((StepVH)holder).ivStepLogo, ((StepVH)holder).progressLoader);

            ((StepVH) holder).tvStepDescr.setText(Html.fromHtml(mStep.getCaption()));
        } else if (holder instanceof LastStepVH) {
            Step mStep = mSteps.get(position - 2);
            ImageUtils.loadImageFromUrl(mContext, mStep.getImage(), ((LastStepVH)holder).ivStepLogo, ((LastStepVH)holder).progressLoader);

            ((LastStepVH) holder).tvStepDescr.setText(Html.fromHtml(mStep.getCaption()));
        } else if (holder instanceof FooterVH) {
            String url = !TextUtils.isEmpty(mItem.getDemo()) ? mItem.getDemo() : "-";
            if (url.equalsIgnoreCase("-")) {
                ((FooterVH) holder).ivDemo.setVisibility(View.GONE);
                ((FooterVH) holder).progressLoader.setVisibility(View.GONE);
            } else {
                ImageUtils.loadImageFromUrl(mContext, url, ((FooterVH) holder).ivDemo, ((FooterVH) holder).progressLoader);
                ((FooterVH) holder).ivDemo.setLayoutParams(GeneralUtils.getRelativeSizeFromUrl(mContext, url));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSteps != null ? mSteps.size() + 3 : 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return COMMON_HEADER;
        else if (position == 1)
            return COMMON_NEEDED;
        else if (position == (getItemCount() - 2) && (getItemCount() - 2) % 2 == 0)//last and impar ** +1 common header
            return COMMON_LAST_STEP;
        else if (position == getItemCount() - 1)
            return COMMON_FOOTER;
        else
            return COMMON_STEP;
    }

    public class HeaderVH extends RecyclerView.ViewHolder {

        private CustomTextView tvHeaderTitle;
        private ImageView ivHeaderLogo;
        private CustomTextView tvHeaderDescr;
        private ProgressBar progressLoader;

        public HeaderVH(View itemView) {
            super(itemView);

            tvHeaderTitle = itemView.findViewById(R.id.tv_header_title);
            ivHeaderLogo = itemView.findViewById(R.id.iv_header_logo);
            tvHeaderDescr = itemView.findViewById(R.id.tv_header_descr);
            progressLoader = itemView.findViewById(R.id.progress_loader);
        }
    }

    public class NeededVH extends RecyclerView.ViewHolder {

        private ViewGroup containerProductsNeeded;

        public NeededVH(View itemView) {
            super(itemView);

            containerProductsNeeded = itemView.findViewById(R.id.container_products_needed);
        }
    }

    public class StepVH extends RecyclerView.ViewHolder {

        private ImageView ivStepLogo;
        private CustomTextView tvStepDescr;
        private ProgressBar progressLoader;

        public StepVH(View itemView) {
            super(itemView);

            ivStepLogo = itemView.findViewById(R.id.iv_step_logo);
            tvStepDescr = itemView.findViewById(R.id.tv_step_descr);
            progressLoader = itemView.findViewById(R.id.progress_loader);
        }
    }

    public class LastStepVH extends RecyclerView.ViewHolder {

        private ImageView ivStepLogo;
        private CustomTextView tvStepDescr;
        private ProgressBar progressLoader;

        public LastStepVH(View itemView) {
            super(itemView);

            ivStepLogo = itemView.findViewById(R.id.iv_step_logo);
            tvStepDescr = itemView.findViewById(R.id.tv_step_descr);
            progressLoader = itemView.findViewById(R.id.progress_loader);
        }
    }

    public class FooterVH extends RecyclerView.ViewHolder {

        private ImageView ivDemo;
        private ProgressBar progressLoader;

        public FooterVH(View itemView) {
            super(itemView);

            ivDemo = itemView.findViewById(R.id.iv_demo);
            progressLoader = itemView.findViewById(R.id.progress_loader);
        }
    }
}
