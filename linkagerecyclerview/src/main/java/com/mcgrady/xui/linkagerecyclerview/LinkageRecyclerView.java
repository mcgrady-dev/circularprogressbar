package com.mcgrady.xui.linkagerecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcgrady.xui.linkagerecyclerview.adapter.LinkagePrimaryAdapter;
import com.mcgrady.xui.linkagerecyclerview.adapter.LinkageSecondaryAdapter;
import com.mcgrady.xui.linkagerecyclerview.adapter.viewholder.LinkagePrimaryViewHolder;
import com.mcgrady.xui.linkagerecyclerview.bean.BaseLinkageGroupItem;
import com.mcgrady.xui.linkagerecyclerview.config.DefaultLinkagePrimaryAdapterConfig;
import com.mcgrady.xui.linkagerecyclerview.config.DefaultLinkageSecondaryAdapterConfig;
import com.mcgrady.xui.linkagerecyclerview.interf.ILinkagePrimaryAdapterConfig;
import com.mcgrady.xui.linkagerecyclerview.interf.ILinkageSecondaryAdapterConfig;
import com.mcgrady.xui.linkagerecyclerview.manager.RecyclerViewScrollHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcgrady on 2019/5/20.
 */
public class LinkageRecyclerView<T extends BaseLinkageGroupItem.ItemInfo> extends RelativeLayout {

    private static final int DEFAULT_SPAN_COUNT = 1;
    private static final int SCROLL_OFFSET = 0;

    private Context mContext;

    private RecyclerView mRvPrimary;
    private RecyclerView mRvSecondary;
    private LinearLayout mLinkageLayout;

    private LinkagePrimaryAdapter mPrimaryAdapter;
    private LinkageSecondaryAdapter mSecondaryAdapter;
    private TextView mTvHeader;
    private FrameLayout mHeaderContainer;

    private List<String> mGroupNames;
    private List<BaseLinkageGroupItem<T>> mItems;

    private List<Integer> mHeaderPositions = new ArrayList<>();
    private int mTitleHeight;
    private int mFirstVisiblePosition = 0;
    private String mLastGroupName;
    private LinearLayoutManager mSecondaryLayoutManager;

    private boolean scrollSmoothly = true;

    private OnPrimaryItemClickListener mPrimaryItemClickListener;

    public LinkageRecyclerView(Context context) {
        super(context);
    }

