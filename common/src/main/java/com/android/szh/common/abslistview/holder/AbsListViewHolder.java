package com.android.szh.common.abslistview.holder;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.android.szh.common.adapter.ViewHolder;


/**
 * {@link AbsListView}的ViewHolder实现类
 * <p>
 * Created by sunzhonghao on 2018/5/16.
 * desc: AbsListView 的ViewHolder实现类
 */
public final class AbsListViewHolder {

    private final View mConvertView;
    private final ViewHolder mHolder;

    private AbsListViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
        this.mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
        this.mHolder = ViewHolder.creat(mConvertView);
    }

    /**
     * 返回一个ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static AbsListViewHolder getViewHolder(View convertView, ViewGroup parent, @LayoutRes int layoutId) {
        if (convertView == null) {
            return new AbsListViewHolder(parent, layoutId);
        } else {
            return (AbsListViewHolder) convertView.getTag();
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    public ViewHolder getViewHolder() {
        return mHolder;
    }
}
