package com.android.szh.common.recycleview.callback;

import android.support.v7.widget.RecyclerView;

/**
 * {@link RecyclerView}的Item触摸回调
 *
 * Created by sunzhonghao
 * @date 2018/1/24 18:44
 */
public interface OnItemTouchCallback {

    /**
     * 当某个Item被滑动删除的时候
     *
     * @param position 滑动删除的位置
     */
    void onSwipe(int position);

    /**
     * 当两个Item位置互换的时候被回调
     *
     * @param fromPosition 拖拽的item的position
     * @param toPosition   目的地的Item的position
     */
    boolean onMove(int fromPosition, int toPosition);
}
