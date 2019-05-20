package com.mcgrady.xui.linkagerecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkageSecondaryViewHolder;
import com.mcgrady.xui.linkagerecyclerview.bean.BaseLinkageGroupItem;
import com.mcgrady.xui.linkagerecyclerview.interf.ILinkageSecondaryAdapterConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcgrady on 2019/5/20.
 */
public class LinkageSecondaryAdapter<T extends BaseLinkageGroupItem.ItemInfo> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BaseLinkageGroupItem<T>> mItems;
    private static final int IS_HEADER = 0;
    private static final int IS_LINEAR = 1;
    private static final int IS_GRID = 2;
    private boolean mIsGridMode;

    private ILinkageSecondaryAdapterConfig mConfig;

    public ILinkageSecondaryAdapterConfig getConfig() {
        return mConfig;
    }

    public List<BaseLinkageGroupItem<T>> getItems() {
        return mItems;
    }

    public boolean isGridMode() {
        return mIsGridMode && mConfig.getGridLayoutId() != 0;
    }

    public void setGridMode(boolean isGridMode) {
        mIsGridMode = isGridMode;
    }

    public LinkageSecondaryAdapter(List<BaseLinkageGroupItem<T>> items, ILinkageSecondaryAdapterConfig config) {
        mItems = items;
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mConfig = config;
    }

    public void refreshList(List<BaseLinkageGroupItem<T>> list) {
        mItems.clear();
        if (list != null) {
            mItems.addAll(list);
        }
        notifyDataSetChanged();
    }

    //TODO load more data...
    public void refreshListLoadMore(List<BaseLinkageGroupItem<T>> list) {
        if (list != null) {
            mItems.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position).isHeader) {
            return IS_HEADER;
        } else if (isGridMode()) {
            return IS_GRID;
        } else {
            return IS_LINEAR;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mConfig.setContext(mContext);
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(mConfig.getHeaderLayoutId(), parent, false);
            return new LinkageSecondaryHeaderViewHolder(view);
        } else if (viewType == IS_GRID && mConfig.getGridLayoutId() != 0) {
            View view = LayoutInflater.from(mContext).inflate(mConfig.getGridLayoutId(), parent, false);
            return new LinkageSecondaryViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(mConfig.getLinearLayoutId(), parent, false);
            return new LinkageSecondaryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BaseLinkageGroupItem<T> linkageItem = mItems.get(holder.getAdapterPosition());
        if (linkageItem.isHeader) {
            LinkageSecondaryHeaderViewHolder headerViewHolder = (LinkageSecondaryHeaderViewHolder) holder;
            mConfig.onBindHeaderViewHolder(headerViewHolder, linkageItem, headerViewHolder.getAdapterPosition());
        } else {
            final LinkageSecondaryViewHolder secondaryViewHolder = (LinkageSecondaryViewHolder) holder;
            mConfig.onBindViewHolder(secondaryViewHolder, linkageItem, secondaryViewHolder.getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
