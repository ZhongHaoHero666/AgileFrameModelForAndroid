package com.android.szh.common.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * 单位转换工具类
 */
public class DensityHelper {

    private DensityHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    /**
     * dp转px
     */
    public static int dp2px(float dp) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * px转dp
     */
    public static float px2dp(float px) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (px / scale) + 0.5f;
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (px / scale) + 0.5f;
    }

    /**
     * sp转px
     */
    public static int sp2px(float sp) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return Math.round(sp * fontScale);
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float sp) {
        if (context == null) {
            return -1;
        }
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) ((sp * fontScale) + 0.5f);
    }

    /**
     * px转sp
     */
    public static float px2sp(float px) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (px / fontScale) + 0.5f;
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (px / fontScale) + 0.5f;
    }
}
