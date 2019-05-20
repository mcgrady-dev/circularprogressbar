package com.mcgrady.xui.linkagerecyclerview.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by mcgrady on 2019/5/20.
 */
class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> headerViews = new SparseArray<>();
    private View convertView;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        convertView = itemView;
    }

    public <T extends View> T getView(int viewId) {
        View view = headerViews.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            headerViews.put(viewId, view);
        }
        return (T) view;
    }
}
