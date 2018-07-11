package com.android.szh.common.mvp;

import android.content.Context;
import android.widget.Toast;

import com.android.szh.common.dialog.IDialogProvider;
import com.android.szh.common.dialog.LoadingDialogProvider;
import com.android.szh.common.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author sunzhonghao
 * @date 2018/7/11
 * desc:View 辅助类 可以展示不同状态的界面，dialog 等
 */
public class ViewHelper implements IViewHepler {


    private Context context;
    private Toast toast;
    private IDialogProvider dialogProvider;
    private SmartRefreshLayout refreshLayout;
//    private StatusLayoutManager statusLayoutManager;

    public ViewHelper(Context context) {
        this(context, new LoadingDialogProvider(context));
    }

    public ViewHelper(Context context, IDialogProvider dialogProvider) {
        this.context = context;
        this.dialogProvider = dialogProvider;
    }

    public Context getContext() {
        return context;
    }


    @Override
    public void showToast(CharSequence message) {
        ToastUtil.showToast(String.valueOf(message));
    }

    @Override
    public void showLoadingDialog() {
        showLoadingDialog(true);
    }

    @Override
    public void showLoadingDialog(boolean cancel) {
        showLoadingDialog(null, cancel);
    }

    @Override
    public void showLoadingDialog(CharSequence message, boolean cancel) {
        if (message != null) {
            dialogProvider.setMessage(message);
        }
        dialogProvider.setCancelable(cancel);
        dialogProvider.setCanceledOnTouchOutside(false);
        dialogProvider.show();
    }

    @Override
    public void hideLoadingDialog() {
        dialogProvider.dismiss();
    }

    @Override
    public void showContentLayout() {

    }

    @Override
    public void showEmptyLayout() {

    }

    @Override
    public void showErrorLayout() {

    }

    @Override
    public void showNetworkErrorLayout() {

    }

    @Override
    public void showNetworkPoorLayout() {

    }

    @Override
    public void restoreLayout() {

    }
}
