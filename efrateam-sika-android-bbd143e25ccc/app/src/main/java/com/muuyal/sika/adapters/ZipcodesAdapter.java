package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.Zipcode;

import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class ZipcodesAdapter extends RecyclerView.Adapter<ZipcodesAdapter.ZipcodeVH>{

    private Context mContext;
    private List<Zipcode> mZipcodes;
    private ICallbackResponse<Zipcode> mCallback;

    public ZipcodesAdapter(Context mContext, List<Zipcode> mZipcodes, ICallbackResponse<Zipcode> mCallback) {
        this.mContext = mContext;
        this.mZipcodes = mZipcodes;
        this.mCallback = mCallback;
    }

    @Override
    public ZipcodeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ZipcodeVH(LayoutInflater.from(mContext).inflate(R.layout.item_state, parent, false));
    }

    @Override
    public void onBindViewHolder(ZipcodeVH holder, int position) {

        final Zipcode zipcode = mZipcodes.get(position);
        holder.tvStateName.setText(zipcode.getDistrict() + " " + zipcode.getSuburb());
        holder.containerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onCallback(zipcode);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mZipcodes != null ? mZipcodes.size() : 0;
    }

    public class ZipcodeVH extends RecyclerView.ViewHolder {

        private ViewGroup containerState;
        private CustomTextView tvStateName;

        public ZipcodeVH(View itemView) {
            super(itemView);

            containerState = itemView.findViewById(R.id.container_state);
            tvStateName = itemView.findViewById(R.id.tv_state_name);
        }
    }
}
