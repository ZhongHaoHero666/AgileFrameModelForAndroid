package com.android.szh.common.logger.utils;

import android.graphics.Bitmap;
import android.os.Build;

/**
 * Bitmap辅助类
 *
 * Created by sunzhonghao
 * @date 2017/7/25 10:36
 */
public class BitmapHelper {

    /**
     * 获取Bitmap格式化后的大小
     */
    public static String getFormatBitmapSize(Bitmap bitmap) {
        return Formatter.formatBytes(getBitmapSize(bitmap));
    }

    /**
     * 获取Bitmap的大小
     */
    private static long getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) { // API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
    }

}
