package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SOFTCO on 1/20/2017.
 */

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ContainerViewHolder> {

    private static final String TAG = ProductDetailAdapter.class.getSimpleName();
    private Context mContext;
    private View mView;

    public ProductDetailAdapter(Context mContext, View mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public ContainerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContainerViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ContainerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ContainerViewHolder extends RecyclerView.ViewHolder {

        public ContainerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
