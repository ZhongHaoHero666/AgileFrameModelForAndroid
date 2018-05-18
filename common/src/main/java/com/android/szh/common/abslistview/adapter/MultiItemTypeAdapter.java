package com.android.szh.common.abslistview.adapter;

import android.widget.AbsListView;


import com.android.szh.common.adapter.ViewHolder;
import com.android.szh.common.delegate.IAdapterDelegate;
import com.android.szh.common.delegate.ItemViewDelegate;
import com.android.szh.common.delegate.ItemViewDelegateManager;

import java.util.Collection;

/**
 * {@link AbsListView}适配器(用于多种ItemView类型)
 * Created by sunzhonghao on 2018/5/16.
 * desc:  AbsListView 适配器
 *
 * @param <DataType> 数据类型的泛型
 */
public class MultiItemTypeAdapter<DataType> extends CommonAdapter<DataType> implements IAdapterDelegate<DataType> {

    private ItemViewDelegateManager<DataType> mItemViewDelegateManager;

    public MultiItemTypeAdapter(Collection<DataType> datas) {
        super(datas);
        this.mItemViewDelegateManager = new ItemViewDelegateManager<>();
    }

    @Override
    public void addItemViewDelegate(ItemViewDelegate<DataType> delegate) {
        mItemViewDelegateManager.addDelegate(delegate);
    }

    @Override
    public void addItemViewDelegate(int viewType, ItemViewDelegate<DataType> delegate) {
        mItemViewDelegateManager.addDelegate(viewType, delegate);
    }

    @Override
    public ItemViewDelegateManager<DataType> removeDelegate(int itemType) {
        return mItemViewDelegateManager.removeDelegate(itemType);
    }

    @Override
    public ItemViewDelegateManager<DataType> removeDelegate(ItemViewDelegate<DataType> delegate) {
        return mItemViewDelegateManager.removeDelegate(delegate);
    }

    @Override
    public boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewType(getItem(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewDelegateCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemLayoutID(int viewType) {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewLayoutId(viewType);
        }
        return getItemLayoutID();
    }

    @Override
    public int getItemLayoutID() {
        return 0;
    }

    @Override
    public void convert(ViewHolder holder, DataType data, int position) {
        mItemViewDelegateManager.convert(holder, data, position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }
}
