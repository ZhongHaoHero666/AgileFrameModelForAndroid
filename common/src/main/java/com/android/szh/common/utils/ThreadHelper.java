package com.android.szh.common.utils;

import android.os.Looper;

/**
 * 线程辅助类
 */
public class ThreadHelper {

    private ThreadHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    /**
     * 判断是否在主线程
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 判断是否在后台线程
     */
    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

}
