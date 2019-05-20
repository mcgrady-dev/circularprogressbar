package com.mcgrady.xui.linkagerecyclerview.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

import com.mcgrady.xui.linkagerecyclerview.interf.ILinkagePrimaryAdapterConfig;

/**
 * Created by mcgrady on 2019/5/20.
 */
public class LinkagePrimaryViewHolder extends BaseViewHolder {

    public View groupTitle;
    public View rootView;
    private ILinkagePrimaryAdapterConfig mConfig;

    public LinkagePrimaryViewHolder(@NonNull View itemView, ILinkagePrimaryAdapterConfig config) {
        super(itemView);
        mConfig = config;
        groupTitle = itemView.findViewById(mConfig.getGroupTitleViewId());
        //need bind root layout by users, because rootLayout may not viewGroup, which can not getChild(0).
        rootView = itemView.findViewById(mConfig.getRootViewId());
    }
}
