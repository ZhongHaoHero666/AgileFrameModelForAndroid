package com.android.szh.common.recycleview.callback;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * {@link ItemTouchHelper.Callback}的实现类
 *
 * Created by sunzhonghao
 * @date 2018/1/24 18:52
 */
public class ItemTouchHelpCallback extends ItemTouchHelper.Callback {

    /** 是否启用拖拽功能 */
    private boolean dragEnable = false;
    /** 是否启用滑动功能 */
    private boolean swipeEnable = false;
    /** Item操作的回调 */
    private OnItemTouchCallback onItemTouchCallback;

    public ItemTouchHelpCallback(OnItemTouchCallback onItemTouchCallback) {
        this.onItemTouchCallback = onItemTouchCallback;
    }

    /**
     * 设置Item操作的回调，去更新UI和数据源
     */
    public void setOnItemTouchCallback(OnItemTouchCallback onItemTouchCallback) {
        this.onItemTouchCallback = onItemTouchCallback;
    }

    /**
     * 设置是否可以被拖拽
     */
    public void setDragEnable(boolean dragEnable) {
        this.dragEnable = dragEnable;
    }

    /**
     * 设置是否可以被滑动
     */
    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    /**
     * 当Item被长按的时候是否可以被拖拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return dragEnable;
    }

    /**
     * Item是否可以被滑动(H：左右滑动，V：上下滑动)
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return swipeEnable;
    }

    /**
     * 当用户拖拽或者滑动Item的时候需要我们告诉系统滑动或者拖拽的方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {// GridLayoutManager
            // flag如果值是0，相当于这个功能被关闭
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            // create make
            return makeMovementFlags(dragFlag, swipeFlag);
        } else if (layoutManager instanceof LinearLayoutManager) {// linearLayoutManager
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            int dragFlag = 0;
            int swipeFlag = 0;
            // 为了方便理解，相当于分为横着的ListView和竖着的ListView
            if (orientation == LinearLayoutManager.HORIZONTAL) {// 如果是横向的布局
                swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (orientation == LinearLayoutManager.VERTICAL) {// 如果是竖向的布局，相当于ListView
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    /**
     * 当Item被拖拽的时候被回调
     *
     * @param recyclerView     recyclerView
     * @param srcViewHolder    拖拽的ViewHolder
     * @param targetViewHolder 目的地的viewHolder
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder srcViewHolder, RecyclerView.ViewHolder targetViewHolder) {
        if (onItemTouchCallback != null) {
            return onItemTouchCallback.onMove(srcViewHolder.getAdapterPosition(), targetViewHolder.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (onItemTouchCallback != null) {
            onItemTouchCallback.onSwipe(viewHolder.getAdapterPosition());
        }
    }

}
