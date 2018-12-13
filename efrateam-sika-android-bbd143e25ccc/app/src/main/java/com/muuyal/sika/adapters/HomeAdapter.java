package com.muuyal.sika.adapters;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.interfaces.ICallbackAdapterResponse;
import com.muuyal.sika.model.SolutionSection;

import java.util.List;

/**
 * Created by Isra on 5/23/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_SECTION = 2;

    private Activity mContext;
    private LayoutInflater mInflater;
    private List<SolutionSection> mSections;
    private ICallbackAdapterResponse mCallback;
    private View mHeaderView;

    public HomeAdapter(Activity mContext, List<SolutionSection> mSections, View mHeaderView, ICallbackAdapterResponse mCallback){
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mSections = mSections;
        this.mHeaderView = mHeaderView;
        this.mCallback = mCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        switch (viewType) {
            case TYPE_HEADER:
                mViewHolder = new HeaderVH(mHeaderView);
                break;
            case TYPE_SECTION:
                mViewHolder = new SectionVH(mInflater.inflate(R.layout.view_home_section, parent, false));
                break;
        }

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionVH) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
            final SuggestionAdapter mAdapter = new SuggestionAdapter(mContext, mSections.get(position - 1), mCallback);
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mAdapter.getItemViewType(position)) {
                        case SuggestionAdapter.TYPE_HEADER:
                            return 2;
                        case SuggestionAdapter.TYPE_ITEM:
                            return 1;
                        default:
                            return -1;
                    }
                }
            });
            ((SectionVH)holder).rvSection.setLayoutManager(mLayoutManager);
            ((SectionVH)holder).rvSection.setHasFixedSize(true);
            ((SectionVH)holder).rvSection.setAdapter(mAdapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_SECTION;
        }
    }

    @Override
    public int getItemCount() {
        return mSections == null ? 1 : (mSections.size() + 1);
    }

    public class HeaderVH extends RecyclerView.ViewHolder {

        public HeaderVH(View itemView) {
            super(itemView);
        }
    }

    public class LabelVH extends RecyclerView.ViewHolder {
        public LabelVH(View itemView) {
            super(itemView);
        }
    }

    public class SectionVH extends RecyclerView.ViewHolder {

        private RecyclerView rvSection;

        public SectionVH(View itemView) {
            super(itemView);

            rvSection = (RecyclerView)itemView.findViewById(R.id.rv_section);
        }
    }
}
