package com.android.szh.common.utils;

import android.widget.Toast;

import com.android.szh.common.BaseApplication;

/**
 * Created by sunzhonghao on 2018/4/10.
 * desc:Toast工具类
 */

public class ToastUtil {
    private ToastUtil() {
    }

    private static Toast toast;
    private static Toast tipToast;

    //纯单例模式
    public static void showToast(String str) {
        int duration = Toast.LENGTH_SHORT;
        if (str.length() > 15) {
            duration = Toast.LENGTH_LONG;
        }
        if (toast == null) {
            synchronized (ToastUtil.class) {
                if (toast == null) {
                    toast = Toast.makeText(BaseApplication.getInstance().getApplicationContext(), str, duration);
                }
            }
        } else {
            toast.setText(str); //直接设置文本
        }
        toast.show();
    }

}