package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.District;

import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictVH>{

    private Context mContext;
    private List<District> mDistricts;
    private ICallbackResponse<District> mCallback;

    public DistrictAdapter(Context mContext, List<District> mDistricts, ICallbackResponse<District> mCallback) {
        this.mContext = mContext;
        this.mDistricts = mDistricts;
        this.mCallback = mCallback;
    }

    @Override
    public DistrictVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DistrictVH(LayoutInflater.from(mContext).inflate(R.layout.item_state, parent, false));
    }

    @Override
    public void onBindViewHolder(DistrictVH holder, int position) {

        final District district = mDistricts.get(position);
        holder.tvStateName.setText(district.getName());
        holder.containerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onCallback(district);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDistricts != null ? mDistricts.size() : 0;
    }

    public class DistrictVH extends RecyclerView.ViewHolder {

        private ViewGroup containerState;
        private CustomTextView tvStateName;

        public DistrictVH(View itemView) {
            super(itemView);

            containerState = itemView.findViewById(R.id.container_state);
            tvStateName = itemView.findViewById(R.id.tv_state_name);
        }
    }
}