    public LinkageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LinkageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_linkage_view, this);
        mRvPrimary = (RecyclerView) view.findViewById(R.id.rv_primary);
        mRvSecondary = (RecyclerView) view.findViewById(R.id.rv_secondary);
        mHeaderContainer = (FrameLayout) view.findViewById(R.id.header_container);
        mLinkageLayout = (LinearLayout) view.findViewById(R.id.linkage_layout);
    }

    private void setLevel2LayoutManager() {
        if (mSecondaryAdapter.isGridMode()) {
            mSecondaryLayoutManager = new GridLayoutManager(mContext, mSecondaryAdapter.getConfig().getSpanCountOfGridMode());
            ((GridLayoutManager) mSecondaryLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (((BaseLinkageGroupItem<T>) mSecondaryAdapter.getItems().get(position)).isHeader) {
                        return mSecondaryAdapter.getConfig().getSpanCountOfGridMode();
                    }
                    return DEFAULT_SPAN_COUNT;
                }
            });
        } else {
            mSecondaryLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        }
        mRvSecondary.setLayoutManager(mSecondaryLayoutManager);
    }

    private void initRecyclerView(ILinkagePrimaryAdapterConfig primaryAdapterConfig,
                                  ILinkageSecondaryAdapterConfig secondaryAdapterConfig) {

        mPrimaryAdapter = new LinkagePrimaryAdapter(mGroupNames, primaryAdapterConfig,
                new LinkagePrimaryAdapter.OnLinkageListener() {
                    @Override
                    public void onLinkageClick(LinkagePrimaryViewHolder holder, String title, int position) {
                        if (isScrollSmoothly()) {
                            RecyclerViewScrollHelper.scrollToPosition(mRvSecondary, mHeaderPositions.get(position));
                        } else {
                            mPrimaryAdapter.selectItem(position);
                            mSecondaryLayoutManager.scrollToPositionWithOffset(mHeaderPositions.get(position), SCROLL_OFFSET);
                        }
                    }
                },
                new LinkagePrimaryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, String title, int position) {
                        if (mPrimaryItemClickListener != null) {
                            mPrimaryItemClickListener.onItemClick(v, title, position);
                        }
                    }
                });

        mRvPrimary.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mRvPrimary.setAdapter(mPrimaryAdapter);

        mSecondaryAdapter = new LinkageSecondaryAdapter(mItems, secondaryAdapterConfig);
        setLevel2LayoutManager();
        mRvSecondary.setAdapter(mSecondaryAdapter);
    }

    private void initLinkageLevel2() {

        // Note: headerLayout is shared by both SecondaryAdapter's header and HeaderView

        if (mTvHeader == null && mSecondaryAdapter.getConfig() != null) {
            ILinkageSecondaryAdapterConfig config = mSecondaryAdapter.getConfig();
            int layout = config.getHeaderLayoutId();
            View view = LayoutInflater.from(mContext).inflate(layout, null);
            mHeaderContainer.addView(view);
            mTvHeader = view.findViewById(config.getHeaderTextViewId());
        }

        if (mItems.get(mFirstVisiblePosition).isHeader) {
            mTvHeader.setText(mItems.get(mFirstVisiblePosition).header);
        }

        mRvSecondary.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mTitleHeight = mTvHeader.getMeasuredHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstPosition = mSecondaryLayoutManager.findFirstVisibleItemPosition();

                // Here is the logic of the sticky:

                if (mItems.get(firstPosition + 1).isHeader) {
                    View view = mSecondaryLayoutManager.findViewByPosition(firstPosition + 1);
                    if (view != null && view.getTop() <= mTitleHeight) {
                        mTvHeader.setY(view.getTop() - mTitleHeight);
                    }
                }

                // Here is the logic of group title changes and linkage:

                boolean groupNameChanged = false;

                if (mFirstVisiblePosition != firstPosition && firstPosition >= 0) {
                    mFirstVisiblePosition = firstPosition;
                    mTvHeader.setY(0);

                    String currentGroupName = mItems.get(mFirstVisiblePosition).isHeader
                            ? mItems.get(mFirstVisiblePosition).header
                            : mItems.get(mFirstVisiblePosition).itemInfo.getGroup();

                    if (TextUtils.isEmpty(mLastGroupName) || !mLastGroupName.equals(currentGroupName)) {
                        mLastGroupName = currentGroupName;
                        groupNameChanged = true;
                        mTvHeader.setText(mLastGroupName);
                    }
                }

                if (groupNameChanged) {
                    for (int i = 0; i < mGroupNames.size(); i++) {
                        if (mGroupNames.get(i).equals(mTvHeader.getText().toString())) {
                            mPrimaryAdapter.selectItem(i);
                        }
                    }
                }

                if (mSecondaryLayoutManager.findLastCompletelyVisibleItemPosition() == mItems.size() - 1) {
                    mPrimaryAdapter.selectItem(mGroupNames.size() - 1);
                }
            }
        });
    }

    private int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }

    public void init(List<BaseLinkageGroupItem<T>> linkageItems,
                     ILinkagePrimaryAdapterConfig primaryAdapterConfig,
                     ILinkageSecondaryAdapterConfig secondaryAdapterConfig) {

        initRecyclerView(primaryAdapterConfig, secondaryAdapterConfig);

        this.mItems = linkageItems;

        List<String> groupNames = new ArrayList<>();
        if (mItems != null && mItems.size() > 0) {
            for (BaseLinkageGroupItem<T> item1 : mItems) {
                if (item1.isHeader) {
                    groupNames.add(item1.header);
                }
            }
        }
        if (mItems != null) {
            for (int i = 0; i < mItems.size(); i++) {
                if (mItems.get(i).isHeader) {
                    mHeaderPositions.add(i);
                }
            }
        }

        this.mGroupNames = groupNames;
        mPrimaryAdapter.refreshList(mGroupNames);
        mSecondaryAdapter.refreshList(mItems);
        initLinkageLevel2();
    }

    public void init(List<BaseLinkageGroupItem<T>> linkageItems) {
        init(linkageItems, new DefaultLinkagePrimaryAdapterConfig(), new DefaultLinkageSecondaryAdapterConfig());
    }

    public void setDefaultOnItemBindListener(
            OnPrimaryItemClickListener onPrimaryItemClickListener,
            DefaultLinkagePrimaryAdapterConfig.OnPrimaryItemBindListener primaryItemBindListener,
            DefaultLinkageSecondaryAdapterConfig.OnSecondaryItemBindListener secondaryItemBindListener,
            DefaultLinkageSecondaryAdapterConfig.OnSecondaryHeaderBindListener headerBindListener) {

        mPrimaryItemClickListener = onPrimaryItemClickListener;

        if (mPrimaryAdapter.getConfig() != null) {
            ((DefaultLinkagePrimaryAdapterConfig) mPrimaryAdapter.getConfig())
                    .setListener(primaryItemBindListener);
        }
        if (mSecondaryAdapter.getConfig() != null) {
            ((DefaultLinkageSecondaryAdapterConfig) mSecondaryAdapter.getConfig())
                    .setItemBindListener(secondaryItemBindListener, headerBindListener);
        }
    }

    public void setLayoutHeight(float dp) {
        ViewGroup.LayoutParams lp = mLinkageLayout.getLayoutParams();
        lp.height = dpToPx(getContext(), dp);
        mLinkageLayout.setLayoutParams(lp);
    }

    public boolean isGridMode() {
        return mSecondaryAdapter.isGridMode();
    }

    public void setGridMode(boolean isGridMode) {
        mSecondaryAdapter.setGridMode(isGridMode);
        setLevel2LayoutManager();
        mRvSecondary.requestLayout();
    }

    public boolean isScrollSmoothly() {
        return scrollSmoothly;
    }

    public void setScrollSmoothly(boolean scrollSmoothly) {
        this.scrollSmoothly = scrollSmoothly;
    }

    public interface OnPrimaryItemClickListener {
        void onItemClick(View primaryClickView, String title, int position);
    }
}
