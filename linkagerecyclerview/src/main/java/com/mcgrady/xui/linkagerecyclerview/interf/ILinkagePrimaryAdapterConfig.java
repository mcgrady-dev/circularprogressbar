package com.mcgrady.xui.linkagerecyclerview.interf;

import android.content.Context;
import android.view.View;

import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkagePrimaryViewHolder;

/**
 * Created by mcgrady on 2019/5/20.
 */
public interface ILinkagePrimaryAdapterConfig {

    /**
     * setContext
     *
     * @param context context
     */
    void setContext(Context context);

    /**
     * get layout res id
     *
     * @return layout res id
     */
    int getLayoutId();

    /**
     * get textView id of layout
     *
     * @return textView id of layout
     */
    int getGroupTitleViewId();

    /**
     * get rootView id of layout
     *
     * @return rootView id of layout
     */
    int getRootViewId();

    /**
     * achieve the onBindViewHolder logic on outside
     * <p>
     * Note: Do not setOnClickListener in onBindViewHolder,
     * instead, you can deal with item click in method 'ILinkagePrimaryAdapterConfig.onItemSelected()'
     * or 'LinkageRecyclerView.OnPrimaryItemClickListener.onItemClick()'
     *
     * @param holder   LinkagePrimaryViewHolder
     * @param title    title of this position
     * @param position holder.getAdapterPosition()
     */
    void onBindViewHolder(LinkagePrimaryViewHolder holder, String title, int position);

    /**
     * configurations of textView when selected or not
     *
     * @param selected if selected
     * @param itemView textView which you choose to config the expression.
     */
    void onItemSelected(boolean selected, View itemView);
}
