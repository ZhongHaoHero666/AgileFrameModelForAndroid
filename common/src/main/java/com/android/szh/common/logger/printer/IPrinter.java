package com.android.szh.common.logger.printer;

/**
 * 日志树接口
 *
 * Created by sunzhonghao
 * @date 2017/7/24 9:25
 */
public interface IPrinter {

    void v(String message, Object... args);

    void v(String tag, String message, Object... args);

    void v(Object object);

    void v(String tag, Object object);

    void d(String message, Object... args);

    void d(String tag, String message, Object... args);

    void d(Object object);

    void d(String tag, Object object);

    void i(String message, Object... args);

    void i(String tag, String message, Object... args);

    void i(Object object);

    void i(String tag, Object object);

    void w(String message, Object... args);

    void w(String tag, String message, Object... args);

    void w(Object object);

    void w(String tag, Object object);

    void e(String message, Object... args);

    void e(String tag, String message, Object... args);

    void e(Object object);

    void e(String tag, Object object);

    void wtf(String message, Object... args);

    void wtf(String tag, String message, Object... args);

    void wtf(Object object);

    void wtf(String tag, Object object);

    void json(String json);

    void json(String tag, String json);

    void xml(String xml);

    void xml(String tag, String xml);

}
