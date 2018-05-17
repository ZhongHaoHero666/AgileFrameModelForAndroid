package com.android.szh.common.logger.utils;

import android.os.Build;


import com.android.szh.common.logger.Logger;

import java.util.Locale;

/**
 * Created by sunzhonghao
 * @date 2017/9/7 18:09
 */
public class LogUtils {

    /**
     * 返回线程信息
     */
    public static String getThreadInfo() {
        Thread thread = Thread.currentThread();
        StringBuilder builder = new StringBuilder();
        builder.append("Thread: ")
                .append(thread.getName())
                .append(" [")
                .append("ID: ")
                .append(thread.getId())
                .append(", ")
                .append("Priority: ")
                .append(thread.getPriority())
                .append(", ")
                .append("State: ")
                .append(thread.getState().name())
                .append(", ")
                .append("Group: ")
                .append(thread.getThreadGroup() == null ? null : thread.getThreadGroup().getName())
                .append("]");
        return builder.toString();
    }

    /**
     * 获取顶部堆栈信息
     */
    public static String getTopStackInfo() {
        StackTraceElement caller = getCurrentStackTrace();
        if (caller == null) {
            return "";
        }
        String callerClazzName = caller.getClassName();
        String simpleClassName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String methodName = caller.getMethodName();
        String fileName = caller.getFileName();
        int lineNumber = caller.getLineNumber();
        String stackInfo;
        if (caller.isNativeMethod()) {
            stackInfo = "(Native Method)";
        } else {
            if (fileName != null && lineNumber >= 0) {
                stackInfo = stringFormat("(%s:%d)", fileName, lineNumber);
            } else if (fileName != null) {
                stackInfo = stringFormat("(%s)", fileName);
            } else {
                stackInfo = "(Unknown Source)";
            }
        }
        String stackTrace = "%s.%s%s";
        return stringFormat(stackTrace, simpleClassName, methodName, stackInfo);
    }

    /**
     * 获取当前堆栈信息
     */
    private static StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace, Logger.class);
        if (stackOffset == -1) {
            return null;
        }
        return trace[stackOffset];
    }

    /**
     * 获取堆栈信息下标
     *
     * @param trace
     * @param clazz
     */
    private static int getStackOffset(StackTraceElement[] trace, Class clazz) {
        for (int i = LogConstants.MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (clazz.equals(Logger.class)
                    && i < trace.length - 1
                    && trace[i + 1].getClassName().equals(Logger.class.getName())) {
                continue;
            }
            if (name.equals(clazz.getName())) {
                return ++i;
            }
        }
        return -1;
    }

    private static String stringFormat(String format, Object... args) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return String.format(Locale.getDefault(Locale.Category.FORMAT), format, args);
        } else {
            return String.format(Locale.getDefault(), format, args);
        }
    }

}
