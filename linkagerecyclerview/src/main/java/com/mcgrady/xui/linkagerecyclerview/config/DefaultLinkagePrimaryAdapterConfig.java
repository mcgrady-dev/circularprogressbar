package com.mcgrady.xui.linkagerecyclerview.config;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mcgrady.xui.linkagerecyclerview.R;
import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkagePrimaryViewHolder;
import com.mcgrady.xui.linkagerecyclerview.interf.ILinkagePrimaryAdapterConfig;

/**
 * Created by mcgrady on 2019/5/20.
 */
public class DefaultLinkagePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {
    private Context mContext;
    private OnPrimaryItemBindListener mListener;

    public void setListener(OnPrimaryItemBindListener listener) {
        mListener = listener;
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.default_adapter_linkage_primary;
    }

    @Override
    public int getGroupTitleViewId() {
        return R.id.tv_group;
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_group;
    }

    @Override
    public void onBindViewHolder(LinkagePrimaryViewHolder holder, String title, int position) {

        ((TextView) holder.groupTitle).setText(title);

        if (mListener != null) {
            mListener.onBindViewHolder(holder, title, position);
        }
    }

    @Override
    public void onItemSelected(boolean selected, View itemView) {
        TextView textView = (TextView) itemView;
        textView.setBackgroundColor(mContext.getResources().getColor(selected ? R.color.colorPurple : R.color.colorWhite));
        textView.setTextColor(ContextCompat.getColor(mContext, selected ? R.color.colorWhite : R.color.colorGray));
        textView.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
        textView.setFocusable(selected);
        textView.setFocusableInTouchMode(selected);
        textView.setMarqueeRepeatLimit(selected ? -1 : 0);
    }

    public interface OnPrimaryItemBindListener {
        /**
         * Note: Please do not override rootView click listener in here, because of linkage selection rely on it.
         *
         * @param primaryHolder primaryHolder
         * @param title         groupTitle
         * @param position      position
         */
        void onBindViewHolder(LinkagePrimaryViewHolder primaryHolder, String title, int position);
    }
}
