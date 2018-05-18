package com.android.szh.common.recycleview.wrapper;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.szh.common.recycleview.holder.RecyclerViewHolder;


/**
 * 可以添加/移除Header和Footer的适配器包装类
 *
 * Created by sunzhonghao
 * @date 2017/8/2 17:36
 */
public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int itemTypeHeaderOrFooter = 0;
    private SparseArrayCompat<View> mHeaderViews;
    private SparseArrayCompat<View> mFooterViews;
    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(@NonNull RecyclerView.Adapter innerAdapter) {
        this(innerAdapter, new SparseArrayCompat<View>(), new SparseArrayCompat<View>());
    }

    public HeaderAndFooterWrapper(@NonNull RecyclerView.Adapter innerAdapter, SparseArrayCompat<View> headerViews, SparseArrayCompat<View> footViews) {
        this.mHeaderViews = headerViews;
        this.mFooterViews = footViews;
        this.mInnerAdapter = innerAdapter;
    }

    private int getInnerItemCount() {
        return mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemCount() {
        return getHeaderViewsCount() + getInnerItemCount() + getFooterViewsCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPosition(position)) {
            return mFooterViews.keyAt(position - getHeaderViewsCount() - getInnerItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeaderViewsCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return RecyclerViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return RecyclerViewHolder.createViewHolder(parent.getContext(), mFooterViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPosition(position)) {
            return;
        }
        if (isFooterViewPosition(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewsCount());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        WrapperHelper.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperHelper.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null || mFooterViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {
            WrapperHelper.setFullSpan(holder);
        }
    }

    /**
     * 添加HeaderView
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(generateViewType(), view);
        notifyItemInserted(mHeaderViews.size() - 1);
    }

    /**
     * 移除HeaderView
     */
    public void removeHeaderView(View view) {
        int indexToRemove = mHeaderViews.indexOfValue(view);
        if (indexToRemove != -1) {
            mHeaderViews.removeAt(indexToRemove);
            notifyItemRemoved(indexToRemove);
        }
    }

    /**
     * 添加FooterView
     */
    public void addFooterView(View view) {
        mFooterViews.put(generateViewType(), view);
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 移除FooterView
     */
    public void removeFooterView(View view) {
        int indexToRemove = mFooterViews.indexOfValue(view);
        if (indexToRemove != -1) {
            mFooterViews.removeAt(indexToRemove);
            notifyItemRemoved(getHeaderViewsCount() + getInnerItemCount() + indexToRemove);
        }
    }

    /**
     * 返回HeaderView的数量
     */
    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    /**
     * 返回FooterView的数量
     */
    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    /**
     * 判断指定位置的ItemView是否为HeaderView
     *
     * @param position 指定位置的索引
     */
    private boolean isHeaderViewPosition(int position) {
        return position < getHeaderViewsCount();
    }

    /**
     * 判断指定位置的ItemView是否为FooterView
     *
     * @param position 指定位置的索引
     */
    private boolean isFooterViewPosition(int position) {
        return position >= getHeaderViewsCount() + getInnerItemCount();
    }

    /**
     * 生成ViewType
     */
    private int generateViewType() {
        return ++itemTypeHeaderOrFooter;
    }

}
