package com.android.szh.common.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.android.szh.common.utils.WindowHelper;


/**
 * PopupWindow基类
 */
public abstract class BasePopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private Activity activity;

    public BasePopupWindow(Activity activity) {
        super(activity);
        this.activity = activity;
        initPopWindow();
        setContentView(inflaterContentView());// 设置PopupWindow的View
    }

    private View inflaterContentView() {
        int contentLayoutId = getContentLayoutId();
        if (contentLayoutId == 0) {
            return null;
        }
        return LayoutInflater.from(activity).inflate(contentLayoutId, null, false);
    }

    /**
     * 初始化PopupWindow
     */
    protected void initPopWindow() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);         // 设置PopupWindow弹出窗体的宽
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);        // 设置PopupWindow弹出窗体的高
//        this.setAnimationStyle(R.style.BottomUpStyle);            // 设置PopupWindow弹出窗体动画效果
        this.setFocusable(true);                                    // 设置PopupWindow可获得焦点
        this.setTouchable(true);                                    // 设置PopupWindow可触摸
        this.setOutsideTouchable(true);                             // 设置非PopupWindow区域可触摸
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));  // 设置PopupWindow的背景
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 防止虚拟软键盘被弹出菜单遮住
        this.setOnDismissListener(this);
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        initContentView(contentView);
    }

    public Activity getActivity() {
        return activity;
    }

    public boolean isUpdateWindowDim() {
        return true;
    }

    @Override
    public void showAsDropDown(View anchor) {
        onShow();
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        onShow();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        onShow();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        onShow();
        super.showAtLocation(parent, gravity, x, y);
    }

    public void onShow() {
        if (isUpdateWindowDim()) {
            WindowHelper.updateWindowDim(activity);
        }
    }

    @Override
    public void onDismiss() {
        if (isUpdateWindowDim()) {
            WindowHelper.updateWindowLight(activity);
        }
    }

    /**
     * 加载页面布局
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化ContentView
     */
    protected abstract void initContentView(View rootView);

}
