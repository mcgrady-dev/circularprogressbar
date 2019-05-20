package com.mcgrady.xui.linkagerecyclerview.interf;

import android.content.Context;

import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkageSecondaryViewHolder;
import com.mcgrady.xui.linkagerecyclerview.bean.BaseLinkageGroupItem;

/**
 * Created by mcgrady on 2019/5/20.
 */
public interface ILinkageSecondaryAdapterConfig<T extends BaseLinkageGroupItem.ItemInfo> {

    /**
     * setContext
     *
     * @param context context
     */
    void setContext(Context context);

    /**
     * get grid layout res id
     *
     * @return grid layout res id
     */
    int getGridLayoutId();

    /**
     * get linear layout res id
     *
     * @return linear layout res id
     */
    int getLinearLayoutId();

    /**
     * get header layout res id
     * <p>
     * Note: Secondary adapter's Header and HeaderView must share the same set of views
     *
     * @return header layout res id
     */
    int getHeaderLayoutId();

    /**
     * get the id of textView for bind title of HeaderView
     * <p>
     * Note: Secondary adapter's Header and HeaderView must share the same set of views
     *
     * @return
     */
    int getHeaderTextViewId();

    /**
     * get SpanCount of grid mode
     */
    int getSpanCountOfGridMode();

    /**
     * achieve the onBindViewHolder logic on outside
     *
     * @param holder   LinkageSecondaryViewHolder
     * @param item     linkageItem of this position
     * @param position holder.getAdapterPosition()
     */
    void onBindViewHolder(LinkageSecondaryViewHolder holder, BaseLinkageGroupItem<T> item, int position);

    /**
     * achieve the onBindHeaderViewHolder logic on outside
     *
     * @param holder   LinkageSecondaryViewHolder
     * @param item
     * @param position
     */
    void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder, BaseLinkageGroupItem<T> item, int position);
}
