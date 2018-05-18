package com.android.szh.common.abslistview.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;


import com.android.szh.common.abslistview.holder.AbsListViewHolder;
import com.android.szh.common.adapter.IAdapter;
import com.android.szh.common.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link ExpandableListView}适配器
 **Created by sunzhonghao on 2018/5/16.
 * desc:  ExpandableListView 适配器
 */
public abstract class CommonExpandableAdapter<GroupType, DataType> extends BaseExpandableListAdapter implements IAdapter<List<DataType>> {

    /** 上下文 */
    protected Context mContext;
    /** 头部数据源 */
    private final List<GroupType> mGroups = new ArrayList<>();
    /** Item数据源 */
    private final List<List<DataType>> mDatas = new ArrayList<>();
    /** Group是否展开状态 */
    private final SparseBooleanArray mGroupExpandedMap = new SparseBooleanArray();

    public CommonExpandableAdapter(List<List<DataType>> datas) {
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.mDatas.addAll(datas);
    }

    public CommonExpandableAdapter(Map<GroupType, List<DataType>> map) {
        if (map == null) {
            map = new HashMap<>(0);
        }
        this.mGroups.addAll(map.keySet());
        this.mDatas.addAll(map.values());
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
    public List<List<DataType>> getDatas() {
        return mDatas;
    }

    public void refresh(Map<GroupType, List<DataType>> map) {
        this.mGroups.clear();
        this.mDatas.clear();
        if (map != null && !map.isEmpty()) {
            this.mGroups.addAll(map.keySet());
            this.mDatas.addAll(map.values());
        }
        notifyDataSetChanged();
    }

    public void refresh(Collection<GroupType> groups, Collection<ArrayList<DataType>> childs) {
        this.mGroups.clear();
        this.mDatas.clear();
        if (groups != null && !groups.isEmpty()) {
            this.mGroups.addAll(groups);
        }
        if (childs != null && !childs.isEmpty()) {
            this.mDatas.addAll(childs);
        }
        notifyDataSetChanged();
    }

    @Override
    public void refresh(List<List<DataType>> datas) {
        this.mDatas.clear();
        if (datas != null && !datas.isEmpty()) {
            this.mDatas.addAll(datas);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void add(List<DataType> data) {
        this.mDatas.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void add(int position, List<DataType> data) {
        this.mDatas.add(position, data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addAll(List<List<DataType>> datas) {
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
    public void remove(List<DataType> data) {
        this.mDatas.remove(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void removeAll(List<List<DataType>> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        this.mDatas.removeAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public void clear() {
        this.mGroups.clear();
        this.mDatas.clear();
        this.notifyDataSetChanged();
    }

    public List<GroupType> getGroupDatas() {
        return mGroups;
    }

    public List<DataType> getChildDatas(int groupPosition) {
        return mDatas == null ? null : mDatas.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (!isGroupPositionValid(groupPosition)) {
            return 0;
        }
        List<DataType> groupDatas = getChildDatas(groupPosition);
        return groupDatas == null ? 0 : groupDatas.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public GroupType getGroup(int groupPosition) {
        return isGroupPositionValid(groupPosition) ? mGroups.get(groupPosition) : null;
    }

    @Override
    public DataType getChild(int groupPosition, int childPosition) {
        List<DataType> childDatas = getChildDatas(groupPosition);
        return childDatas == null ? null : childDatas.get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        int groupLayoutID = getGroupLayoutID(getGroupType(groupPosition));
        AbsListViewHolder viewHolder = AbsListViewHolder.getViewHolder(convertView, parent, groupLayoutID);
        mGroupExpandedMap.put(groupPosition, isExpanded);
        convertGroup(viewHolder.getViewHolder(), getGroup(groupPosition), groupPosition);
        return viewHolder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        int childLayoutID = getItemLayoutID(getChildType(groupPosition, childPosition));
        AbsListViewHolder viewHolder = AbsListViewHolder.getViewHolder(convertView, parent, childLayoutID);
        convertChild(viewHolder.getViewHolder(), getChild(groupPosition, childPosition), groupPosition, childPosition);
        return viewHolder.getConvertView();
    }

    @Override
    public void convert(ViewHolder holder, List<DataType> data, int position) {

    }

    /**
     * 返回指定Group视图类型对应的视图资源ID
     *
     * @param groupType Group视图类型
     */
    protected int getGroupLayoutID(int groupType) {
        return getGroupLayoutID();
    }

    /**
     * 返回Group视图资源ID
     */
    protected abstract int getGroupLayoutID();

    /**
     * 返回指定Child视图类型对应的视图资源ID
     *
     * @param childType Child视图类型
     */
    @Override
    public int getItemLayoutID(int childType) {
        return getItemLayoutID();
    }

    /**
     * 返回Child视图资源ID
     */
    @Override
    public abstract int getItemLayoutID();

    /**
     * Group视图数据和事件绑定
     *
     * @param holder        ViewHolder对象
     * @param group         Group数据
     * @param groupPosition Group索引
     */
    protected abstract void convertGroup(ViewHolder holder, GroupType group, int groupPosition);

    /**
     * Child视图数据和事件绑定
     *
     * @param holder        ViewHolder对象
     * @param child         Child数据
     * @param groupPosition Group索引
     * @param childPosition Child索引
     */
    protected abstract void convertChild(ViewHolder holder, DataType child, int groupPosition, int childPosition);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return isChildPositionValid(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        mGroupExpandedMap.put(groupPosition, true);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        mGroupExpandedMap.put(groupPosition, false);
    }

    /**
     * 判断指定位置的Group是否展开
     *
     * @param groupPosition Group索引
     */
    public boolean isGroupExpanded(int groupPosition) {
        return mGroupExpandedMap.get(groupPosition);
    }

    /**
     * 检查{@code groupPosition}是否在有效范围的辅助方法
     */
    protected boolean isGroupPositionValid(int groupPosition) {
        return (mGroups != null && groupPosition >= 0 && groupPosition < mGroups.size());
    }

    /**
     * 检查{@code childPosition}是否在有效范围的辅助方法
     */
    protected boolean isChildPositionValid(int groupPosition, int childPosition) {
        return (isGroupPositionValid(groupPosition) && childPosition >= 0);
    }

}
