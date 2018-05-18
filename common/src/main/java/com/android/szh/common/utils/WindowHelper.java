package com.android.szh.common.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 窗口处理辅助类
 */
public class WindowHelper {

    private static final float DEFAULT_ALPHA_DIM = 0.7f;
    private static final float DEFAULT_ALPHA_LIGHT = 1.0f;
    private static final long DEFAULT_ANIM_DURATION = 350;

    /**
     * 使窗口背景变亮(窗口背景透明度变化区间[0.7, 1.0])
     */
    public static void updateWindowLight(Activity activity) {
        updateWindowAlpha(activity, DEFAULT_ALPHA_DIM, DEFAULT_ALPHA_LIGHT);
    }

    /**
     * 使窗口背景变暗(窗口背景透明度变化区间[1.0, 0.7])
     *
     * @see #updateWindowAlpha(Activity, float, float)
     */
    public static void updateWindowDim(Activity activity) {
        updateWindowAlpha(activity, DEFAULT_ALPHA_LIGHT, DEFAULT_ALPHA_DIM);
    }

    /**
     * 改变窗口背景透明度
     *
     * @see #updateWindowAlpha(Activity, float, float)
     */
    public static void updateWindowAlpha(Activity activity, float fromValue, float toValue) {
        final Window window = activity.getWindow();
        final WindowManager.LayoutParams params = window.getAttributes();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromValue, toValue);
        valueAnimator.setDuration(DEFAULT_ANIM_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                if (currentValue == DEFAULT_ALPHA_LIGHT) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                }
                params.alpha = currentValue;
                window.setAttributes(params);
            }
        });
        valueAnimator.start();
    }

}
