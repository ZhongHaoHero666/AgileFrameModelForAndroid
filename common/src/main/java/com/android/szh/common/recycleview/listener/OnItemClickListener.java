package com.android.szh.common.recycleview.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * {@link RecyclerView}的Item点击事件监听
 *
 * Created by sunzhonghao
 * @date 2017/8/1 17:00
 */
public interface OnItemClickListener {

    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
}
