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
