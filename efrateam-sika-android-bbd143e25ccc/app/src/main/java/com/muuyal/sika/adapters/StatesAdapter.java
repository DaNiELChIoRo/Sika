package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.interfaces.ICallbackResponse;
import com.muuyal.sika.model.State;

import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.StateVH>{

    private Context mContext;
    private List<State> mStates;
    private ICallbackResponse<State> mCallback;

    public StatesAdapter (Context mContext, List<State> mStates, ICallbackResponse<State> mCallback) {
        this.mContext = mContext;
        this.mStates = mStates;
        this.mCallback = mCallback;
    }

    @Override
    public StateVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StateVH(LayoutInflater.from(mContext).inflate(R.layout.item_state, parent, false));
    }

    @Override
    public void onBindViewHolder(StateVH holder, int position) {

        final State state = mStates.get(position);
        holder.tvStateName.setText(state.getName());
        holder.containerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onCallback(state);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStates != null ? mStates.size() : 0;
    }

    public class StateVH extends RecyclerView.ViewHolder {

        private ViewGroup containerState;
        private CustomTextView tvStateName;

        public StateVH(View itemView) {
            super(itemView);

            containerState = itemView.findViewById(R.id.container_state);
            tvStateName = itemView.findViewById(R.id.tv_state_name);
        }
    }
}
