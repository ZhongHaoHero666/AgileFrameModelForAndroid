package com.android.szh.common.adapter;


import com.android.szh.common.recycleview.listener.OnItemClickListener;
import com.android.szh.common.recycleview.listener.OnItemLongClickListener;

/**
 * RecyclerView的Adapter数据操作接口
 *
 * Created by sunzhonghao
 * @date 2017/8/2 14:06
 */
public interface IRecycleViewAdapter<DataType> extends IAdapter<DataType> {

    /**
     * 判断适配器的Item数量是否为0
     */
    boolean isEmpty();

    /**
     * 是否适配器中的所有项都是Enabled状态(即selectable和clickable)
     */
    boolean areAllItemsEnabled();

    /**
     * 指定位置的项是否是Enabled状态(即selectable和clickable)
     *
     * @param position 指定位置的索引
     */
    boolean isEnabled(int position);

    /**
     * 设置RecycleView的Item点击事件监听
     *
     * @param listener Item点击事件监听
     */
    void setOnItemClickListener(OnItemClickListener listener);

    /**
     * 设置RecycleView的Item长点击事件监听
     *
     * @param listener Item长点击事件监听
     */
    void setOnItemLongClickListener(OnItemLongClickListener listener);

    /**
     * 返回指定位置关联的数据
     *
     * @param position 指定位置的索引
     */
    DataType getItem(int position);
}
