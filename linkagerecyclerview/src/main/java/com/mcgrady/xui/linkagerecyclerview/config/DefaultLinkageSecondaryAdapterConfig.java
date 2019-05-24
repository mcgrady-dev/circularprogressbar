package com.mcgrady.xui.linkagerecyclerview.config;

import android.content.Context;
import android.widget.TextView;

import com.mcgrady.xui.linkagerecyclerview.R;
import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkageSecondaryViewHolder;
import com.mcgrady.xui.linkagerecyclerview.bean.BaseLinkageGroupItem;
import com.mcgrady.xui.linkagerecyclerview.bean.DefaultLinkageGroupedItem;
import com.mcgrady.xui.linkagerecyclerview.interf.ILinkageSecondaryAdapterConfig;

/**
 * Created by mcgrady on 2019/5/20.
 */
public class DefaultLinkageSecondaryAdapterConfig implements ILinkageSecondaryAdapterConfig<DefaultLinkageGroupedItem.ItemInfo> {

    private Context mContext;
    private OnSecondaryItemBindListener mItemBindListener;
    private OnSecondaryHeaderBindListener mHeaderBindListener;
    private static final int SPAN_COUNT = 3;

    public void setItemBindListener(OnSecondaryItemBindListener itemBindListener, OnSecondaryHeaderBindListener headerBindListener) {
        mItemBindListener = itemBindListener;
        mHeaderBindListener = headerBindListener;
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getGridLayoutId() {
        return R.layout.default_adapter_linkage_secondary_grid;
    }

    @Override
    public int getLinearLayoutId() {
        return R.layout.default_adapter_linkage_secondary_linear;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.default_adapter_linkage_secondary_header;
    }

    @Override
    public int getHeaderTextViewId() {
        return R.id.secondary_header;
    }

    @Override
    public int getSpanCountOfGridMode() {
        return SPAN_COUNT;
    }

    @Override
    public void onBindViewHolder(LinkageSecondaryViewHolder holder, BaseLinkageGroupItem<DefaultLinkageGroupedItem.ItemInfo> groupItem, int position) {
        ((TextView) holder.getView(R.id.level_2_title)).setText(groupItem.itemInfo.getTitle());
        ((TextView) holder.getView(R.id.level_2_content)).setText(groupItem.itemInfo.getContent());

        if (mItemBindListener != null) {
            mItemBindListener.onBindViewHolder(holder, groupItem, position);
        }
    }

    @Override
    public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder, BaseLinkageGroupItem<DefaultLinkageGroupedItem.ItemInfo> item, int position) {
        ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);

        if (mHeaderBindListener != null) {
            mHeaderBindListener.onBindHeaderViewHolder(holder, item, position);
        }
    }

    public interface OnSecondaryItemBindListener {
        void onBindViewHolder(LinkageSecondaryViewHolder secondaryHolder,
                              BaseLinkageGroupItem<DefaultLinkageGroupedItem.ItemInfo> item, int position);
    }

    public interface OnSecondaryHeaderBindListener {
        void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder headerHolder,
                                    BaseLinkageGroupItem<DefaultLinkageGroupedItem.ItemInfo> item, int position);
    }
}
