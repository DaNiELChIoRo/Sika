package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Taxonomy;

import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class TaxonomyAdapter extends RecyclerView.Adapter<TaxonomyAdapter.TaxonomyVH> {

    private Context mContext;
    private List<Taxonomy> mTaxonomyList;
    private ICallbackResponse<Taxonomy> mCallback;

    public TaxonomyAdapter(Context mContext, List<Taxonomy> mTaxonomyList, ICallbackResponse<Taxonomy> mCallback) {
        this.mContext = mContext;
        this.mTaxonomyList = mTaxonomyList;
        this.mCallback = mCallback;
    }

    @Override
    public TaxonomyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaxonomyVH(LayoutInflater.from(mContext).inflate(R.layout.item_search_taxonomy, parent, false));
    }

    @Override
    public void onBindViewHolder(TaxonomyVH holder, int position) {

        final Taxonomy taxonomy = mTaxonomyList.get(position);
        holder.tvProductName.setText(taxonomy.getValue());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onCallback(taxonomy);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaxonomyList != null ? mTaxonomyList.size() : 0;
    }

    public class TaxonomyVH extends RecyclerView.ViewHolder {

        private CustomTextView tvProductName;

        public TaxonomyVH(View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tv_product_name);
        }
    }
}
