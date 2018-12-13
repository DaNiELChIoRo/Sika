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
import com.muuyal.sika.model.Product;
import com.muuyal.sika.utils.ImageUtils;
import com.muuyal.sika.utils.TextUtils;

import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.ProductVH>{

    private Context mContext;
    private List<Product> mProducts;
    private ICallbackResponse<Product> mCallback;

    public SearchProductsAdapter(Context mContext, List<Product> mProducts, ICallbackResponse<Product> mCallback) {
        this.mContext = mContext;
        this.mProducts = mProducts;
        this.mCallback = mCallback;
    }

    @Override
    public ProductVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductVH(LayoutInflater.from(mContext).inflate(R.layout.item_search_product, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductVH holder, int position) {

        final Product product = mProducts.get(position);
        holder.tvProductName.setText(product.getName());

        String url = (product.getPhoto() != null && !TextUtils.isEmpty(product.getPhoto().getUrl())) ? product.getPhoto().getUrl() : "-";
        ImageUtils.loadImageFromUrl(mContext, url, holder.ivProductLogo, holder.progressLoader);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onCallback(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts != null ? mProducts.size() : 0;
    }

    public class ProductVH extends RecyclerView.ViewHolder {

        private CustomTextView tvProductName;
        private ImageView ivProductLogo;
        private ProgressBar progressLoader;

        public ProductVH(View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tv_product_name);
            ivProductLogo = itemView.findViewById(R.id.iv_product_logo);
            progressLoader = itemView.findViewById(R.id.progress_loader);
        }
    }
}
