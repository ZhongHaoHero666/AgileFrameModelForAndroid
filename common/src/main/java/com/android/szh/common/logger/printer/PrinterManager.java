package com.android.szh.common.logger.printer;

import android.util.Log;


import com.android.szh.common.logger.utils.LogConvert;

import java.util.ArrayList;
import java.util.List;

/**
 * 打印助手管理器
 *
 * Created by sunzhonghao
 * @date 2017/7/24 10:5
 */
public class PrinterManager extends Printer {

    private volatile List<Printer> printers = new ArrayList<>(0);

    public List<Printer> getPrinters() {
        return printers;
    }

    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    @Override
    public void v(String message, Object... args) {
        printString(Log.VERBOSE, null, message, args);
    }

    @Override
    public void v(String tag, String message, Object... args) {
        printString(Log.VERBOSE, tag, message, args);
    }

    @Override
    public void v(Object object) {
        printObject(Log.VERBOSE, null, object);
    }

    @Override
    public void v(String tag, Object object) {
        printObject(Log.VERBOSE, tag, object);
    }

    @Override
    public void d(String message, Object... args) {
        printString(Log.DEBUG, null, message, args);
    }

    @Override
    public void d(String tag, String message, Object... args) {
        printString(Log.DEBUG, tag, message, args);
    }

    @Override
    public void d(Object object) {
        printObject(Log.DEBUG, null, object);
    }

    @Override
    public void d(String tag, Object object) {
        printObject(Log.DEBUG, tag, object);
    }

    @Override
    public void i(String message, Object... args) {
        printString(Log.INFO, null, message, args);
    }

    @Override
    public void i(String tag, String message, Object... args) {
        printString(Log.INFO, tag, message, args);
    }

    @Override
    public void i(Object object) {
        printObject(Log.INFO, null, object);
    }

    @Override
    public void i(String tag, Object object) {
        printObject(Log.INFO, tag, object);
    }

    @Override
    public void w(String message, Object... args) {
        printString(Log.WARN, null, message, args);
    }

    @Override
    public void w(String tag, String message, Object... args) {
        printString(Log.WARN, tag, message, args);
    }

    @Override
    public void w(Object object) {
        printObject(Log.WARN, null, object);
    }

    @Override
    public void w(String tag, Object object) {
        printObject(Log.WARN, tag, object);
    }

    @Override
    public void e(String message, Object... args) {
        printString(Log.ERROR, null, message, args);
    }

    @Override
    public void e(String tag, String message, Object... args) {
        printString(Log.ERROR, tag, message, args);
    }

    @Override
    public void e(Object object) {
        printObject(Log.ERROR, null, object);
    }

    @Override
    public void e(String tag, Object object) {
        printObject(Log.ERROR, tag, object);
    }

    @Override
    public void wtf(String message, Object... args) {
        printString(Log.ASSERT, null, message, args);
    }

    @Override
    public void wtf(String tag, String message, Object... args) {
        printString(Log.ASSERT, tag, message, args);
    }

    @Override
    public void wtf(Object object) {
        printObject(Log.ASSERT, null, object);
    }

    @Override
    public void wtf(String tag, Object object) {
        printObject(Log.ASSERT, tag, object);
    }

    @Override
    public void json(String json) {
        json(null, json);
    }

    @Override
    public void json(String tag, String json) {
        for (IPrinter printer : printers) {
            printer.json(tag, json);
        }
    }

    @Override
    public void xml(String xml) {
        xml(null, xml);
    }

    @Override
    public void xml(String tag, String xml) {
        for (Printer printer : printers) {
            printer.xml(tag, xml);
        }
    }

    @Override
    protected void printLog(int level, String tag, String message) {
        throw new AssertionError("Missing override for log method.");
    }

    private void printObject(int level, String tag, Object object) {
        printString(level, tag, LogConvert.objectToString(object));
    }

    private void printString(int level, String tag, String message, Object... args) {
        synchronized (PrinterManager.class) {
            for (Printer printer : printers) {
                switch (level) {
                    case Log.VERBOSE:
                        printer.v(tag, message, args);
                        break;
                    case Log.INFO:
                        printer.i(tag, message, args);
                        break;
                    case Log.DEBUG:
                        printer.d(tag, message, args);
                        break;
                    case Log.WARN:
                        printer.w(tag, message, args);
                        break;
                    case Log.ERROR:
                        printer.e(tag, message, args);
                        break;
                    case Log.ASSERT:
                        printer.wtf(tag, message, args);
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
