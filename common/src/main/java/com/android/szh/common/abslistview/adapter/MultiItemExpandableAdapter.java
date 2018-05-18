package com.android.szh.common.abslistview.adapter;

import android.widget.ExpandableListView;


import com.android.szh.common.adapter.ViewHolder;
import com.android.szh.common.delegate.IAdapterDelegate;
import com.android.szh.common.delegate.ItemViewDelegate;
import com.android.szh.common.delegate.ItemViewDelegateManager;

import java.util.List;
import java.util.Map;

/**
 * {@link ExpandableListView}适配器(用于多种ItemView类型)
 * Created by sunzhonghao on 2018/5/16.
 * desc:  ExpandableListView 适配器
 */
public abstract class MultiItemExpandableAdapter<GroupType, DataType> extends CommonExpandableAdapter<GroupType, DataType> implements IAdapterDelegate<DataType> {

    private ItemViewDelegateManager<DataType> mItemViewDelegateManager;

    public MultiItemExpandableAdapter(List<List<DataType>> datas) {
        super(datas);
        this.mItemViewDelegateManager = new ItemViewDelegateManager<>();
    }

    public MultiItemExpandableAdapter(Map<GroupType, List<DataType>> map) {
        super(map);
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
    public int getChildType(int groupPosition, int childPosition) {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewType(getChild(groupPosition, childPosition), childPosition);
        }
        return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public int getChildTypeCount() {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewDelegateCount();
        }
        return super.getChildTypeCount();
    }

    @Override
    public int getItemLayoutID(int childType) {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewLayoutId(childType);
        }
        return super.getItemLayoutID(childType);
    }

    @Override
    public int getItemLayoutID() {
        return 0;
    }

    @Override
    protected void convertChild(ViewHolder holder, DataType child, int groupPosition, int childPosition) {
        mItemViewDelegateManager.convert(holder, child, childPosition);
    }

}
