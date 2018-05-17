package com.android.szh.common.logger.utils;

/**
 * 日志常量
 *
 * Created by sunzhonghao
 * @date 2017/7/21 17:00
 */
public class LogConstants {

    /** TAG */
    public static final String TAG = "Logger";
    /** 换行符 */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /** 每行最大日志长度 */
    public static final int LINE_MAX = 1024 * 4;
    /** 解析属性最大层级 */
    public static final int MAX_CHILD_LEVEL = 2;
    /** 堆栈信息下标 */
    public static final int MIN_STACK_OFFSET = 5;

    /** 分割线(顶部) */
    public static final int DIVIDER_TOP = 1;
    /** 分割线(底部) */
    public static final int DIVIDER_BOTTOM = 2;
    /** 分割线(中间) */
    public static final int DIVIDER_CENTER = 4;
    /** 分割线(普通) */
    public static final int DIVIDER_NORMAL = 3;

}
