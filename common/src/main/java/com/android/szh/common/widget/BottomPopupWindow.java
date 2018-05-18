package com.android.szh.common.widget;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.android.szh.common.R;
import com.android.szh.common.abslistview.adapter.CommonAdapter;
import com.android.szh.common.adapter.ViewHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 自定义底部弹窗
 */
public class BottomPopupWindow extends BasePopupWindow {

    private TextView tvTitle;
    private LinearLayout llContainer;
    private ListView mList;
    private View vDivider;
    private Button btnCancle;

    public BottomPopupWindow(Activity activity) {
        super(activity);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_pop_bottom_popupwindow;
    }

    @Override
    protected void initPopWindow() {
        super.initPopWindow();
        this.setAnimationStyle(R.style.BottomUpStyle); //设置PopupWindow弹出窗体动画效果
    }

    @Override
    protected void initContentView(View rootView) {
        tvTitle = (TextView) rootView.findViewById(R.id.bottom_pop_title);
        vDivider = rootView.findViewById(R.id.bottom_pop_title_divider);
        llContainer = (LinearLayout) rootView.findViewById(R.id.bottom_pop_container);
        mList = (ListView) rootView.findViewById(R.id.bottom_pop_list);
        btnCancle = (Button) rootView.findViewById(R.id.bottom_pop_cancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * 设置标题
     */
    public BottomPopupWindow setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
            vDivider.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置标题字体颜色
     */
    public BottomPopupWindow setTitleColor(@ColorInt int color) {
        if (color > 0) {
            tvTitle.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置操作列表
     */
    public BottomPopupWindow setListAction(@Nullable OnActionItemClickListener listener, CharSequence... actionNames) {
        if (actionNames != null && actionNames.length > 0) {
            List<CharSequence> list = Arrays.asList(actionNames);
            return setListAction(listener, list);
        }
        return this;
    }

    /**
     * 设置操作列表
     */
    public BottomPopupWindow setListAction(@Nullable final OnActionItemClickListener listener, final List<CharSequence> list) {
        if (list != null && list.size() > 0) {
            mList.setVisibility(View.VISIBLE);
            ActionAdapter adapter = new ActionAdapter(list);
            mList.setAdapter(adapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dismiss();
                    if (listener != null) {
                        listener.onActionItemClickListener(list.get(position), position);
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置取消按钮监听
     */
    public BottomPopupWindow setListAction(@Nullable final OnCancelClickListener listener) {
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancelClickListener();
                }
                dismiss();
            }
        });
        return this;
    }

    /**
     * 设置自定义View
     */
    public BottomPopupWindow setCustomView(View view) {
        if (view != null) {
            llContainer.removeAllViews();
            llContainer.addView(view);
        }
        return this;
    }


    public void show() {
        showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private static class ActionAdapter extends CommonAdapter<CharSequence> {

        private ActionAdapter(Collection<CharSequence> datas) {
            super(datas);
        }

        @Override
        public int getItemLayoutID() {
            return R.layout.layout_item_bottom_pop;
        }

        @Override
        public void convert(ViewHolder holder, CharSequence data, int position) {
            holder.setText(R.id.bottom_pop_item_action, data);
        }

    }

    public interface OnActionItemClickListener {
        void onActionItemClickListener(CharSequence action, int position);
    }

    public interface OnCancelClickListener {
        void onCancelClickListener();
    }

}
