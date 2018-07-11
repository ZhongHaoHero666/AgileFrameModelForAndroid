package com.android.szh.common.dialog;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * {@link ProgressDialog}提供者
 *
 * @author sunzhonghao
 * @date 2018/7/11
 */
class ProgressDialogProvider extends BaseDialogProvider<ProgressDialog> {

    public ProgressDialogProvider(Context context) {
        super(context);
    }

    @Override
    public ProgressDialog creatDialog() {
        return new ProgressDialog(getContext());
    }

    @Override
    public void setMessage(CharSequence message) {
        getDialog().setMessage(message);
    }

}
