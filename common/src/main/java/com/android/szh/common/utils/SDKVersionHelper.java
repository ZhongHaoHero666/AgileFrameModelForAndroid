package com.android.szh.common.utils;

import android.os.Build;

/**
 * SDK版本工具类
 */
public final class SDKVersionHelper {

    private SDKVersionHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    /**
     * 判断SDK版本是否大于Android 4.4(KitKat,API level 19)
     */
    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 判断SDK版本是否大于Android 5.0(LOLLIPOP,API level 21)
     */
    public static boolean hasLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 判断SDK版本是否大于Android 6.0(Marshmallow,API level 23)
     */
    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 判断SDK版本是否大于Android 7.0(Nougat,API level 24)
     */
    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

}
