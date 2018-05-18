package com.android.szh.common.abslistview.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.android.szh.common.abslistview.holder.AbsListViewHolder;
import com.android.szh.common.adapter.IAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link AbsListView}适配器
 * Created by sunzhonghao on 2018/5/16.
 * desc:  AbsListView 适配器
 */
public abstract class CommonAdapter<DataType> extends BaseAdapter implements IAdapter<DataType> {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据源
     */
    private final List<DataType> mDatas = new ArrayList<>();

    public CommonAdapter(Collection<DataType> datas) {
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.mDatas.addAll(datas);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public Resources getResources() {
        return mContext.getResources();
    }

    @Override
    public List<DataType> getDatas() {
        return mDatas;
    }

    @Override
    public void refresh(List<DataType> datas) {
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public void add(DataType data) {
        this.mDatas.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void add(int position, DataType data) {
        this.mDatas.add(position, data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addAll(List<DataType> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        this.mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public void remove(int position) {
        this.mDatas.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public void remove(DataType data) {
        this.mDatas.remove(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void removeAll(List<DataType> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        this.mDatas.removeAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public void clear() {
        this.mDatas.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public DataType getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        int itemLayoutID = getItemLayoutID(getItemViewType(position));
        AbsListViewHolder viewHolder = AbsListViewHolder.getViewHolder(convertView, parent, itemLayoutID);
        convert(viewHolder.getViewHolder(), getItem(position), position);
        return viewHolder.getConvertView();
    }

    @Override
    public int getItemLayoutID(int viewType) {
        return getItemLayoutID();
    }

}
