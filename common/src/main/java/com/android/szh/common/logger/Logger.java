package com.android.szh.common.logger;



import com.android.szh.common.logger.config.LogConfig;
import com.android.szh.common.logger.printer.Printer;
import com.android.szh.common.logger.printer.PrinterManager;

import java.util.Collections;
import java.util.List;

/**
 * 日志输出辅助类
 *
 * Created by sunzhonghao
 * @date 2017/7/21 16:56
 */
public class Logger {

    /**
     * 默认日志配置信息
     */
    private static final LogConfig LOG_CONFIG = LogConfig.getDefault();
    /**
     * 打印助手管理器
     */
    private static final PrinterManager PRINTER_MANAGER = new PrinterManager();


    /**
     * 获取配置信息(可重新进行设置)
     */
    public static LogConfig getLogConfig() {
        return LOG_CONFIG;
    }

    /**
     * 设置标签
     */
    public static Printer setTag(String tag) {
        List<Printer> printers = PRINTER_MANAGER.getPrinters();
        for (Printer printer : printers) {
            printer.setTag(tag);
        }
        return PRINTER_MANAGER;
    }

    /**
     * 添加打印助手
     */
    public static void addPrinter(Printer printer) {
        if (printer == null) {
            throw new NullPointerException("printer == null");
        }
        if (printer == PRINTER_MANAGER) {
            throw new IllegalArgumentException(" Cannot add a printer manager into itself.");
        }
        synchronized (PRINTER_MANAGER) {
            List<Printer> printerList = PRINTER_MANAGER.getPrinters();
            printerList.add(printer);
        }
    }

    /**
     * 添加打印助手
     */
    public static void addPrinters(Printer... printers) {
        if (printers == null) {
            throw new NullPointerException("printers == null");
        }
        for (Printer printer : printers) {
            if (printer == null) {
                throw new NullPointerException("printers contains null");
            }
            if (printer == PRINTER_MANAGER) {
                throw new IllegalArgumentException(" Cannot add a printer manager into itself.");
            }
        }
        synchronized (PRINTER_MANAGER) {
            List<Printer> printerList = PRINTER_MANAGER.getPrinters();
            Collections.addAll(printerList, printers);
        }
    }

    /**
     * 移除打印助手
     */
    public static void remove(Printer printer) {
        if (printer == null) {
            throw new NullPointerException("printers == null");
        }
        synchronized (PRINTER_MANAGER) {
            List<Printer> printerList = PRINTER_MANAGER.getPrinters();
            if (!printerList.remove(printer)) {
                throw new IllegalArgumentException("Cannot remove printer which is not added: " + printer);
            }
        }
    }

    /**
     * 移除打印助手
     */
    public static void removeAll() {
        synchronized (PRINTER_MANAGER) {
            List<Printer> printerList = PRINTER_MANAGER.getPrinters();
            printerList.clear();
        }
    }

    /**
     * 获取所有打印助手
     */
    public static List<Printer> getAll() {
        synchronized (PRINTER_MANAGER) {
            List<Printer> printerList = PRINTER_MANAGER.getPrinters();
            return Collections.unmodifiableList(printerList);
        }
    }

    /**
     * 获取当前打印助手的数量
     */
    public static int getPrinterCount() {
        synchronized (PRINTER_MANAGER) {
            return PRINTER_MANAGER.getPrinters().size();
        }
    }

    public static void v(String message, Object... args) {
        PRINTER_MANAGER.v(message, args);
    }

    public static void v(String tag, String message, Object... args) {
        PRINTER_MANAGER.v(tag, message, args);
    }

    public static void v(Object object) {
        PRINTER_MANAGER.v(object);
    }

    public static void v(String tag, Object object) {
        PRINTER_MANAGER.v(tag, object);
    }

    public static void d(String message, Object... args) {
        PRINTER_MANAGER.d(message, args);
    }

    public static void d(String tag, String message, Object... args) {
        PRINTER_MANAGER.d(tag, message, args);
    }

    public static void d(Object object) {
        PRINTER_MANAGER.d(object);
    }

    public static void d(String tag, Object object) {
        PRINTER_MANAGER.d(tag, object);
    }

    public static void i(String message, Object... args) {
        PRINTER_MANAGER.i(message, args);
    }

    public static void i(String tag, String message, Object... args) {
        PRINTER_MANAGER.i(tag, message, args);
    }

    public static void i(Object object) {
        PRINTER_MANAGER.i(object);
    }

    public static void i(String tag, Object object) {
        PRINTER_MANAGER.i(tag, object);
    }

    public static void w(String message, Object... args) {
        PRINTER_MANAGER.w(message, args);
    }

    public static void w(String tag, String message, Object... args) {
        PRINTER_MANAGER.w(tag, message, args);
    }

    public static void w(Object object) {
        PRINTER_MANAGER.w(object);
    }

    public static void w(String tag, Object object) {
        PRINTER_MANAGER.w(tag, object);
    }

    public static void e(String message, Object... args) {
        PRINTER_MANAGER.e(message, args);
    }

    public static void e(String tag, String message, Object... args) {
        PRINTER_MANAGER.e(tag, message, args);
    }

    public static void e(Object object) {
        PRINTER_MANAGER.e(object);
    }

    public static void e(String tag, Object object) {
        PRINTER_MANAGER.e(tag, object);
    }

    public static void wtf(String message, Object... args) {
        PRINTER_MANAGER.wtf(message, args);
    }

    public static void wtf(String tag, String message, Object... args) {
        PRINTER_MANAGER.wtf(tag, message, args);
    }

    public static void wtf(Object object) {
        PRINTER_MANAGER.wtf(object);
    }

    public static void wtf(String tag, Object object) {
        PRINTER_MANAGER.wtf(tag, object);
    }

    public static void json(String json) {
        PRINTER_MANAGER.json(json);
    }

    public static void json(String tag, String json) {
        PRINTER_MANAGER.json(tag, json);
    }

    public static void xml(String xml) {
        PRINTER_MANAGER.xml(xml);
    }

    public static void xml(String tag, String xml) {
        PRINTER_MANAGER.xml(tag, xml);
    }
}
