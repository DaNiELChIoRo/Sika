package com.muuyal.sika.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muuyal.sika.R;
import com.muuyal.sika.customs.CustomTextView;
import com.muuyal.sika.model.Glossary;

import java.util.List;

/**
 * Created by isra on 7/27/17.
 */

public class GlossaryAdapter extends RecyclerView.Adapter<GlossaryAdapter.GlossaryVH> {

    private Context mContext;
    private List<Glossary> mGlossaryList;

    public GlossaryAdapter(Context mContext, List<Glossary> mGlossaryList) {
        this.mContext = mContext;
        this.mGlossaryList = mGlossaryList;
    }

    @Override
    public GlossaryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GlossaryVH(LayoutInflater.from(mContext).inflate(R.layout.item_glossary, parent, false));
    }

    @Override
    public void onBindViewHolder(GlossaryVH holder, int position) {

        final Glossary mGlossary = mGlossaryList.get(position);

        holder.tvGlossaryTitle.setText(mGlossary.getTitle());
        holder.tvGlossaryDescr.setText(mGlossary.getDescription());
    }

    @Override
    public int getItemCount() {
        return mGlossaryList != null ? mGlossaryList.size() : 0;
    }

    public class GlossaryVH extends RecyclerView.ViewHolder {

        private CustomTextView tvGlossaryTitle;
        private CustomTextView tvGlossaryDescr;

        public GlossaryVH(View itemView) {
            super(itemView);

            tvGlossaryTitle = itemView.findViewById(R.id.tv_glossary_title);
            tvGlossaryDescr = itemView.findViewById(R.id.tv_glossary_descr);
        }
    }
}
