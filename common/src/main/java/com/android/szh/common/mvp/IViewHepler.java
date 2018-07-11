package com.android.szh.common.mvp;

import android.support.annotation.UiThread;

/**
 * @author sunzhonghao
 * @date 2018/7/11
 * desc:MVP 中view 的辅助工具接口
 */
public interface IViewHepler {

    /**
     * 显示提示信息
     *
     * @param message 提示信息
     */
    void showToast(CharSequence message);

    /**
     * 显示加载对话框
     */
    @UiThread
    void showLoadingDialog();

    /**
     * 显示加载对话框
     *
     * @param cancel 是否可取消
     */
    @UiThread
    void showLoadingDialog(boolean cancel);

    /**
     * 显示加载对话框
     *
     * @param message 提示信息
     * @param cancel  是否可取消
     */
    @UiThread
    void showLoadingDialog(CharSequence message, boolean cancel);

    /**
     * 隐藏加载对话框
     */
    @UiThread
    void hideLoadingDialog();

    /**
     * 显示内容View
     */
    @UiThread
    void showContentLayout();

    /**
     * 显示无数据View
     */
    @UiThread
    void showEmptyLayout();

    /**
     * 显示加载失败View
     */
    @UiThread
    void showErrorLayout();

    /**
     * 显示无网络View
     */
    @UiThread
    void showNetworkErrorLayout();

    /**
     * 显示网络不佳View
     */
    @UiThread
    void showNetworkPoorLayout();

    /**
     * 恢复显示有数据View
     */
    @UiThread
    void restoreLayout();
}
