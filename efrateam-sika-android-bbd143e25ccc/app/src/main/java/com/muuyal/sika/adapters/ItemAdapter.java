package com.muuyal.sika.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Item;
import com.muuyal.sika.model.Solution;
import com.muuyal.sika.utils.ColorUtils;
import com.muuyal.sika.utils.GeneralUtils;
import com.muuyal.sika.utils.ImageUtils;
import com.muuyal.sika.utils.LoggerUtils;

import java.util.List;

/**
 * Created by Isra on 5/23/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int HEADER = 1;
    private static final int ITEM = 2;

    private Activity mContext;
    private List<Item> mItems;
    private Solution mSuggestionSelected;
    private ICallbackResponse<Item> mCallback;

    public ItemAdapter(Activity mContext, List<Item> mItems, Solution mSuggestionSelected, ICallbackResponse<Item> mCallback){
        this.mContext = mContext;
        this.mItems = mItems;
        this.mSuggestionSelected = mSuggestionSelected;
        this.mCallback = mCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER)
            return new HeaderVH(LayoutInflater.from(mContext).inflate(R.layout.item_product_header, parent, false));
        else if (viewType == ITEM)
            return new ItemVH(LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false));
        else
            return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int color = 255;
        try {
            color = mSuggestionSelected.getColor();
        } catch (Exception e) {
            LoggerUtils.logError("Error parse color: " + e);
        }
        if (holder instanceof HeaderVH) {
            String textToShow = mSuggestionSelected.getTitle();
            String [] textSplit = textToShow.split(" ", 2);

            try {
                ((HeaderVH) holder).itemView.setBackgroundColor(color);
            } catch (Exception e) {
                LoggerUtils.logError("Error parse color: " + e);
            }
            /*if (textSplit.length > 1) {
                ((HeaderVH)holder).tvProductTitle.setText(textSplit[0]);
                ((HeaderVH)holder).tvProductDescr.setText(textSplit[1]);
            } else {
                ((HeaderVH)holder).tvProductTitle.setText(textToShow);
            }*/

            ((HeaderVH)holder).tvTitleSection.setText(mSuggestionSelected.getTitleSection());
            ((HeaderVH)holder).tvProductTitle.setText(textToShow);
            ImageUtils.loadImageFromUrl(mContext, mSuggestionSelected.getImage(), ((HeaderVH)holder).ivProductLogo, ((HeaderVH)holder).progressLoader);
            RelativeLayout.LayoutParams params = GeneralUtils.getRelativeSizeFromUrl(mContext, mSuggestionSelected.getImage());
            ((HeaderVH)holder).ivProductLogo.setLayoutParams(params);
            ((HeaderVH)holder).containerImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, params.height));

        } else if (holder instanceof ItemVH) {
            final Item mItem = mItems.get(position - 1);
            ((ItemVH) holder).tvItemName.setText(mItem.getTitle());
            ((ItemVH) holder).tvItemName.setTextColor(ColorUtils.getCustomColorStateList(mContext, color));
            ImageUtils.loadImageFromUrl(mContext, mItem.getImage(), ((ItemVH)holder).ivItemLogo, ((ItemVH)holder).progressLoader);

            ((ItemVH) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onCallback(mItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 1 : mItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : ITEM;
    }

    public class HeaderVH extends RecyclerView.ViewHolder {

        private CustomTextView tvTitleSection;
        private ImageView ivProductLogo;
        private CustomTextView tvProductTitle;
        private ProgressBar progressLoader;
        private ViewGroup containerImage;

        public HeaderVH(View itemView) {
            super(itemView);

            tvTitleSection = itemView.findViewById(R.id.tv_title_section);
            ivProductLogo = itemView.findViewById(R.id.iv_product_logo);
            tvProductTitle = itemView.findViewById(R.id.tv_product_title);
            progressLoader = itemView.findViewById(R.id.progress_loader);
            containerImage = itemView.findViewById(R.id.container_image);

        }
    }

    public class ItemVH extends RecyclerView.ViewHolder {

        private CustomTextView tvItemName;
        private ImageView ivItemLogo;
        private ProgressBar progressLoader;

        public ItemVH(View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tv_item_name);
            ivItemLogo = itemView.findViewById(R.id.iv_item_logo);
            progressLoader = itemView.findViewById(R.id.progress_loader);
        }
    }
}
