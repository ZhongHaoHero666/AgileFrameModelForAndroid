package com.android.szh.common.logger.utils;

/**
 * 格式化文件大小工具类
 *
 * Created by sunzhonghao
 * @date 2017/7/26 15:53
 */
class Formatter {

    private static final long KB_IN_BYTES = 1024;
    private static final long MB_IN_BYTES = KB_IN_BYTES * 1024;
    private static final long GB_IN_BYTES = MB_IN_BYTES * 1024;
    private static final long TB_IN_BYTES = GB_IN_BYTES * 1024;
    private static final long PB_IN_BYTES = TB_IN_BYTES * 1024;

    static String formatBytes(long sizeBytes) {
        final boolean isNegative = (sizeBytes < 0);
        float result = isNegative ? -sizeBytes : sizeBytes;
        String suffix = "B";
        long mult = 1;
        if (result > 900) {
            suffix = "KB";
            mult = KB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "MB";
            mult = MB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "GB";
            mult = GB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "TB";
            mult = TB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "PB";
            mult = PB_IN_BYTES;
            result = result / 1024;
        }
        final String roundFormat;
        if (mult == 1 || result >= 100) {
            roundFormat = "%.0f";
        } else if (result < 1) {
            roundFormat = "%.2f";
        } else if (result < 10) {
            roundFormat = "%.2f";
        } else { // 10 <= result < 100
            roundFormat = "%.2f";
        }

        if (isNegative) {
            result = -result;
        }
        final String roundedString = String.format(roundFormat, result);
        return String.format("%s %s", roundedString, suffix);
    }

}
