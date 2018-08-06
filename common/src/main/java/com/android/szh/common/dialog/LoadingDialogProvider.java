package com.android.szh.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.android.szh.common.R;


/**
 * 加载对话框提供者
 *
 * @author sunzhonghao
 * @date 2018/7/11
 */
public class LoadingDialogProvider extends BaseDialogProvider<Dialog> {

    public LoadingDialogProvider(Context context) {
        super(context);
    }

    @Override
    public Dialog creatDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.CustomDialogStyle);
        //设定加载中的View,此处使用的是系统的，开发者可以自定义View 实现酷炫的动画效果
        dialog.setContentView(new ProgressBar(getContext()));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void setMessage(CharSequence message) {
    }

    @Override
    public boolean cancleAble() {
        return false;
    }
}
