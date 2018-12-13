package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muuyal.sika.R;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Product;
import com.muuyal.sika.model.Taxonomy;

import java.util.List;

/**
 * Created by Isra on 5/23/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int HEADER = 1;
    private static final int ITEM = 2;

    private Context mContext;
    private List<Product> mItems;
    private Taxonomy mTaxonomySelected;
    private ICallbackResponse<Product> mCallback;

    public ProductAdapter(Context mContext, List<Product> mItems, Taxonomy mTaxonomySelected, ICallbackResponse<Product> mCallback){
        this.mContext = mContext;
        this.mItems = mItems;
        this.mTaxonomySelected = mTaxonomySelected;
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
        if (holder instanceof HeaderVH) {
            String textToShow = mTaxonomySelected.getValue();
            String [] textSplit = textToShow.split(" ", 2);

            ((HeaderVH)holder).itemView.setBackgroundColor(mTaxonomySelected.getColor());
            if (textSplit.length > 1) {
                ((HeaderVH)holder).tvProductTitle.setText(textSplit[0]);
                ((HeaderVH)holder).tvProductDescr.setText(textSplit[1]);
            } else {
                ((HeaderVH)holder).tvProductTitle.setText(textToShow);
            }

        } else if (holder instanceof ItemVH) {
            final Product mProduct = mItems.get(position);
            ((ItemVH) holder).tvItemName.setText(mProduct.getName());
            ((ItemVH) holder).tvItemName.setTextColor(mTaxonomySelected.getColor());
            ((ItemVH) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onCallback(mProduct);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : ITEM;
    }

    public class HeaderVH extends RecyclerView.ViewHolder {

        private ImageView ivProductLogo;
        private TextView tvProductTitle;
        private TextView tvProductDescr;

        public HeaderVH(View itemView) {
            super(itemView);

            ivProductLogo = (ImageView) itemView.findViewById(R.id.iv_product_logo);
            tvProductTitle = (TextView) itemView.findViewById(R.id.tv_product_title);
            tvProductDescr = (TextView) itemView.findViewById(R.id.tv_product_descr);

        }
    }

    public class ItemVH extends RecyclerView.ViewHolder {

        private TextView tvItemName;

        public ItemVH(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
        }
    }
}
