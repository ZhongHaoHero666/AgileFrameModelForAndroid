package com.android.szh.common.dialog;

import android.content.Context;

/**
 * 加载对话框提供者
 *
 * @author sunzhonghao
 * @date 2018/7/11
 */
public abstract class BaseDialogProvider<Dialog extends android.app.Dialog> implements IDialogProvider<Dialog> {

    private Context mContext;
    private Dialog mDialog;
    private boolean cancle = true;

    public BaseDialogProvider(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public Dialog getDialog() {
        if (mDialog == null) {
            mDialog = creatDialog();
        }
        return mDialog;
    }

    @Override
    public void setCancelable(boolean cancel) {
        getDialog().setCancelable(cancel);
        this.cancle = cancel;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        getDialog().setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
        getDialog().show();
    }

    @Override
    public void dismiss() {
        getDialog().dismiss();
    }

    @Override
    public boolean isShowing() {
        return getDialog().isShowing();
    }

    @Override
    public boolean cancleAble() {
        return cancle;
    }
}
