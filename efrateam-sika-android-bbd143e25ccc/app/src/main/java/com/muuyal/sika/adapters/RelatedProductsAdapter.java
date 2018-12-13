package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.ProductRelated;
import com.muuyal.sika.utils.ImageUtils;

import java.util.List;

/**
 * Created by isra on 6/8/17.
 */

public class RelatedProductsAdapter extends RecyclerView.Adapter<RelatedProductsAdapter.RelatedProduct> {

    private Context mContext;
    private List<ProductRelated> mRelatedProducts;
    private ICallbackResponse<ProductRelated> mCallback;

    public RelatedProductsAdapter(Context mContext, List<ProductRelated> mRelatedProducts, ICallbackResponse<ProductRelated> mCallback) {
        this.mContext = mContext;
        this.mRelatedProducts = mRelatedProducts;
        this.mCallback = mCallback;
    }

    @Override
    public RelatedProduct onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RelatedProduct(LayoutInflater.from(mContext).inflate(R.layout.item_related_product, parent, false));
    }

    @Override
    public void onBindViewHolder(RelatedProduct holder, final int position) {
        final ProductRelated mProduct = mRelatedProducts.get(position);
        holder.tvRelatedProduct.setText(mProduct.getName());

        ImageUtils.loadImageFromUrl(mContext, mProduct.getPhoto(), holder.ivRelatedProduct, holder.progressLoader);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onCallback(mProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRelatedProducts != null ? mRelatedProducts.size() : 0;
    }

    public class RelatedProduct extends RecyclerView.ViewHolder {

        private ImageView ivRelatedProduct;
        private CustomTextView tvRelatedProduct;
        private ProgressBar progressLoader;

        public RelatedProduct(View itemView) {
            super(itemView);

            ivRelatedProduct = itemView.findViewById(R.id.iv_related_product);
            tvRelatedProduct = itemView.findViewById(R.id.tv_related_product);
            progressLoader = itemView.findViewById(R.id.progress_loader);
        }
    }
}
