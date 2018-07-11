package com.android.szh.common.dialog;

import android.content.Context;

/**
 * 加载对话框提供者
 *
 * @author liyunlong
 * @date 2017/9/8 13:37
 */
public interface IDialogProvider<Dialog extends android.app.Dialog> {

    Context getContext();

    Dialog getDialog();

    Dialog creatDialog();

    void setMessage(CharSequence message);

    void setCancelable(boolean cancel);

    void setCanceledOnTouchOutside(boolean cancel);

    void show();

    void dismiss();

    boolean isShowing();

    boolean cancleAble();

}
