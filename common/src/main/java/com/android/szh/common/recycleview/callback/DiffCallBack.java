package com.android.szh.common.recycleview.callback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * 比较新旧数据集规则抽象类
 * <ul>
 * <li>介绍
 * <ul>
 * <li>{@link DiffCallBack}继承{@link DiffUtil.Callback}
 * <li>{@link DiffCallBack}是一个抽象类
 * <li>可以重写该类的方法实现数据比较逻辑
 * </ul>
 * </li>
 * <li>使用步骤
 * <ol>
 * <li>继承{@link DiffCallBack}并在各个抽象方法中实现数据比较逻辑和保存两个数据项发生变化的属性的有效负载对象。
 * <li>调用{@link DiffUtil#calculateDiff(DiffUtil.Callback, boolean)}计算更新并得到{@link DiffUtil.DiffResult}对象。第二个参数可省，意为是否探测数据的移动，是否关闭需要根据数据集情况来权衡。当数据集很大时，此操作可能耗时较长，需要异步计算。
 * <li>在UI线程中使用得到的{@link DiffUtil.DiffResult}对象调用{@link DiffUtil.DiffResult#dispatchUpdatesTo(RecyclerView.Adapter)}将更新事件分派到给定的适配器。
 * <li>重写{@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int, List)}处理更新的数据，第三个参数中存储了更新的数据。
 * </ol>
 * </li>
 * <ul/>
 *
 * Created by sunzhonghao
 * @date 2017/8/3 14:34
 */
public abstract class DiffCallBack<DataType> extends DiffUtil.Callback {

    /**
     * 旧数据集
     */
    private List<DataType> mOldDatas;
    /**
     * 新数据集
     */
    private List<DataType> mNewDatas;

    public DiffCallBack(List<DataType> oldDatas, List<DataType> newDatas) {
        this.mOldDatas = oldDatas;
        this.mNewDatas = newDatas;
    }

    /**
     * 返回旧数据集中数据项的数量
     */
    @Override
    public int getOldListSize() {
        return mOldDatas == null ? 0 : mOldDatas.size();
    }

    /**
     * 返回新数据集中数据项的数量
     */
    @Override
    public int getNewListSize() {
        return mNewDatas == null ? 0 : mNewDatas.size();
    }

    /**
     * 返回旧数据集中指定位置的数据项
     */
    public DataType getOldItem(int position) {
        return mOldDatas == null ? null : mOldDatas.get(position);
    }

    /**
     * 返回新数据集中指定位置的数据项
     */
    public DataType getNewItem(int position) {
        return mNewDatas == null ? null : mNewDatas.get(position);
    }

    /**
     * 由{@link DiffUtil}调用来判断两个对象是否代表相同的数据项
     *
     * @param oldItemPosition 数据项在旧数据集中的位置
     * @param newItemPosition 数据项在新数据集中的位置
     * @return 如果两个数据项表示相同对象时返回 {@code true} ，反之返回 {@code false}
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        DataType oldItem = getOldItem(oldItemPosition);
        DataType newItem = getNewItem(newItemPosition);
        if (oldItem == null && newItem == null) {
            return true;
        } else if (oldItem == null || newItem == null) {
            return false;
        }
        return areItemsTheSame(oldItem, newItem);
    }

    /**
     * 由{@link DiffUtil}调用来检查两个数据项是否具有相同的数据({@link DiffUtil}用此信息来检测当前数据项的内容是否发生变化)
     * <ul>
     * <li>{@link DiffUtil}使用此方法替代{@link Object#equals(Object)}判断是否相等，所以可以根据UI改变它的返回值
     * <li>{@link DiffUtil}和{@link RecyclerView.Adapter}配合使用，需要返回数据项的视觉表现是否相同
     * <li>此方法仅仅在{@link #areItemsTheSame(int, int)}返回{@code true}时才会调用
     * <ul/>
     *
     * @param oldItemPosition 数据项在旧数据集中的位置
     * @param newItemPosition 数据项在新数据集中的位置
     * @return 如果两个数据项的内容相同时返回 {@code true} ，反之返回 {@code false}
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        DataType oldItem = getOldItem(oldItemPosition);
        DataType newItem = getNewItem(newItemPosition);
        if (oldItem == null && newItem == null) {
            return true;
        } else if (oldItem == null || newItem == null) {
            return false;
        }
        return areContentsTheSame(oldItem, newItem);
    }

    /**
     * 当{@link #areItemsTheSame(int, int)}返回true且{@link #areContentsTheSame(int, int)}返回false时
     * (即两个对象代表相同的数据项但数据项的内容改变了)，{@link DiffUtil}会调用该方法得到关于改变的负载
     * <ul>
     * <li>{@link DiffUtil}和{@link RecyclerView.Adapter}配合使用，可以返回数据项中改变的属性
     * <li>{@link RecyclerView.ItemAnimator ItemAnimator}可以通过这些信息执行正确的动画
     * <li>默认实现返回 {@code null}
     * <ul/>
     *
     * @param oldItemPosition 数据项在旧数据集中的位置
     * @param newItemPosition 数据项在新数据集中的位置
     * @return 返回表示两个数据项之间变化的有效负载对象
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        DataType oldItem = getOldItem(oldItemPosition);
        DataType newItem = getNewItem(newItemPosition);
        if (oldItem == null && newItem == null) {
            return null;
        }
        Bundle bundle = getChangePayload(oldItem, newItem);
        if (bundle != null && !bundle.isEmpty()) {
            return bundle;
        }
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    /**
     * 判断两个对象是否是相同的数据项
     *
     * @param oldItem 旧数据项
     * @param newItem 新数据项
     * @return 如果两个数据项表示相同对象时返回 {@code true} ，反之返回 {@code false}
     * @see #areItemsTheSame(int, int)
     */
    protected abstract boolean areItemsTheSame(DataType oldItem, DataType newItem);

    /**
     * 检查两个数据项是否具有相同的数据
     *
     * @param oldItem 旧数据项
     * @param newItem 新数据项
     * @return 如果两个数据项的内容相同时返回 {@code true} ，反之返回 {@code false}
     * @see #areContentsTheSame(int, int)
     */
    protected abstract boolean areContentsTheSame(DataType oldItem, DataType newItem);

    /**
     * 返回保存两个数据项发生变化的属性的{@link Bundle}对象
     * <ul>
     * <li>将两个数据项发生变化的属性以键值对的形式保存到{@link Bundle}对象中
     * <li>{@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int, List)}可以处理数据项变化的有效负载对象
     * <ul/>
     *
     * @param oldItem 旧数据项
     * @param newItem 新数据项
     * @return 保存两个数据项发生变化的属性的 {@link Bundle} 对象
     * @see #getChangePayload(int, int)
     */
    protected abstract Bundle getChangePayload(DataType oldItem, DataType newItem);
}
