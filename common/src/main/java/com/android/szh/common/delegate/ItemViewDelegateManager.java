package com.android.szh.common.delegate;

import android.support.v4.util.SparseArrayCompat;

import com.android.szh.common.adapter.ViewHolder;


/**
 * {@link ItemViewDelegate}管理器
 *
 * @param <DataType> 数据类型的泛型
 * Created by sunzhonghao
 * @date 2017/8/1 19:11
 */
public class ItemViewDelegateManager<DataType> {

    private SparseArrayCompat<ItemViewDelegate<DataType>> delegates = new SparseArrayCompat<>();

    /**
     * 添加{@link ItemViewDelegate}
     *
     * @param delegate {@link ItemViewDelegate}对象
     */
    public ItemViewDelegateManager<DataType> addDelegate(ItemViewDelegate<DataType> delegate) {
        int viewType = delegates.size();
        if (delegate != null) {
            delegates.put(viewType, delegate);
        }
        return this;
    }

    /**
     * 添加{@link ItemViewDelegate}
     *
     * @param viewType ItemView的类型
     * @param delegate {@link ItemViewDelegate}对象
     */
    public ItemViewDelegateManager<DataType> addDelegate(int viewType, ItemViewDelegate<DataType> delegate) {
        if (delegates.get(viewType) != null) {
            throw new IllegalArgumentException("An ItemViewDelegate is already registered for the viewType = " + viewType + ". Already registered ItemViewDelegate is " + delegates.get(viewType) + ".");
        }
        delegates.put(viewType, delegate);
        return this;
    }

    /**
     * 删除{@link ItemViewDelegate}
     *
     * @param itemType ItemView的类型(SparseArrayCompat的Key)
     * @see SparseArrayCompat
     */
    public ItemViewDelegateManager<DataType> removeDelegate(int itemType) {
        int indexToRemove = delegates.indexOfKey(itemType);
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    /**
     * 删除{@link ItemViewDelegate}
     *
     * @param delegate {@link ItemViewDelegate}对象(SparseArrayCompat的Value)
     * @see SparseArrayCompat
     */
    public ItemViewDelegateManager<DataType> removeDelegate(ItemViewDelegate<DataType> delegate) {
        if (delegate == null) {
            throw new NullPointerException("ItemViewDelegate may not be null.");
        }
        int indexToRemove = delegates.indexOfValue(delegate);
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    /**
     * 返回ItemView的类型
     *
     * @param data     数据
     * @param position 位置索引
     */
    public int getItemViewType(DataType data, int position) {
        int delegatesCount = delegates.size();
        for (int index = 0; index < delegatesCount; index++) {
            ItemViewDelegate<DataType> delegate = delegates.valueAt(index);
            if (delegate.isForViewType(data, position)) {
                return delegates.keyAt(index);
            }
        }
        throw new IllegalArgumentException("No ItemViewDelegate added that matches position=" + position + " in data source.");
    }

    /**
     * 数据和事件绑定
     *
     * @param holder   {@link ViewHolder}对象
     * @param data     数据
     * @param position 位置索引
     */
    public void convert(ViewHolder holder, DataType data, int position) {
        int delegatesCount = delegates.size();
        for (int index = 0; index < delegatesCount; index++) {
            ItemViewDelegate<DataType> delegate = delegates.valueAt(index);
            if (delegate.isForViewType(data, position)) {
                delegate.convert(holder, data, position);
                return;
            }
        }
        throw new IllegalArgumentException("No ItemViewDelegate added that matches position=" + position + " in data source.");
    }

    /**
     * 返回Item布局资源ID
     *
     * @param viewType ItemView的类型
     */
    public int getItemViewLayoutId(int viewType) {
        return delegates.get(viewType).getItemLayoutID();
    }

    /**
     * 返回{@link ItemViewDelegate}的数量
     */
    public int getItemViewDelegateCount() {
        return delegates.size();
    }

}
