package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Store;
import com.muuyal.sika.utils.TextUtils;

import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class NearbyStoresAdapter extends RecyclerView.Adapter<NearbyStoresAdapter.StoreVH>{

    private Context mContext;
    private List<Store> mStores;
    private ICallbackResponse<Store> mCallback;

    public NearbyStoresAdapter(Context mContext, List<Store> mStores, ICallbackResponse<Store> mCallback) {
        this.mContext = mContext;
        this.mStores = mStores;
        this.mCallback = mCallback;
    }

    @Override
    public StoreVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreVH(LayoutInflater.from(mContext).inflate(R.layout.item_nearby_store, parent, false));
    }

    @Override
    public void onBindViewHolder(StoreVH holder, int position) {

        final Store store = mStores.get(position);
        holder.tvStoreTitle.setText(!TextUtils.isEmpty(store.getName()) ? store.getName() : "");
        holder.tvStoreDescr.setText(!TextUtils.isEmpty(store.getAddress()) ? store.getAddress() : "");
        holder.containerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onCallback(store);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStores != null ? mStores.size() : 0;
    }

    public class StoreVH extends RecyclerView.ViewHolder {

        private ViewGroup containerItem;
        private CustomTextView tvStoreTitle;
        private CustomTextView tvStoreDescr;

        public StoreVH(View itemView) {
            super(itemView);

            containerItem = itemView.findViewById(R.id.container_item);
            tvStoreTitle = itemView.findViewById(R.id.tv_store_title);
            tvStoreDescr = itemView.findViewById(R.id.tv_store_desc);
        }
    }
}
