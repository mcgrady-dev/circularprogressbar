package com.mcgrady.xui.linkagerecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkagePrimaryViewHolder;
import com.mcgrady.xui.linkagerecyclerview.interf.ILinkagePrimaryAdapterConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcgrady on 2019/5/20.
 */
public class LinkagePrimaryAdapter extends RecyclerView.Adapter<LinkagePrimaryViewHolder> {

    private List<String> mStrings;
    private List<View> mGroupTitleViews = new ArrayList<>();
    private Context mContext;

    private ILinkagePrimaryAdapterConfig mConfig;
    private OnLinkageListener mLinkageListener;
    private OnItemClickListener mItemClickListener;

    public List<String> getStrings() {
        return mStrings;
    }

    public ILinkagePrimaryAdapterConfig getConfig() {
        return mConfig;
    }

    public LinkagePrimaryAdapter(List<String> strings, ILinkagePrimaryAdapterConfig config,
                                 OnLinkageListener linkageListener, OnItemClickListener onItemClickListener) {
        mStrings = strings;
        if (mStrings == null) {
            mStrings = new ArrayList<>();
        }
        mConfig = config;
        mLinkageListener = linkageListener;
        mItemClickListener = onItemClickListener;
    }

    public void refreshList(List<String> list) {
        mStrings.clear();
        if (list != null) {
            mStrings.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LinkagePrimaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mConfig.setContext(mContext);
        View view = LayoutInflater.from(mContext).inflate(mConfig.getLayoutId(), parent, false);
        return new LinkagePrimaryViewHolder(view, mConfig);
    }

    @Override
    public void onBindViewHolder(@NonNull final LinkagePrimaryViewHolder holder, int position) {

        // for textView MARQUEE available.
        holder.rootView.setSelected(true);

        mConfig.onBindViewHolder(holder, mStrings.get(holder.getAdapterPosition()), holder.getAdapterPosition());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinkageListener != null) {
                    mLinkageListener.onLinkageClick(holder, mStrings.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, mStrings.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            }
        });

        if (!mGroupTitleViews.contains(holder.groupTitle)) {
            mGroupTitleViews.add(holder.groupTitle);
        }
        if (mGroupTitleViews != null && mGroupTitleViews.size() == mStrings.size()) {
            selectItem(0);
        }
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public void selectItem(int position) {
        for (int i = 0; i < mStrings.size(); i++) {
            mConfig.onItemSelected(position == i, mGroupTitleViews.get(i));
        }
    }

    /**
     * only for linkage logic of level primary adapter. not use for outside logic
     * users can archive onLinkageClick in configs instead.
     */
    public interface OnLinkageListener {
        void onLinkageClick(LinkagePrimaryViewHolder holder, String title, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String title, int position);
    }
}
