package com.android.szh.common.logger.printer;

import android.util.Log;

import com.android.szh.common.logger.utils.LogConstants;
import com.android.szh.common.logger.utils.LogConvert;
import com.android.szh.common.logger.utils.LogUtils;


/**
 * Logcat打印助手(输出日志信息到Logcat)
 *
 * Created by sunzhonghao
 * @date 2017/7/24 10:33
 */
public class LogcatPrinter extends Printer {

    private boolean isPrettyFormat; // 是否打印排版线条

    public LogcatPrinter() {
        this(false);
    }

    public LogcatPrinter(boolean isPrettyFormat) {
        this.isPrettyFormat = isPrettyFormat;
    }

    @Override
    protected void printMessage(int level, String tag, String message) {
        logString(level, tag, message, false);
    }

    private void logString(int level, String tag, String message, boolean isPart) {
        //判断信息是否超过一行最大显示
        if (message.length() > LogConstants.LINE_MAX) { // 超过一行
            if (isPrettyFormat) { // 是否打印排版线条
                printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_TOP));
                if (mLogConfig.isShowThreadInfo()) { // 是否打印线程信息
                    printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_NORMAL) + LogUtils.getThreadInfo());
                    printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_CENTER));
                }
                if (mLogConfig.isShowMethodInfo()) { // 是否打印方法信息(类名/方法名/行号)
                    printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_NORMAL) + LogUtils.getTopStackInfo());
                    printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_CENTER));
                }
            }
            for (String subMsg : LogConvert.largeStringToList(message)) {
                logString(level, tag, subMsg, false);
            }
            if (isPrettyFormat) {
                printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_BOTTOM));
            }
        } else {
            //判断是否显示排版线条
            if (isPrettyFormat) { // 是否打印排版线条
                //判定是否需要分段显示
                if (isPart) {//需要分段显示
                    for (String sub : message.split(LogConstants.LINE_SEPARATOR)) {
                        printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_NORMAL) + sub);
                    }
                } else {//不需要分段显示
                    printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_TOP));
                    if (mLogConfig.isShowThreadInfo()) { // 是否打印线程信息
                        printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_NORMAL) + LogUtils.getThreadInfo());
                        printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_CENTER));
                    }
                    if (mLogConfig.isShowMethodInfo()) { // 是否打印方法信息(类名/方法名/行号)
                        printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_NORMAL) + LogUtils.getTopStackInfo());
                        printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_CENTER));
                    }
                    for (String sub : message.split(LogConstants.LINE_SEPARATOR)) {
                        printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_NORMAL) + sub);
                    }
                    printLog(level, tag, LogConvert.printDividingLine(LogConstants.DIVIDER_BOTTOM));
                }
            } else { // 直接显示
                StringBuilder builder = new StringBuilder();
                if (mLogConfig.isShowThreadInfo()) { // 是否打印线程信息
                    builder.append(LogUtils.getThreadInfo()).append(LogConstants.LINE_SEPARATOR);
                }
                if (mLogConfig.isShowMethodInfo()) { // 是否打印方法信息(类名/方法名/行号)
                    builder.append(LogUtils.getTopStackInfo()).append(LogConstants.LINE_SEPARATOR);
                }
                builder.append(message);
                printLog(level, tag, builder.toString());
            }
        }
    }

    @Override
    protected void printLog(int level, String tag, String message) {
        switch (level) {
            case Log.VERBOSE:
                Log.v(tag, message);
                break;
            case Log.INFO:
                Log.i(tag, message);
                break;
            case Log.DEBUG:
                Log.d(tag, message);
                break;
            case Log.WARN:
                Log.w(tag, message);
                break;
            case Log.ERROR:
                Log.e(tag, message);
                break;
            case Log.ASSERT:
                Log.wtf(tag, message);
                break;
            default:
                Log.println(level, tag, message);
                break;
        }
    }

}
