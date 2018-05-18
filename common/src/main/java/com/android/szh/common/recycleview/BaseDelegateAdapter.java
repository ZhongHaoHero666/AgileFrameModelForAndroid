package com.android.szh.common.recycleview;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.android.szh.common.adapter.ViewHolder;
import com.android.szh.common.recycleview.holder.RecyclerViewHolder;

/**
 * vlayout适配器基类
 *
 * Created by sunzhonghao
 * @date 2018/1/22 17:05
 */
public abstract class BaseDelegateAdapter<DataType> extends DelegateAdapter.Adapter<RecyclerViewHolder> {

    private int mItemCount = -1;
    private Context mContext;
    private DataType mData;
    private LayoutHelper mLayoutHelper;
    private int mViewTypeItem = -1;

    public BaseDelegateAdapter(LayoutHelper layoutHelper, int itemCount, DataType data, int viewTypeItem) {
        this.mData = data;
        this.mItemCount = itemCount;
        this.mLayoutHelper = layoutHelper;
        this.mViewTypeItem = viewTypeItem;
    }

    public Context getContext() {
        return mContext;
    }

    public DataType getData() {
        return mData;
    }

    public void updateData(DataType data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * Item数量
     */
    @Override
    public int getItemCount() {
        return mItemCount;
    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        return mViewTypeItem;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == mViewTypeItem) {
            return RecyclerViewHolder.createViewHolder(mContext, parent, getItemLayoutID(viewType));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (mContext == null) {
            mContext = holder.getContext();
        }
        convert(holder.getViewHolder(), position);
    }

    protected abstract int getItemLayoutID(int viewType);

    protected abstract void convert(ViewHolder holder, int position);

}
