package com.muuyal.sika.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.makeramen.roundedimageview.RoundedImageView;
import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackAdapterResponse;
import com.muuyal.sika.model.Solution;
import com.muuyal.sika.model.SolutionSection;
import com.muuyal.sika.utils.GeneralUtils;
import com.muuyal.sika.utils.ImageUtils;
import com.muuyal.sika.utils.TextUtils;

/**
 * Created by Isra on 5/23/17.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private Activity mContext;
    private LayoutInflater mInflater;
    private SolutionSection mSection;
    private ICallbackAdapterResponse mCallback;

    int size;

    public SuggestionAdapter(Activity mContext, SolutionSection mSection, ICallbackAdapterResponse mCallback){
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mSection = mSection;
        this.mCallback = mCallback;

        size = GeneralUtils.getWidthScreen(mContext) / 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        switch (viewType) {
            case TYPE_HEADER:
                mViewHolder = new HeaderVH(mInflater.inflate(R.layout.view_home_title, parent, false));
                break;
            case TYPE_ITEM:
                mViewHolder = new ItemVH(mInflater.inflate(R.layout.view_home_item, parent, false));
                break;
        }

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderVH) {
            ((HeaderVH)holder).tvSection.setText(!TextUtils.isEmpty(mSection.getTitle()) ? mSection.getTitle() : "");
        } else if (holder instanceof ItemVH) {
            final Solution mSolution = mSection.getItems().get(position - 1);
            String textToShow = mSolution.getTitle();
            String [] textSplit = textToShow.split(" ", 2);

            final int color = mContext.getResources().getColor(R.color.gray_section);
            ((ItemVH)holder).tvItemTitle.setText(!TextUtils.isEmpty(textToShow) ? textToShow : " ");

            ImageUtils.loadImageFromUrl(mContext, mSolution.getImage(), ((ItemVH)holder).ivItem, ((ItemVH)holder).progressLoader);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)((ItemVH) holder).containerItem.getLayoutParams();
            params.width = size;
            params.height = size;

            ((ItemVH)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSolution.setColor(color);
                    mSolution.setTitleSection(mSection.getTitle());
                    mCallback.onCallback(TYPE_ITEM, mSolution);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mSection.getItems() == null ? 0 : mSection.getItems().size() + 1;
    }

    public class HeaderVH extends RecyclerView.ViewHolder {

        private CustomTextView tvSection;

        public HeaderVH(View itemView) {
            super(itemView);
            tvSection = itemView.findViewById(R.id.tv_section);
        }
    }

    public class ItemVH extends RecyclerView.ViewHolder {

        private RoundedImageView ivItem;
        private CustomTextView tvItemTitle;
        private ProgressBar progressLoader;
        private ViewGroup containerItem;

        public ItemVH(View itemView) {
            super(itemView);

            ivItem = itemView.findViewById(R.id.iv_item);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            progressLoader = itemView.findViewById(R.id.progress_loader);
            containerItem = itemView.findViewById(R.id.container_item);
        }
    }
}
